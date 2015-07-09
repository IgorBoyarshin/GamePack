package gamepack;

import himmel.graphics.Shader;
import himmel.graphics.Window;
import himmel.graphics.fonts.Font;
import himmel.graphics.fonts.Text;
import himmel.graphics.renderers.FastSpriteRenderer;
import himmel.graphics.renderers.Renderer;
import himmel.math.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

/*

TOP_PRIORITY:
TODO: Himmel: fix textures amount bigger than 16
TODO: Himmel: Text setting the text right in the constructor fails

LOW_PRIORITY:
TODO: Himmel: set proper jar path
TODO: Himmel: fix font size
 */

/**
 * Created by Igor on 14-Jun-15.
 */
public class GamePack {
    private Window window;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final float SCALER = 0.1f;
    private final float WIDTH_COORD = WIDTH * SCALER;
    private final float HEIGHT_COORD = HEIGHT * SCALER;

    private Shader textShader;
    private Shader spriteShader;
    private Renderer spriteRenderer;

    private Game mainMenu;

    public GamePack() {
        window = new Window("GamePack", WIDTH, HEIGHT, Window.ANTI_ALIASING_4X, true, false);

        textShader = new Shader(System.getProperty("user.dir") + "//resources//main//shaders//font.vert",
                System.getProperty("user.dir") + "//resources//main//shaders//font.frag");
        spriteShader = new Shader(System.getProperty("user.dir") + "//resources//main//shaders//fast.vert",
                System.getProperty("user.dir") + "//resources//main//shaders//fast.frag");
        spriteRenderer = new FastSpriteRenderer();

        prepareShaders();

        Game.spriteRenderer = spriteRenderer;
        Game.spriteShader = spriteShader;
        Text.setFont(new Font("resources//main//fonts//FontCalibri"));
        Text.setShader(textShader);

        mainMenu = new MainMenu(WIDTH_COORD, HEIGHT_COORD, window);

        mainLoop();

        destroy();
    }

    private void mainLoop() {
        long nano = System.nanoTime();
        final float ONE_SECOND = 1000000000.0f;

        while (!window.isClosed() && mainMenu.isAlive()) {
            window.clear();

            long newNano = System.nanoTime();
            float delta = (1.0f * newNano / ONE_SECOND - 1.0f * nano / ONE_SECOND);
            nano = newNano;

            mainMenu.update(delta);
            mainMenu.render();

            window.update();
        }
    }

    private void prepareShaders() {
        int values[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        IntBuffer textureIDs = BufferUtils.createIntBuffer(values.length);
        textureIDs.put(values);
        textureIDs.rewind();

        spriteShader.enable();
        spriteShader.setUniform1iv("textures", textureIDs);
        spriteShader.setUniformMat4f("pr_matrix",
                Matrix4f.orthographic(0.0f, WIDTH * SCALER, 0.0f, HEIGHT * SCALER, -1.0f, 1.0f));
        spriteShader.setUniform1f("r", 0.0f);

        textShader.enable();
        textShader.setUniform1iv("textures", textureIDs);
        textShader.setUniformMat4f("pr_matrix",
                Matrix4f.orthographic(0.0f, WIDTH * SCALER, 0.0f, HEIGHT * SCALER, -1.0f, 1.0f));
    }

    private void destroy() {
        window.terminate();
    }

    public static void main(String[] args) {
        new GamePack();
    }
}
