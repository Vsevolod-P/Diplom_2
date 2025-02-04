package apiHelper;

public class UserCreationApi {
    public static User createUser() {
        // Создаем уникальные данные для курьера
        String email = "testCour" + System.currentTimeMillis() + "@yandex.ru";
        String password = "password123";
        String firstName = "ЛюбительБургеров";

        return new User(email, password, firstName);
    }
}