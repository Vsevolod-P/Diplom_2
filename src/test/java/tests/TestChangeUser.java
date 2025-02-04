package tests;

import apiHelper.User;
import apiHelper.UserApi;
import apiHelper.UserCreationApi;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class TestChangeUser {

    private User user;
    private apiHelper.UserApi UserApi;
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
    @Description("Проверка ручки изменения пользователя без токена")
    @Step("Изменение данных, при отсутствующем методе авторизации в запросе")
    public void changeUserParametersWithOutToken() {
        //Меняем данные пользователя без токена
        Response responseChange = UserApi.changeUserNoToken("{\"name\": \"Sakura\"}");
        responseChange.then()
                .statusCode(401) // Проверяем, что статус ответа 401
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @Description("Проверка ручки изменения пользователя")
    @Step("Создание пользователя и изменение данных")
    public void changeUserParametersWrongToken() {
        //создаем пользователя
        Response response = UserApi.createUserRequest(user);
        response.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        // Меняем данные пользователя
        Response responseChange = UserApi.changeUserWrongToken(user.getEmail(), user.getPassword(), "{\"name\": \"Sakura\"}");
        responseChange.then()
                .statusCode(403) // Проверяем, что статус ответа 403
                .body("message", equalTo("invalid signature"));
        isUserCreated = true;
    }
}
