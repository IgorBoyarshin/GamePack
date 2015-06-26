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
    public static final int TILES_PER_SIDE = 2;

    //    private boolean flippedDown = false;
    private List<Vector2f> uvUp;
    private static List<Vector2f> uvDown;
    private static List<Vector2f> uvMaskGreen;
    private static List<Vector2f> uvMaskRed;
    private static List<Vector2f> uvMaskSelected;
    private static List<Vector2f> uvMaskNull;

    private DIRECTION direction;
    private final int side1;
    private final int side2;

    public Domino(final int side1, final int side2, Vector3f position, DIRECTION direction, List<Vector2f> uv) {
        super(position, new Vector2f(TILES_PER_SIDE * TILE_WIDTH * 1.0f, TILES_PER_SIDE * TILE_WIDTH * 2.0f),
                dominoes, Game.spriteRenderer, Game.spriteShader);

        this.direction = DIRECTION.UP;
        this.uv = uv;
        uvUp = uv;
        this.side1 = side1;
        this.side2 = side2;

        rotateUv(direction);
        rotatePositionAndSize(direction);

        this.direction = direction;
    }

    private void rotatePositionAndSize(DIRECTION direction) {
        if (!(this.direction.getX() == direction.getX())) {
            this.move(new Vector3f(
                    (direction.getX() - this.direction.getX()) * TILE_WIDTH,
                    (direction.getY() - this.direction.getY()) * TILE_WIDTH,
                    0.0f));

            Vector2f oldSize = this.getSize();

            this.setSize(new Vector2f(
                    oldSize.x - (direction.getX() - this.direction.getX()) * 2.0f * TILE_WIDTH,
                    oldSize.y - (direction.getY() - this.direction.getY()) * 2.0f * TILE_WIDTH));
        }
    }

    private void rotateUv(DIRECTION direction) {
        if (this.getUV() != null) {
            int c = (direction.getNumber() - this.direction.getNumber() + 4) % 4;

            for (int i = 0; i < c; i++) {
                rotateUvCoord();
            }
        } else {
            // TODO: get rif of that check
        }
    }

    private void rotateUvCoord() {
        // Permutation
        List<Vector2f> list = this.getUV();

        Vector2f end = list.remove(list.size() - 1);
        list.add(0, end);
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction, boolean setNew) {
        rotatePositionAndSize(direction);
        rotateUv(direction);

        if (setNew) {
            this.direction = direction;
        }
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
        setDirection(DIRECTION.getDirectionByNumber((direction.getNumber() + 1) % 4), true);
    }

    public void uvMaskNull() {
        uv = uvMaskNull;
    }

    public void uvMaskGreen() {
        uv = uvMaskGreen;
    }

    public void uvMaskRed() {
        uv = uvMaskRed;
    }

    public void uvMaskSelected() {
        uv = uvMaskSelected;
    }

    public static void setUvs(List<Vector2f> uvD, List<Vector2f> uvGreen,
                              List<Vector2f> uvRed, List<Vector2f> uvSelected, List<Vector2f> uvNull) {

        uvDown = uvD;
        uvMaskRed = uvRed;
        uvMaskGreen = uvGreen;
        uvMaskSelected = uvSelected;
        uvMaskNull = uvNull;
    }

    public static void setDominoesTexture(Texture texture) {
        dominoes = texture;
    }

    public static void setTileWidth(float newTileWidth) {
        TILE_WIDTH = newTileWidth;
    }

    public static float getTileWidth() {
        return TILE_WIDTH;
    }

    public enum DIRECTION {
        UP(0.0f, 0.0f, 0), RIGHT(-1.0f, 1.0f, 1), DOWN(0.0f, 0.0f, 2), LEFT(-1.0f, 1.0f, 3);

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

        static DIRECTION getDirectionByNumber(int number) {
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
