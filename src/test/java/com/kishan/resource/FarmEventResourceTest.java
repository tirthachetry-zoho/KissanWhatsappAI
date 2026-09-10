package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class FarmEventResourceTest {

    private long createFarm(String phone) {
        long farmerId = given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        return given()
                .contentType("application/json")
                .body("{\"farmerId\": %d}".formatted(farmerId))
                .when().post("/api/farms")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetFarmEvent() {
        long farmId = createFarm("919640010001");
        long eventId = given()
                .contentType("application/json")
                .body("""
                        {"farmId": %d, "eventType": "fertilizer", "date": "2026-09-10", "quantity": "2 bags", "cost": 1800.0, "notes": "Bought fertilizer"}
                        """.formatted(farmId))
                .when().post("/api/farm-events")
                .then()
                .statusCode(201)
                .body("eventType", equalTo("fertilizer"))
                .body("date", equalTo("2026-09-10"))
                .body("quantity", equalTo("2 bags"))
                .body("cost", equalTo(1800.0f))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/farm-events/{id}", eventId)
                .then()
                .statusCode(200)
                .body("notes", equalTo("Bought fertilizer"));
    }

    @Test
    void createFarmEventWithUnknownFarmReturns400() {
        given()
                .contentType("application/json")
                .body("""
                        {"farmId": 99999999, "eventType": "pesticide", "date": "2026-09-10"}
                        """)
                .when().post("/api/farm-events")
                .then()
                .statusCode(400)
                .body("error", equalTo("Farm not found"));
    }

    @Test
    void createFarmEventWithoutEventTypeOrDateIsRejected() {
        long farmId = createFarm("919640010002");
        given()
                .contentType("application/json")
                .body("{\"farmId\": %d}".formatted(farmId))
                .when().post("/api/farm-events")
                .then()
                .statusCode(400);
    }

    @Test
    void listFarmEventsByFarm() {
        long farmId = createFarm("919640010003");
        given()
                .contentType("application/json")
                .body("{\"farmId\": %d, \"eventType\": \"harvest\", \"date\": \"2026-09-11\"}".formatted(farmId))
                .when().post("/api/farm-events")
                .then().statusCode(201);

        given()
                .when().get("/api/farm-events/by-farm/{farmId}", farmId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].eventType", equalTo("harvest"));
    }

    @Test
    void deleteFarmEventRemovesIt() {
        long farmId = createFarm("919640010004");
        long eventId = given()
                .contentType("application/json")
                .body("{\"farmId\": %d, \"eventType\": \"irrigation\", \"date\": \"2026-09-12\"}".formatted(farmId))
                .when().post("/api/farm-events")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/farm-events/{id}", eventId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/farm-events/{id}", eventId)
                .then()
                .statusCode(404);
    }
}