package apiHelper;
import java.util.List;

public class IngredientsResponse {
    private boolean success;
    private String _id;
    private List<Ingredient> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Ingredient> getData() {
        return data;
    }
}
