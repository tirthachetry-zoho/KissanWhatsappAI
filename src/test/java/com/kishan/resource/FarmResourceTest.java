package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class FarmResourceTest {

    private long createFarmer(String phone) {
        return given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetFarm() {
        long farmerId = createFarmer("919710000001");

        long farmId = given()
                .contentType("application/json")
                .body("""
                        {"farmerId": %d, "location": "Mysuru", "area": 2.5, "areaUnit": "acres", "soilType": "red"}
                        """.formatted(farmerId))
                .when().post("/api/farms")
                .then()
                .statusCode(201)
                .body("farmerId", equalTo((int) farmerId))
                .body("location", equalTo("Mysuru"))
                .body("area", equalTo(2.5f))
                .body("soilType", equalTo("red"))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/farms/{id}", farmId)
                .then()
                .statusCode(200)
                .body("areaUnit", equalTo("acres"));
    }

    @Test
    void createFarmWithMissingFarmerReturns400() {
        given()
                .contentType("application/json")
                .body("""
                        {"farmerId": 99999999, "location": "Noplace"}
                        """)
                .when().post("/api/farms")
                .then()
                .statusCode(400)
                .body("error", equalTo("Farmer not found"));
    }

    @Test
    void listFarmsByFarmer() {
        long farmerId = createFarmer("919600010009");

        given()
                .contentType("application/json")
                .body("{\"farmerId\": %d, \"location\": \"Hubli\"}".formatted(farmerId))
                .when().post("/api/farms")
                .then().statusCode(201);

        given()
                .when().get("/api/farms/by-farmer/{farmerId}", farmerId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].location", equalTo("Hubli"));
    }

    @Test
    void createFarmWithoutFarmerIdIsRejected() {
        given()
                .contentType("application/json")
                .body("""
                        {"location": "No Farmer Field"}
                        """)
                .when().post("/api/farms")
                .then()
                .statusCode(400);
    }

    @Test
    void deleteFarmRemovesIt() {
        long farmerId = createFarmer("919600010002");
        long farmId = given()
                .contentType("application/json")
                .body("{\"farmerId\": %d}".formatted(farmerId))
                .when().post("/api/farms")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/farms/{id}", farmId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/farms/{id}", farmId)
                .then()
                .statusCode(404);
    }
}