package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class MessageResourceTest {

    private long createConversation(String phone) {
        long farmerId = given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        return given()
                .contentType("application/json")
                .body("{\"farmerId\": %d}".formatted(farmerId))
                .when().post("/api/conversations")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    @Test
    void createAndGetMessage() {
        long conversationId = createConversation("919630010001");
        long messageId = given()
                .contentType("application/json")
                .body("""
                        {"conversationId": %d, "direction": "INCOMING", "content": "Tomato price today"}
                        """.formatted(conversationId))
                .when().post("/api/messages")
                .then()
                .statusCode(201)
                .body("direction", equalTo("INCOMING"))
                .body("messageType", equalTo("TEXT"))
                .body("content", equalTo("Tomato price today"))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/messages/{id}", messageId)
                .then()
                .statusCode(200)
                .body("conversationId", equalTo((int) conversationId));
    }

    @Test
    void messageTypeDefaultsToText() {
        long conversationId = createConversation("919630010002");
        given()
                .contentType("application/json")
                .body("{\"conversationId\": %d, \"direction\": \"OUTGOING\", \"content\": \"Hello\"}".formatted(conversationId))
                .when().post("/api/messages")
                .then()
                .statusCode(201)
                .body("messageType", equalTo("TEXT"));
    }

    @Test
    void createMessageWithUnknownConversationReturns400() {
        given()
                .contentType("application/json")
                .body("""
                        {"conversationId": 99999999, "direction": "INCOMING", "content": "Hi"}
                        """)
                .when().post("/api/messages")
                .then()
                .statusCode(400)
                .body("error", equalTo("Conversation not found"));
    }

    @Test
    void createMessageWithoutDirectionIsRejected() {
        long conversationId = createConversation("919630010003");
        given()
                .contentType("application/json")
                .body("{\"conversationId\": %d, \"content\": \"No direction\"}".formatted(conversationId))
                .when().post("/api/messages")
                .then()
                .statusCode(400);
    }

    @Test
    void listMessagesByConversation() {
        long conversationId = createConversation("919630010004");
        given()
                .contentType("application/json")
                .body("{\"conversationId\": %d, \"direction\": \"INCOMING\", \"content\": \"First\"}".formatted(conversationId))
                .when().post("/api/messages")
                .then().statusCode(201);
        given()
                .contentType("application/json")
                .body("{\"conversationId\": %d, \"direction\": \"OUTGOING\", \"content\": \"Second\"}".formatted(conversationId))
                .when().post("/api/messages")
                .then().statusCode(201);

        given()
                .when().get("/api/messages/by-conversation/{conversationId}", conversationId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    void deleteMessageRemovesIt() {
        long conversationId = createConversation("919630010005");
        long messageId = given()
                .contentType("application/json")
                .body("{\"conversationId\": %d, \"direction\": \"INCOMING\"}".formatted(conversationId))
                .when().post("/api/messages")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/messages/{id}", messageId)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/messages/{id}", messageId)
                .then()
                .statusCode(404);
    }
}