package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
public class WhatsAppWebhookResourceTest {

    private static final String VERIFY_TOKEN = "dev-verify-token";

    @Test
    void verificationEchoesChallengeForValidToken() {
        given()
                .queryParam("hub.mode", "subscribe")
                .queryParam("hub.verify_token", VERIFY_TOKEN)
                .queryParam("hub.challenge", "1234567890")
                .when().get("/webhook")
                .then()
                .statusCode(200)
                .body(equalTo("1234567890"));
    }

    @Test
    void verificationRejectsInvalidToken() {
        given()
                .queryParam("hub.mode", "subscribe")
                .queryParam("hub.verify_token", "wrong-token")
                .queryParam("hub.challenge", "1234567890")
                .when().get("/webhook")
                .then()
                .statusCode(403);
    }

    @Test
    void receiveTextCreatesFarmerConversationAndMessages() {
        String phone = "919650010001";
        String payload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "WABA_ID",
                    "changes": [{
                      "field": "messages",
                      "value": {
                        "messaging_product": "whatsapp",
                        "metadata": {"display_phone_number": "15550000000", "phone_number_id": "PHONE_ID"},
                        "contacts": [{"profile": {"name": "Ravi"}, "wa_id": "%s"}],
                        "messages": [{"from": "%s", "id": "wamid.1", "timestamp": "1700000000", "type": "text", "text": {"body": "Tomato price today"}}]
                      }
                    }]
                  }]
                }
                """.formatted(phone, phone);

        given()
                .contentType("application/json")
                .body(payload)
                .when().post("/webhook")
                .then()
                .statusCode(200);

        // Farmer created with profile name
        given()
                .when().get("/api/farmers")
                .then()
                .statusCode(200)
                .body("phoneNumber", hasItem(phone));

        long farmerId = given()
                .when().get("/api/farmers")
                .then().statusCode(200)
                .extract().jsonPath().getLong("find { it.phoneNumber == '%s' }.id".formatted(phone));

        // One active conversation
        given()
                .when().get("/api/conversations/by-farmer/{farmerId}", farmerId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].status", equalTo("ACTIVE"));

        long conversationId = given()
                .when().get("/api/conversations/by-farmer/{farmerId}", farmerId)
                .then().statusCode(200)
                .extract().jsonPath().getLong("[0].id");

        // Two messages: incoming + generated assistant reply
        given()
                .when().get("/api/messages/by-conversation/{conversationId}", conversationId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].direction", equalTo("INCOMING"))
                .body("[1].direction", equalTo("OUTGOING"))
                .body("[1].content", equalTo("Market prices can change during the day. Send your crop name to check today's local mandi price."));
    }

    @Test
    void receiveImageMessageStoresImageType() {
        String phone = "139650010002";
        String payload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "WABA_ID",
                    "changes": [{
                      "field": "messages",
                      "value": {
                        "messaging_product": "whatsapp",
                        "contacts": [{"profile": {"name": "Kavitha"}, "wa_id": "%s"}],
                        "messages": [{"from": "%s", "id": "wamid.2", "timestamp": "1700000000", "type": "image", "image": {"id": "MEDIA_ID", "caption": "crop photo"}}]
                      }
                    }]
                  }]
                }
                """.formatted(phone, phone);

        given()
                .contentType("application/json")
                .body(payload)
                .when().post("/webhook")
                .then()
                .statusCode(200);

        long farmerId = given()
                .when().get("/api/farmers")
                .then().statusCode(200)
                .extract().jsonPath().getLong("find { it.phoneNumber == '%s' }.id".formatted(phone));

        long conversationId = given()
                .when().get("/api/conversations/by-farmer/{farmerId}", farmerId)
                .then().statusCode(200)
                .extract().jsonPath().getLong("[0].id");

        given()
                .when().get("/api/messages/by-conversation/{conversationId}", conversationId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].messageType", equalTo("IMAGE"))
                .body("[0].content", equalTo("crop photo"));
    }

    @Test
    void receiveMessageSwitchesLanguageToKannada() {
        String phone = "139900010003";
        String payload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "WABA_ID",
                    "changes": [{
                      "field": "messages",
                      "value": {
                        "messaging_product": "whatsapp",
                        "contacts": [{"profile": {"name": "Gowda"}, "wa_id": "%s"}],
                        "messages": [{"from": "%s", "id": "wamid.3", "timestamp": "1700000000", "type": "text", "text": {"body": "Please respond in Kannada"}}]
                      }
                    }]
                  }]
                }
                """.formatted(phone, phone);

        given()
                .contentType("application/json")
                .body(payload)
                .when().post("/webhook")
                .then()
                .statusCode(200);

        given()
                .when().get("/api/farmers")
                .then()
                .statusCode(200)
                .body("find { it.phoneNumber == '%s' }.language".formatted(phone), equalTo("kn"));
    }

    @Test
    void receiveWithoutMessagesReturns200() {
        String payload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "WABA_ID",
                    "changes": [{
                      "field": "statuses",
                      "value": {"messaging_product": "whatsapp"}
                    }]
                  }]
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when().post("/webhook")
                .then()
                .statusCode(200);
    }
}