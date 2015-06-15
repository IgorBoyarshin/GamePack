package gamepack;

import himmel.graphics.layers.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 15-Jun-15.
 */
public class Menu {
    private List<Button> buttons;
    private int currentButton = 0;

    public Menu() {
        buttons = new ArrayList<>();
    }

    public void setCurrent(String name) {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);

            if (button.getName().equals(name)) {
                button.setActive();
                currentButton = i;
                break;
            }
        }
    }

    public String getCurrentButtonName() {
        return buttons.get(currentButton).getName();
    }

    public void submitAll(Layer layer) {
        for (Button button : buttons) {
            button.submitTo(layer);
        }
    }

    public void setCurrent(int currentButton) {
        buttons.get(currentButton).setActive();
        this.currentButton = currentButton;
    }

    public void moveUp() {
        int size = buttons.size();

        buttons.get(currentButton).setIdle();
        if (currentButton == 0) {
            currentButton = size - 1;
        } else {
            currentButton--;
        }
        buttons.get(currentButton).setActive();
    }

    public void moveDown() {
        int size = buttons.size();

        buttons.get(currentButton).setIdle();
        if (currentButton == size - 1) {
            currentButton = 0;
        } else {
            currentButton++;
        }
        buttons.get(currentButton).setActive();
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public int getSize() {
        return buttons.size();
    }

    public String getButtonName(int id) {
        return buttons.get(id).getName();
    }
}
