package apiHelper;

import com.google.gson.Gson;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class IngredientApi extends BaseHttpClient {
    public static final String API_PATH = "/api/ingredients";
    private List<Ingredient> ingredients;
    Gson gson = new Gson();

    public void IngredientService() {
        Type responseType = new TypeToken<IngredientsResponse>(){}.getType();
        IngredientsResponse ingredientsResponse = gson.fromJson(doGetRequestString(API_PATH), responseType);
        this.ingredients = ingredientsResponse.getData();
    }

    public String getIdByName(String name) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equalsIgnoreCase(name)) {
                return ingredient.getId();
            }
        }
        return null;
    }
}
