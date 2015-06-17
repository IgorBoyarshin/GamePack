package gamepack.domino;

import gamepack.Game;
import himmel.graphics.Sprite;
import himmel.graphics.Texture;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import java.util.List;

/**
 * Created by Igor on 16-Jun-15.
 */
public class Domino extends Sprite {
    private static Texture dominoes;
    private static float TILE_WIDTH = 2.0f;

    private List<Vector2f> uvUp;
    private static List<Vector2f> uvDown;

    private DIRECTION direction;
    private final int side1;
    private final int side2;

    public Domino(final int side1, final int side2, Vector3f position, DIRECTION direction, List<Vector2f> uv) {
        super(position, new Vector2f(2 * TILE_WIDTH * direction.x, 2 * TILE_WIDTH * direction.y),
                dominoes, Game.spriteRenderer, Game.spriteShader);

        this.direction = direction;
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

    public static void setTileWidth(float newTileWidth) {
        TILE_WIDTH = newTileWidth;
    }

    public void setPosition(int x, int y) {
        this.setNewPosition(new Vector3f(x * TILE_WIDTH, y * TILE_WIDTH, getPosition().z));
    }

    public void moveUp() {
        this.move(new Vector3f(0.0f, TILE_WIDTH, 0.0f));
    }

    public void moveLeft() {
        this.move(new Vector3f(-TILE_WIDTH, 0.0f, 0.0f));
    }

    public void moveDown() {
        this.move(new Vector3f(0.0f, -TILE_WIDTH, 0.0f));
    }

    public void moveRight() {
        this.move(new Vector3f(TILE_WIDTH, 0.0f, 0.0f));
    }

    public void rotateClockWise() {
        direction = DIRECTION.getRirectionByNumber((direction.getNumber() + 1) % 4);
    }

    public void rotateCounterClockWise() {
        direction = DIRECTION.getRirectionByNumber((direction.getNumber() + 3) % 4);
    }

    public static void setDominoesTexture(Texture texture) {
        dominoes = texture;
    }

    public enum DIRECTION {
        UP(1.0f, 2.0f, 0), RIGHT(2.0f, 1.0f, 1), DOWN(1.0f, -2.0f, 2), LEFT(-2.0f, -1.0f, 3);

        private final float x;
        private final float y;
        private final int number;

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public int getNumber() {
            return number;
        }

        static DIRECTION getRirectionByNumber(int number) {
            switch (number) {
                case 0:
                    return UP;
                case 1:
                    return RIGHT;
                case 2:
                    return DOWN;
                case 3:
                    return LEFT;

                default:
                    return null;
            }
        }

        DIRECTION(float x, float y, int number) {
            this.x = x;
            this.y = y;
            this.number = number;
        }
    }
}
