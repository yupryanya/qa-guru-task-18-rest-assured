package in.reqres;

import in.reqres.models.RegisterBodyModel;
import in.reqres.models.SuccessfulRegisterResponseModel;
import in.reqres.models.UnsuccessfulRegisterResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static in.reqres.specs.RegisterSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationTests {
    @Test
    @DisplayName("Positive Test: Successful Registration")
    void successfulRegistrationTest() {
        RegisterBodyModel registerData = new RegisterBodyModel();
        registerData.setEmail("michael.lawson@reqres.in");
        registerData.setPassword("P@ssw0rd");

        SuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(registerRequestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registerResponseSpec)
                        .extract().as(SuccessfulRegisterResponseModel.class));
        step("Verify response body", () -> {
            assertNotNull("ID should not be null", response.getId());
            assertNotNull("Token should not be null", response.getToken());
        });
    }

    @ParameterizedTest(name = "When password is \"{1}\" error message is \"{2}\"")
    @CsvSource({"michael.lawson@reqres.in,   invalid_password, Missing password",
                "lindsay.ferguson@reqres.in, '',               Missing password"
    })
    @DisplayName("Negative Test: Invalid Password")
    void invalidPasswordRegistrationTest(String email, String password, String errorMessage) {
        RegisterBodyModel registerData = new RegisterBodyModel();
        registerData.setEmail(email);
        registerData.setPassword(password);

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(registerRequestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(invalidData400ResponseSpec)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals(errorMessage, response.getError())
        );
    }

    @Test
    @DisplayName("Negative Test: Non-existent User")
    void nonExistentUserUnsuccessfulRegistrationTest() {
        RegisterBodyModel registerData = new RegisterBodyModel();
        registerData.setEmail("nonexistent.user@email.com");
        registerData.setPassword("P@ssw0rd");

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(registerRequestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(invalidData400ResponseSpec)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals("Note: Only defined users succeed registration", response.getError())
        );
    }

    @Test
    @DisplayName("Negative Test: Empty Email")
    void emptyEmailUnsuccessfulRegistrationTest() {
        RegisterBodyModel registerData = new RegisterBodyModel();
        registerData.setEmail("");
        registerData.setPassword("P@ssw0rd");

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(registerRequestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(invalidData400ResponseSpec)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals("Missing email or username", response.getError())
        );
    }
}