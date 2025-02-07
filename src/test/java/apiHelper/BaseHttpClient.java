package apiHelper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public abstract class BaseHttpClient {


    private RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL.BASE_URL)
            .addHeader("Content-Type", "application/json")
            .setRelaxedHTTPSValidation()
            .build();


    public Response doPatchRequest(String path, Object token, Object body) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .body(body)
                .patch(path)
                .thenReturn();
    }

    public Response doPatchRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .patch(path)
                .thenReturn();
    }

    public Response doPostRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .thenReturn();
    }

    public Response doPostRequest(String path, Object token, Object body) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .body(body)
                .post(path)
                .thenReturn();
    }

    public Response doDeleteRequest(String path, Object token) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .delete(path)
                .thenReturn();
    }
     public String doGetRequestString(String path) {
         return given()
                 .spec(baseRequestSpec)
                 .get(path)
                 .thenReturn()
                 .getBody()
                 .asString();
     }
    public Response doGetRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .get(path)
                .thenReturn();
    }
    public Response doGetRequest(String path, Object token) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .get(path)
                .thenReturn();
    }

}