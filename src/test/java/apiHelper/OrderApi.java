package apiHelper;

import io.restassured.response.Response;

public class OrderApi extends BaseHttpClient {
    static final String API_ORDER_PATH = "/api/orders";
    static final String API_GET_ALL_ORDER_PATH = "/api/orders/all";

    public Response createOrderWithoutAuthorization(String requestBody) {
        return doPostRequest(API_ORDER_PATH, requestBody);
    }

    public Response createOrderAuthorization(String userToken, String requestBody) {
        return doPostRequest(API_ORDER_PATH, userToken, requestBody);
    }

    public Response takeAllOrdersWithoutAuthorization() {
        return doGetRequest(API_GET_ALL_ORDER_PATH);
    }

    public Response takeOrdersAuthorization(String userToken) {
        return doGetRequest(API_ORDER_PATH, userToken);
    }


}