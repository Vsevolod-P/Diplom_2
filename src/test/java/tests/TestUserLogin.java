package tests;

import apiHelper.UserApi;
import apiHelper.UserCreationApi;
import apiHelper.User;
import apiHelper.UserLoginApi;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class TestUserLogin {

    private User user;
    private UserApi UserApi;
    private boolean isUserCreated;
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        // Создаем пользователя перед каждым тестом
        UserApi = new UserApi();
        user = UserCreationApi.createUser();
        isUserCreated = false;
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (isUserCreated) {
            UserApi.deleteUser(user.getEmail(), user.getPassword());
        }
    }

    @Test
    @Description("Проверка ручки логина")
    @Step("Возможность логина")
    public void userCanLogin() {
        // Создаем объект для логина
        UserApi.createUserRequest(user); //создаем и логинимся курьером только в этом тесте
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest(user.getEmail(), user.getPassword());
        Response response = UserApi.doPostRequest(UserApi.API_LOGIN_PATH, loginRequest);

        response.then()
                .statusCode(200) // Успешный запрос
                .body("accessToken", notNullValue()); // Проверка, что возвращается accessToken
        isUserCreated = true;
    }

    @Test
    @Description("Проверка не валидных данных")
    @Step("Ввод не правильных данных")
    public void loginWithInvalidCredentials() {
        String email = "invalidEmail@yan.ru";
        String password = "invalidPassword";

        UserLoginApi.LoginRequest invalidLoginRequest = new UserLoginApi.LoginRequest(email, password);

        Response response = UserApi.doPostRequest(UserApi.API_LOGIN_PATH, invalidLoginRequest);

        response.then()
                .statusCode(401) // Код ошибки для неверных учетных данных
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Проверка пустых полей")
    @Step("Логин с пустым логином")
    public void loginWithoutRequiredFields() {
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest("", "somePassword");

        Response response = UserApi.doPostRequest(UserApi.API_LOGIN_PATH, loginRequest);

        response.then()
                .statusCode(401) // Код ошибки для неверных учетных данных
                .body("message", equalTo("email or password are incorrect"));
    }
}
