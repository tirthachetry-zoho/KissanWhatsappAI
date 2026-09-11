package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class HomeResourceTest {

    @Test
    void homeServesLandingPage() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(containsString("Kissan-AI"))
                .body(containsString("/health"))
                .body(containsString("/webhook"));
    }
}