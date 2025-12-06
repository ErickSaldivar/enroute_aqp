package org.utp;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testGetAllUsers() {
        given()
          .when().get("/api/users")
          .then()
             .statusCode(200);
    }

}