package in.reqres;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationAPITests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Positive Test: Successful Registration")
    void successfulRegistrationTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"michael.lawson@reqres.in\", \"password\": \"P@ssw0rd\" }")
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Negative Test: Invalid Password")
    void invalidPasswordUnsuccessfulRegistrationTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"michael.lawson@reqres.in\", \"password\": \"invalid_password\" }")
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Negative Test: Non-existent User")
    void nonExistentUserUnsuccessfulRegistrationTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"nonexistent.user@email.com\", \"password\": \"P@ssw0rd\" }")
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @DisplayName("Negative Test: Empty Email")
    void emptyNameUnsuccessfulRegistrationTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"\", \"password\": \"P@ssw0rd\" }")
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Negative Test: Empty Password")
    void emptyPasswordUnsuccessfulRegistrationTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"michael.lawson@reqres.in\", \"password\": \"\" }")
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}