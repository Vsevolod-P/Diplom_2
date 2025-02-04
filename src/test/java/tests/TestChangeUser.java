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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestChangeUser {

    private User user;
    private apiHelper.UserApi UserApi;
    private boolean isUserCreated;

    @Parameterized.Parameter
    public String body;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"{\"password\": \"password123\"}"}, //не придумал как это сериализировать и нужно ли это тут
                {"{\"name\": \"ШаурмаЛучше\"}"},
                {"{\"email\": \"betterMail@yahoo.ru\"}"},
        });
    }

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
    @Description("Проверка ручки изменения пользователя")
    @Step("Создание пользователя и изменение данных")
    public void changeUserParameters() {
        //создаем пользователя
        Response response = UserApi.createUserRequest(user);
        response.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        // Меняем данные пользователя
        Response responseChange = UserApi.changeUser(user.getEmail(),user.getPassword(),body);
        responseChange.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        isUserCreated = true;
    }

    @Test
    @Description("Проверка ручки изменения пользователя без токена")
    @Step("Изменение данных, для не существующего пользователя")
    public void changeUserParametersWithOutToken() {
        //Меняем данные пользователя без токена
        Response responseChange = UserApi.changeUserNoToken("{\"name\": \"Sakura\"}");
        responseChange.then()
                .statusCode(403) // Проверяем, что статус ответа 401
                .body("message", equalTo("You should be authorised1"));
    }

}
