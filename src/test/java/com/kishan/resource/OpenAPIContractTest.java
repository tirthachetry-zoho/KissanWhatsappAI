package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Guards the machine-readable OpenAPI contract: the spec must expose every
 * public route (/, /health, /webhook, /webhook/openwa, /api/*) and the UI
 * landing link from the homepage must resolve.
 */
@QuarkusTest
class OpenAPIContractTest {

    @Test
    void openapiSpecCoversAllPublicRoutes() {
        given()
                .when().get("/q/openapi?format=json")
                .then()
                .statusCode(200)
                .contentType(containsString("json"))
                .body("openapi", startsWith("3."))
                .body("info.title", equalTo("Kissan-AI API"))
                .body("paths.'/'", notNullValue())
                .body("paths.'/health'", notNullValue())
                .body("paths.'/webhook'", notNullValue())
                .body("paths.'/webhook/openwa'", notNullValue())
                .body("paths.'/api/farmers'", notNullValue())
                .body("paths.'/api/farms'", notNullValue())
                .body("paths.'/api/crops'", notNullValue())
                .body("paths.'/api/conversations'", notNullValue())
                .body("paths.'/api/messages'", notNullValue())
                .body("paths.'/api/farm-events'", notNullValue());
    }

    @Test
    void homepageLinksToContract() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(containsString("/q/openapi"))
                .body(containsString("/docs"));
    }

    @Test
    void swaggerUiIsServed() {
        given()
                .when().get("/docs")
                .then()
                .statusCode(anyOf(is(200), is(301), is(302), is(308)));
    }
}
