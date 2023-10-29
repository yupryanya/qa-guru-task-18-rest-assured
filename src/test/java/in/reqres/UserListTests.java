package in.reqres;

import in.reqres.models.UserListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static in.reqres.specs.UserListSpec.userListRequestSpec;
import static in.reqres.specs.UserListSpec.userListResponseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserListTests {
    @Test
    @DisplayName("User list page contains the expected number of items per page")
    void userListPaginationTest() {
        UserListResponseModel response = step("Get user list", () ->
                given(userListRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(userListResponseSpec)
                        .extract().as(UserListResponseModel.class));

        step("Verify response", () -> {
            assertEquals(2, response.getPage());
            assertEquals(6, response.getPer_page());
            assertEquals(6, response.getData().size());
        });
    }

    @ParameterizedTest
    @CsvSource({"7,  Michael, Lawson, michael.lawson@reqres.in"})
    @DisplayName("User list contains valid user data")
    void userListContainsCoreectDataTest(int id, String firstName, String lastName, String email) {
        UserListResponseModel response = step("Get user list", () ->
                given(userListRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(userListResponseSpec)
                        .extract().as(UserListResponseModel.class));

        step("Verify response body contains valid data", () -> {
            var userData = response.getData().get(0);
            assertEquals(id, userData.getId(), "Id");
            assertEquals(firstName, userData.getFirst_name(), "First name");
            assertEquals(lastName, userData.getLast_name(), "Last name");
            assertEquals(email, userData.getEmail(), "Email");
        });
    }
}