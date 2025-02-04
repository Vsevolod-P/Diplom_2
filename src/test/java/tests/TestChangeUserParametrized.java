package tests;

import apiHelper.User;
import apiHelper.UserApi;
import apiHelper.UserCreationApi;
import com.google.gson.Gson;
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
public class TestChangeUserParametrized {

    private User user;
    private apiHelper.UserApi UserApi;
    private boolean isUserCreated;

    @Parameterized.Parameter
    public String body;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"{\"password\": \"DoomDays123\"}"},
                {"{\"name\": \"ШаурмаЛучше\"}"},
                {"{\"email\": \"Qwerty@yah.ru\"}"},
        });
    }

    @Before
    public void setUp() {
        // Создаем пользователя перед каждым тестом
        UserApi = new UserApi();
        user = UserCreationApi.createUser();
        isUserCreated = false;
        updateUser(body);
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (isUserCreated) {
            UserApi.deleteUser(user.getEmail(), user.getPassword());
        }
    }

    //Необходимо обновлять данный почты и пароля для возможности удаления пользователя
    private void updateUser(String json) {
        Gson gson = new Gson();
        User updatedUser = gson.fromJson(json, User.class);
        // Обновляем поля пользователя
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
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
        Response responseChange = UserApi.changeUser(user.getEmail(), user.getPassword(), body);
        responseChange.then()
                .statusCode(200) // Проверяем, что статус ответа 200
                .body("success", equalTo(true));
        isUserCreated = true;
    }
}
