package tests;

import apiHelper.UserApi;
import apiHelper.UserCreationApi;
import apiHelper.User;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class TestUserCreation {

    private User user;
    private UserApi UserApi;
    private boolean isUserCreated;

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
    @Description("Проверка ручки создания пользователя")
    @Step("Создаем пользователя")
    public void courierCanBeCreated() {
        Response response = UserApi.createUserRequest(user);

        response.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        isUserCreated = true; // ловим созданного пользователя
    }
    @Test
    @Description("Проверка создания пользователя c пустым email")
    @Step("Попытка создания пользователя с пустым email")
    public void cannotCreateCourierWithoutRequiredFields() {
        // Пример без email
        User emptyUser = new User("", "Password123", "Saske");
        Response response = UserApi.createUserRequest(emptyUser);
        response.then()
                .statusCode(403) // Проверяем, что статус ответа 403
                .body("message", equalTo("Email, password and name are required fields"));

    }
    @Test
    @Description("Проверка ручки создания существующего пользователя")
    @Step("Создание пользователя с существующим email")
    public void cannotCreateCourierWithExistingLogin() {
        // Пытаемся создать пользователя с тем же логином
        Response response = UserApi.createUserRequest(user);
        //создаем первого пользователя
        response.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        Response responseDublicate = UserApi.createUserRequest(user);

        responseDublicate.then()
                .statusCode(403) // Проверяем, что статус ответа 403
                .body("message", equalTo("User already exists"));
        isUserCreated = true; // ловим созданного пользователя
    }
}