package gamepack.domino;

import gamepack.Game;
import himmel.graphics.Shader;
import himmel.graphics.Sprite;
import himmel.graphics.Texture;
import himmel.graphics.layers.Layer;
import himmel.graphics.renderers.Renderer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import java.util.List;

/**
 * Created by Igor on 16-Jun-15.
 */
public class Domino extends Sprite {
    private static Texture dominoes;
    private static float DOMINO_WIDTH = 4.0f;

    private List<Vector2f> uvUp;
    private static List<Vector2f> uvDown;

    private final int side1;
    private final int side2;

    public Domino(final int side1, final int side2, Vector3f position, DIRECTION direction, List<Vector2f> uv) {
        super(position, new Vector2f(DOMINO_WIDTH * direction.x, DOMINO_WIDTH * direction.y),
                dominoes, Game.spriteRenderer, Game.spriteShader);

        this.uv = uv;
        uvUp = uv;
        this.side1 = side1;
        this.side2 = side2;
    }

    public static void setUvDown(List<Vector2f> theUvDown) {
        uvDown = theUvDown;
    }

    public void flipUp() {
        uv = uvUp;
    }

    public void flipDown() {
        uv = uvDown;
    }

    public int getSide1() {
        return side1;
    }

    public int getSide2() {
        return side2;
    }

    public static void setDominoWidth(float newDominoWidth) {
        DOMINO_WIDTH = newDominoWidth;
    }

    public void rotateClockWise() {

    }

    public void rotateCounterClockWise() {

    }

    public static void setDominoesTexture(Texture texture) {
        dominoes = texture;
    }

    public enum DIRECTION {
        UP(1.0f, 2.0f), RIGHT(2.0f, 1.0f), DOWN(1.0f, -2.0f), LEFT(-2.0f, -1.0f);

        float x;
        float y;

        DIRECTION(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
