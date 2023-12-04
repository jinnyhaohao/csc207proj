package use_case.add_fooditem;
import interface_adapter.add_fooditem.AddFoodItemState;
import interface_adapter.add_fooditem.AddFoodItemViewModel;
import entities.FoodItem;
import entities.User;
import entities.UserFactory;
import entities.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddFoodItemInteractorInvalidDateTest {
    private static class TestAddFoodItemDataAccessInterface implements AddFoodItemDataAccessInterface {
        private User user;
        public TestAddFoodItemDataAccessInterface(User user) {
            this.user = user;
        }
        public Inventory getUserInventory() {
            return user.getInventory();
        }
        @Override
        public void addItem(FoodItem foodItem) {
        }
    }
    private AddFoodItemDataAccessInterface addFoodItemDataAccessInterface;
    private AddFoodItemInteractor addFoodItemInteractor;
    private AddFoodItemOutputBoundary addFoodItemOutputBoundary;
    private AddFoodItemViewModel addFoodItemViewModel;

    @BeforeEach
    public void setUp() {
        UserFactory userFactory = new UserFactory();
        addFoodItemViewModel = new AddFoodItemViewModel();
        addFoodItemDataAccessInterface = new TestAddFoodItemDataAccessInterface(userFactory.create(new HashMap<>()));
        addFoodItemOutputBoundary = new AddFoodItemOutputBoundary() {
            @Override
            public void prepareSuccessView() {
            }

            @Override
            public void prepareFailView(String error) {
                AddFoodItemState addFoodItemState = addFoodItemViewModel.getState();
                if (error.equals("Ingredient is invalid!")) {
                    addFoodItemState.setIngredientError(error);
                } else if (error.equals("Invalid Date!")) {
                    addFoodItemState.setDateError(error);
                } else {
                    addFoodItemState.setAmountError(error);
                }
            }};
        addFoodItemInteractor = new AddFoodItemInteractor(addFoodItemOutputBoundary, addFoodItemDataAccessInterface);
    }
    @Test
    public void execute_shouldReturnError_whenDateIsExpired() {
        AddFoodItemInputData addFoodItemInputData = new AddFoodItemInputData("Banana", "2023", "1", "1", "1");
        addFoodItemInteractor.execute(addFoodItemInputData);
        assertEquals("Invalid Date!", addFoodItemViewModel.getState().getDateError());
    }

    @Test
    public void execute_shouldReturnTrue_whenDateIsLeapYear() {
        AddFoodItemInputData addFoodItemInputData = new AddFoodItemInputData("Banana", "2024", "2", "29", "1");
        addFoodItemInteractor.execute(addFoodItemInputData);
        assertEquals(null, addFoodItemViewModel.getState().getDateError());
    }

    @Test
    public void execute_shouldReturnFalse_WhenDateIsNotNumber() {
        AddFoodItemInputData addFoodItemInputData = new AddFoodItemInputData("Banana", "2024", "a", "29", "1");
        addFoodItemInteractor.execute(addFoodItemInputData);
        assertEquals("Invalid Date!", addFoodItemViewModel.getState().getDateError());
    }
}

