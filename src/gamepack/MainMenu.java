package gamepack;

import gamepack.domino.DominoGame;
import himmel.graphics.Texture;
import himmel.graphics.Window;
import himmel.graphics.layers.Layer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/*

TODO: Improve menuItems

 */

/**
 * Created by Igor on 14-Jun-15.
 */
public class MainMenu extends Game {
    private Game game;
    private Menu menu;

    private Window window;

    private Layer mainLayer;
    private Layer bgLayer;
    private BackgroundAnimation backgroundAnimation;

    private final float BUTTON_WH_RATIO = 8.0f;
    private final float BUTTON_WIDTH;
    private final float BUTTON_HEIGHT;

    private final float MENU_Z = 0.0f;
    private final float BG_Z = -0.5f;

    public MainMenu(final float width, final float height, Window window) {
        super(width, height, window);

        this.window = window;
        BUTTON_WIDTH = WIDTH * 0.6f;
        BUTTON_HEIGHT = BUTTON_WIDTH / BUTTON_WH_RATIO;

        menu = new Menu();

        mainLayer = new Layer();
        bgLayer = new Layer();
        Button.setShader(Game.spriteShader);
        Button.setRenderer(Game.spriteRenderer);

        menu.addButton(new Button(
                "Domino",
                new Vector3f((WIDTH - BUTTON_WIDTH) / 2.0f, HEIGHT - BUTTON_HEIGHT - 5.0f, MENU_Z),
                new Vector2f(BUTTON_WIDTH, BUTTON_HEIGHT),
                new Texture("resources//main//textures//buttonDominoActive.png", Texture.TYPE_RGB),
                new Texture("resources//main//textures//buttonDominoIdle.png", Texture.TYPE_RGB),
                false));
        menu.addButton(new Button(
                "About",
                new Vector3f((WIDTH - BUTTON_WIDTH) / 2.0f, HEIGHT - 2.0f * (BUTTON_HEIGHT + 5.0f), MENU_Z),
                new Vector2f(BUTTON_WIDTH, BUTTON_HEIGHT),
                new Texture("resources//main//textures//buttonAboutActive.png", Texture.TYPE_RGB),
                new Texture("resources//main//textures//buttonAboutIdle.png", Texture.TYPE_RGB),
                false));
        menu.addButton(new Button(
                "Exit",
                new Vector3f((WIDTH - BUTTON_WIDTH) / 2.0f, HEIGHT - 3.0f * (BUTTON_HEIGHT + 5.0f), MENU_Z),
                new Vector2f(BUTTON_WIDTH, BUTTON_HEIGHT),
                new Texture("resources//main//textures//buttonExitActive.png", Texture.TYPE_RGB),
                new Texture("resources//main//textures//buttonExitIdle.png", Texture.TYPE_RGB),
                false));

        menu.setCurrent("Domino");
        menu.submitAll(mainLayer);

        backgroundAnimation = new BackgroundAnimation(WIDTH, HEIGHT, bgLayer, BG_Z);

        game = new DominoGame(WIDTH, HEIGHT, window);
    }

    private void keyboard() {
        if (System.currentTimeMillis() - lastKeyboard > keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_UP)) {
                lastKeyboard = System.currentTimeMillis();

                menu.moveUp();
            }

            if (window.isKeyDown(GLFW_KEY_DOWN)) {
                lastKeyboard = System.currentTimeMillis();

                menu.moveDown();
            }

            if (window.isKeyDown(GLFW_KEY_ENTER)) {
                lastKeyboard = System.currentTimeMillis();

                switch (menu.getCurrentButtonName()) {
                    case "Domino":
                        game = new DominoGame(WIDTH, HEIGHT, window);
                        break;
                    case "About":
                        System.out.println("'About' is not implemented yet");
                        break;
                    case "Exit":
                        alive = false;
                        break;
                }
            }
        }

//        if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
//            alive = false;
//        }
    }

    @Override
    public void update(float delta) {
        if (game == null) {
            backgroundAnimation.update(delta);

            keyboard();
        } else {
            if (game.isAlive()) {
                game.update(delta);
            } else {
                game = null;
            }
        }
    }

    @Override
    public void render() {
        if (game == null) {
            bgLayer.render();
            mainLayer.render();
        } else {
            game.render();
        }
    }
}
