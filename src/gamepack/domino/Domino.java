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
public class Domino extends Sprite{
    private static Texture dominoes;
    private static final float DOMINO_WIDTH = 4.0f;
    private static final float DOMINO_HEIGHT = 8.0f;
    private static float tileSize = 1.0f;

    private List<Vector2f> uvUp;
    private static List<Vector2f> uvDown;

    private final int side1;
    private final int side2;

    public Domino(final int side1, final int side2, Vector3f position, Vector2f size, List<Vector2f> uv) {
        super(position, size, dominoes, Game.spriteRenderer, Game.spriteShader);
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

    public static void setTileSize(float newTileSize) {
        tileSize = newTileSize;
    }

    public void rotateClockWise() {

    }

    public void rotateCounterClockWise() {

    }

    public static void setDominoesTexture(Texture texture) {
        dominoes = texture;
    }
}
