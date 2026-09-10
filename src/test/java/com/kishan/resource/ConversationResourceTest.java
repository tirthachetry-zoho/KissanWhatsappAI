package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class ConversationResourceTest {

    private long createFarmer(String phone) {
        return given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetConversation() {
        long farmerId = createFarmer("919620010001");
        long conversationId = given()
                .contentType("application/json")
                .body("""
                        {"farmerId": %d, "intent": "crop_help", "status": "ACTIVE"}
                        """.formatted(farmerId))
                .when().post("/api/conversations")
                .then()
                .statusCode(201)
                .body("intent", equalTo("crop_help"))
                .body("status", equalTo("ACTIVE"))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/conversations/{id}", conversationId)
                .then()
                .statusCode(200)
                .body("farmerId", equalTo((int) farmerId));
    }

    @Test
    void newConversationDefaultsToActiveStatus() {
        long farmerId = createFarmer("919620010002");
        given()
                .contentType("application/json")
                .body("{\"farmerId\": %d}".formatted(farmerId))
                .when().post("/api/conversations")
                .then()
                .statusCode(201)
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    void createConversationWithUnknownFarmerReturns400() {
        given()
                .contentType("application/json")
                .body("""
                        {"farmerId": 99999999}
                        """)
                .when().post("/api/conversations")
                .then()
                .statusCode(400)
                .body("error", equalTo("Farmer not found"));
    }

    @Test
    void listConversationsByFarmer() {
        long farmerId = createFarmer("919620010003");
        given()
                .contentType("application/json")
                .body("{\"farmerId\": %d, \"intent\": \"weather\"}".formatted(farmerId))
                .when().post("/api/conversations")
                .then().statusCode(201);

        given()
                .when().get("/api/conversations/by-farmer/{farmerId}", farmerId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].intent", equalTo("weather"));
    }

    @Test
    void deleteConversationRemovesIt() {
        long farmerId = createFarmer("919620010004");
        long conversationId = given()
                .contentType("application/json")
                .body("{\"farmerId\": %d}".formatted(farmerId))
                .when().post("/api/conversations")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/conversations/{id}", conversationId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/conversations/{id}", conversationId)
                .then()
                .statusCode(404);
    }
}