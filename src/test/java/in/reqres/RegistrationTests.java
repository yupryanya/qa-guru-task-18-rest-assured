package in.reqres;

import in.reqres.models.RegisterRequestModel;
import in.reqres.models.SuccessfulRegisterResponseModel;
import in.reqres.models.UnsuccessfulRegisterResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.ReqresApiSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationTests {
    @Test
    @DisplayName("Successful registration with valid login and username")
    void successfulRegistrationTest() {
        RegisterRequestModel registerData = new RegisterRequestModel();
        registerData.setEmail("michael.lawson@reqres.in");
        registerData.setPassword("P@ssw0rd");

        SuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpecWithStatusCode200)
                        .extract().as(SuccessfulRegisterResponseModel.class));
        step("Verify response body", () -> {
            assertNotNull("ID should not be null", response.getId());
            assertNotNull("Token should not be null", response.getToken());
        });
    }

    @Test
    @DisplayName("Unsuccessful registartion with missing password")
    void invalidPasswordRegistrationTest() {
        RegisterRequestModel registerData = new RegisterRequestModel();
        registerData.setEmail("eve.holt@reqres.in");
        registerData.setPassword("");

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpecWithStatusCode400)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals("Missing password", response.getError())
        );
    }

    @Test
    @DisplayName("Unsuccessful registration with non-defined user")
    void nonExistentUserUnsuccessfulRegistrationTest() {
        RegisterRequestModel registerData = new RegisterRequestModel();
        registerData.setEmail("nonexistent.user@email.com");
        registerData.setPassword("P@ssw0rd");

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpecWithStatusCode400)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals("Note: Only defined users succeed registration", response.getError())
        );
    }

    @Test
    @DisplayName("Unsuccessful registration with empty email")
    void emptyEmailUnsuccessfulRegistrationTest() {
        RegisterRequestModel registerData = new RegisterRequestModel();
        registerData.setEmail("");
        registerData.setPassword("P@ssw0rd");

        UnsuccessfulRegisterResponseModel response = step("Make login request", () ->
                given(requestSpec)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpecWithStatusCode400)
                        .extract().as(UnsuccessfulRegisterResponseModel.class));
        step("Verify response body", () ->
                assertEquals("Missing email or username", response.getError())
        );
    }
}