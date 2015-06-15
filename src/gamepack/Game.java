package gamepack;

import himmel.graphics.Shader;
import himmel.graphics.Window;
import himmel.graphics.renderers.FastSpriteRenderer;
import himmel.graphics.renderers.Renderer;

/**
 * Created by Igor on 14-Jun-15.
 */
public abstract class Game {
//    public static Shader textShader;
    public static Shader spriteShader;
    public static Renderer spriteRenderer;

    private Window window;

    protected boolean alive = true;

    protected final long keyboardMillisDelay = 180;
    protected long lastKeyboard = 0;

    public Game(Window window) {
        this.window = window;
    }

    public boolean isAlive() {
        return alive;
    }

    public abstract void update(float delta);
    public abstract void render();
}
