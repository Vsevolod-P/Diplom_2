package tests;

import apiHelper.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestOrderList {

    private IngredientApi ingredientApi;
    private OrderApi orderApi;
    private String validIngredientId1;
    private String validIngredientId2;
    private User user;
    private apiHelper.UserApi UserApi;
    private boolean isUserCreated;

    @Before
    public void setUp() {
        ingredientApi = new IngredientApi();
        orderApi = new OrderApi();
        // Создаем пользователя перед каждым тестом
        UserApi = new UserApi();
        user = UserCreationApi.createUser();
        isUserCreated = false;
        // Получаем ингредиенты
        ingredientApi.IngredientService();
        validIngredientId1 = ingredientApi.getIdByName("Флюоресцентная булка R2-D3");
        validIngredientId2 = ingredientApi.getIdByName("Мясо бессмертных моллюсков Protostomia");
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (isUserCreated) {
            UserApi.deleteUser(user.getEmail(), user.getPassword());
        }
    }

    @Test
    @Description("Проверка ручки создания получения заказа авторизованным пользователем")
    @Step("Создание заказа авторизованным пользователем и получение списка заказов для пользователя")
    public void checkOrderForUser() {
        // Создаем объект для логина
        UserApi.createUserRequest(user); //создаем и логинимся курьером только в этом тесте
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest(user.getEmail(), user.getPassword());
        Response responseForToken = UserApi.doPostRequest(UserApi.API_LOGIN_PATH, loginRequest);
        // Получаем Токен
        String userToken = responseForToken.jsonPath().getString("accessToken");
        // Создаем тело заказа
        String[] ingredients = {validIngredientId1, validIngredientId2};
        OrderRequestBodyBuilder requestBodyBuilder = new OrderRequestBodyBuilder(ingredients);
        String requestBody = requestBodyBuilder.buildRequestBody();
        // Создаем заказ
        Response response = orderApi.createOrderAuthorization(userToken, requestBody);
        response.then()
                .statusCode(200) // Ожидаем код 200
                .body("success", equalTo(true));
        //Проверяем заказ у пользователя
        Response responseOrder = orderApi.takeOrdersAuthorization(userToken);
        responseOrder.then()
                .statusCode(200) // Ожидаем код 200
                .body("orders", notNullValue());
        isUserCreated = true;
    }

    @Test
    @Description("Проверка ручки получения заказов без авторизации")
    @Step("Проверяем ручку")
    public void checkOrders() {
        Response response = orderApi.takeAllOrdersWithoutAuthorization();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
