package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class CropResourceTest {

    private long createFarmer(String phone) {
        return given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    private long createFarm(long farmerId) {
        return given()
                .contentType("application/json")
                .body("{\"farmerId\": %d, \"location\": \"Hubballi\"}".formatted(farmerId))
                .when().post("/api/farms")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetCrop() {
        long farmId = createFarm(createFarmer("919610010001"));

        long cropId = given()
                .contentType("application/json")
                .body("""
                        {"farmId": %d, "crop": "Tomato", "variety": "Arka Vikas", "plantingDate": "2026-06-01", "growthStage": "vegetative"}
                        """.formatted(farmId))
                .when().post("/api/crops")
                .then()
                .statusCode(201)
                .body("crop", equalTo("Tomato"))
                .body("variety", equalTo("Arka Vikas"))
                .body("plantingDate", equalTo("2026-06-01"))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/crops/{id}", cropId)
                .then()
                .statusCode(200)
                .body("growthStage", equalTo("vegetative"));
    }

    @Test
    void createCropWithUnknownFarmReturns400() {
        given()
                .contentType("application/json")
                .body("""
                        {"farmId": 99999999, "crop": "Unknown"}
                        """)
                .when().post("/api/crops")
                .then()
                .statusCode(400)
                .body("error", equalTo("Farm not found"));
    }

    @Test
    void createCropWithoutNameIsRejected() {
        long farmId = createFarm(createFarmer("919610010002"));
        given()
                .contentType("application/json")
                .body("{\"farmId\": %d}".formatted(farmId))
                .when().post("/api/crops")
                .then()
                .statusCode(400);
    }

    @Test
    void listCropsByFarm() {
        long farmId = createFarm(createFarmer("919610010003"));
        given()
                .contentType("application/json")
                .body("{\"farmId\": %d, \"crop\": \"Cotton\"}".formatted(farmId))
                .when().post("/api/crops")
                .then().statusCode(201);

        given()
                .when().get("/api/crops/by-farm/{farmId}", farmId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].crop", equalTo("Cotton"));
    }

    @Test
    void deleteCropRemovesIt() {
        long farmId = createFarm(createFarmer("919610010004"));
        long cropId = given()
                .contentType("application/json")
                .body("{\"farmId\": %d, \"crop\": \"Maize\"}".formatted(farmId))
                .when().post("/api/crops")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/crops/{id}", cropId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/crops/{id}", cropId)
                .then()
                .statusCode(404);
    }
}