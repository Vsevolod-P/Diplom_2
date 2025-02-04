package apiHelper;

public class UserLoginApi {
    // Класс для представления данных для логина
    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}