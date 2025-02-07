package tests;

import apiHelper.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class TestOrderCreate {

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
    @Description("Проверка ручки создания заказа авторизованным пользователем")
    @Step("Создание заказа авторизованным пользователем")
    public void createOrderAuthorization() {
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

        Response response = orderApi.createOrderAuthorization(userToken, requestBody);
        response.then()
                .statusCode(200) // Ожидаем код 200
                .body("success", equalTo(true));
        isUserCreated = true;
    }

    @Test
    @Description("Проверка ручки создания заказа без авторизации пользователем")
    @Step("Создание заказа без авторизации пользователем")
    public void createOrderWithoutAuthorization() {
        String[] ingredients = {validIngredientId1, validIngredientId2};
        OrderRequestBodyBuilder requestBodyBuilder = new OrderRequestBodyBuilder(ingredients);
        String requestBody = requestBodyBuilder.buildRequestBody();

        Response response = orderApi.createOrderWithoutAuthorization(requestBody);
        response.then()
                .statusCode(200) // Ожидаем код 200
                .body("success", equalTo(true));
    }

    @Test
    @Description("Проверка ручки создания заказа без ингредиентов")
    @Step("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredient() {
        Response response = orderApi.createOrderWithoutAuthorization("{\"ingredients\": []}");
        response.then()
                .statusCode(400) // Ожидаем код 400
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Проверка ручки создания заказа без авторизации пользователем")
    @Step("Создание заказа без авторизации пользователем")
    public void createOrderWrongIngredient() {
        String[] ingredients = {validIngredientId1 +"1", validIngredientId2};
        OrderRequestBodyBuilder requestBodyBuilder = new OrderRequestBodyBuilder(ingredients);
        String requestBody = requestBodyBuilder.buildRequestBody();

        Response response = orderApi.createOrderWithoutAuthorization(requestBody);
        response.then()
                .statusCode(500) // Ожидаем код 500
                .body(containsString("Internal Server Error"));
    }
}
