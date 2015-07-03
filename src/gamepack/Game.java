package gamepack;

import himmel.graphics.Shader;
import himmel.graphics.Window;
import himmel.graphics.renderers.Renderer;

/**
 * Created by Igor on 14-Jun-15.
 */
public abstract class Game {
//    public static Shader textShader;
    public static Shader spriteShader;
    public static Renderer spriteRenderer;

    protected Window window;
    protected final float WIDTH;
    protected final float HEIGHT;

    protected boolean alive = true;

    public static final long keyboardMillisDelay = 130;
//    public static final long keyboardMillisDelay = 180;
    public static long lastKeyboard = 0;

    public Game(float width, float height, Window window) {
        WIDTH = width;
        HEIGHT = height;
        this.window = window;
    }

    public boolean isAlive() {
        return alive;
    }

    public abstract void update(float delta);
    public abstract void render();
}
