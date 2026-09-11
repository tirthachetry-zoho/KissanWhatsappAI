package com.kishan.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class FarmerResourceTest {

    @Test
    void createAndGetFarmer() {
        long id = given()
                .contentType("application/json")
                .body("""
                        {"phoneNumber": "919700000001", "name": "Ramesh", "language": "kn", "location": "Mandya"}
                        """)
                .when().post("/api/farmers")
                .then()
                .statusCode(201)
                .body("phoneNumber", equalTo("919700000001"))
                .body("name", equalTo("Ramesh"))
                .body("language", equalTo("kn"))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/farmers/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Ramesh"))
                .body("location", equalTo("Mandya"));
    }

    @Test
    void creatingFarmerWithoutPhoneIsRejected() {
        given()
                .contentType("application/json")
                .body("""
                        {"name": "No Phone"}
                        """)
                .when().post("/api/farmers")
                .then()
                .statusCode(400);
    }

    @Test
    void duplicatePhoneNumberReturnsConflict() {
        String phone = "919700000002";
        given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201);

        given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\", \"name\": \"Duplicate\"}".formatted(phone))
                .when().post("/api/farmers")
                .then()
                .statusCode(409)
                .body("error", equalTo("Farmer with this phone number already exists"));
    }

    @Test
    void listFarmersContainsCreated() {
        String phone = "919700000003";
        given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"%s\", \"name\": \"Listed\"}".formatted(phone))
                .when().post("/api/farmers")
                .then().statusCode(201);

        given()
                .when().get("/api/farmers")
                .then()
                .statusCode(200)
                .body("phoneNumber", hasItem(phone));
    }

    @Test
    void updateFarmerChangesFields() {
        long id = given()
                .contentType("application/json")
                .body("""
                        {"phoneNumber": "919700000004", "name": "Before", "language": "en"}
                        """)
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .contentType("application/json")
                .body("""
                        {"phoneNumber": "919700000004", "name": "After", "language": "hi"}
                        """)
                .when().put("/api/farmers/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("After"))
                .body("language", equalTo("hi"));

        given()
                .when().get("/api/farmers/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("After"));
    }

    @Test
    void locationDefaultsToMadhyaPradeshWhenOmitted() {
        long id = given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"919700000006\", \"name\": \"MP Farmer\"}")
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/farmers/{id}", id)
                .then()
                .statusCode(200)
                .body("location", equalTo("Madhya Pradesh"));
    }

    @Test
    void explicitLocationIsKept() {
        given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"919700000007\", \"name\": \"Local Farmer\", \"location\": \"Indore\"}")
                .when().post("/api/farmers")
                .then().statusCode(201)
                .body("location", equalTo("Indore"));
    }

    @Test
    void updateDoesNotWipeLocationWhenOmitted() {
        long id = given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"919700000008\", \"name\": \"Update Me\"}")
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .contentType("application/json")
                .body("{\"phoneNumber\": \"919700000008\", \"name\": \"Updated\"}")
                .when().put("/api/farmers/{id}", id)
                .then()
                .statusCode(200)
                .body("location", equalTo("Madhya Pradesh"));
    }

    @Test
    void getMissingFarmerReturns404() {
        given()
                .when().get("/api/farmers/99999999")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteFarmerRemovesIt() {
        long id = given()
                .contentType("application/json")
                .body("""
                        {"phoneNumber": "919700000005", "name": "DeleteMe"}
                        """)
                .when().post("/api/farmers")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when().delete("/api/farmers/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when().get("/api/farmers/{id}", id)
                .then()
                .statusCode(404);
    }
}