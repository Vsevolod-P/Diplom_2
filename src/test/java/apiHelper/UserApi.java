package apiHelper;
import com.google.gson.Gson;
import io.restassured.response.Response;

public class UserApi extends BaseHttpClient {

    public static final String API_PATH = "/api/auth/register";
    public static final String API_LOGIN_PATH = "/api/auth/login";
    public static final String API_USER_PATH = "/api/auth/user";
    private static final Gson gson = new Gson();



    public Response createUserRequest(User user) {
        // Отправляем POST запрос для создания пользователя
        return doPostRequest(API_PATH, gson.toJson(user));

    }
    public void deleteUser(String email, String password) {
        // Логин для получения токена пользователя
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest(email, password); //ничего лучше не придумал
        Response response = doPostRequest(API_LOGIN_PATH, gson.toJson(loginRequest));

        // Получаем Токен пользователя из ответа
        String userToken = response.jsonPath().getString("accessToken");

        // Удаляем пользователя
        doDeleteRequest(API_USER_PATH, userToken);
    }

    public Response changeUser(String email, String password, String body){
        // Логин для получения токена пользователя
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest(email, password); //ничего лучше не придумал
        Response response = doPostRequest(API_LOGIN_PATH, gson.toJson(loginRequest));

        // Получаем Токен пользователя из ответа
        String userToken = response.jsonPath().getString("accessToken");

        // Меняем данные пользователя
        return doPatchRequest(API_USER_PATH, userToken, body);
    }

    public Response changeUserNoToken(String body){
        // Меняем данные пользователя
        return doPatchRequest(API_USER_PATH, body);
    }

    public Response changeUserWrongToken(String email, String password, String body){
        // Логин для получения токена пользователя
        UserLoginApi.LoginRequest loginRequest = new UserLoginApi.LoginRequest(email, password); //ничего лучше не придумал
        Response response = doPostRequest(API_LOGIN_PATH, gson.toJson(loginRequest));

        // Получаем Токен пользователя из ответа
        String userToken = response.jsonPath().getString("accessToken");

        // Меняем данные пользователя
        return doPatchRequest(API_USER_PATH, userToken + "wrong", body);
    }
}