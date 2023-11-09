package entities.get_recipe;
import java.util.*;

public interface UserInventory {

    void addItem(FoodItem item);

    FoodItem removeItem();

    boolean removeSpecificItem(FoodItem item);
}
