package apiHelper;

public class OrderRequestBodyBuilder {
    private String[] ingredients;

    public OrderRequestBodyBuilder(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String buildRequestBody() {
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("{\"ingredients\": [");

        for (int i = 0; i < ingredients.length; i++) {
            requestBody.append("\"").append(ingredients[i]).append("\"");
            if (i < ingredients.length - 1) {
                requestBody.append(",");
            }
        }
        requestBody.append("]}");
        return requestBody.toString();
    }
}
