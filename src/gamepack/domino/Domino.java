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
    private Vector2i positionCoord;

    public Domino(final int side1, final int side2, Vector2i position, float zValue, DIRECTION direction, List<Vector2f> uv) {
        super(new Vector3f(position.x * TILE_WIDTH, position.y * TILE_WIDTH, zValue),
                new Vector2f(TILES_PER_SIDE * TILE_WIDTH * 1.0f, TILES_PER_SIDE * TILE_WIDTH * 2.0f),
                dominoes, Game.spriteRenderer, Game.spriteShader);

        this.positionCoord = position;
        this.direction = DIRECTION.UP;
        this.uv = uv;
        uvUp = uv;
        this.side1 = side1;
        this.side2 = side2;

        rotateUv(direction);
        rotatePositionAndSize(direction);

        this.direction = direction;
    }

//    private Vector2f convertToGlobalPosition2f(Vector2i pos) {
//        return new Vector2f(pos.x * TILE_WIDTH, pos.y * TILE_WIDTH);
//    }

    private Vector3f convertToGlobalPosition3f(Vector2i pos) {
        return new Vector3f(pos.x * TILE_WIDTH, pos.y * TILE_WIDTH, getPosition().z);
    }

//    private Vector3f convertToGlobalPosition3f(Vector2i pos, float zValue) {
//        return new Vector3f(pos.x * TILE_WIDTH, pos.y * TILE_WIDTH, zValue);
//    }

    public void setPositionCoord(int x, int y) {
//        positionCoord = new Vector2i(x, y);
        positionCoord.x = x;
        positionCoord.y = y;

        this.setNewPosition(convertToGlobalPosition3f(new Vector2i(x, y)));
    }

    public Vector2i getPositionCoord() {
        return positionCoord;
    }

    public void move(int shiftX, int shiftY) {
        positionCoord.x += shiftX;
        positionCoord.y += shiftY;

        this.setNewPosition(convertToGlobalPosition3f(new Vector2i(positionCoord.x, positionCoord.y)));
    }

    private void rotatePositionAndSize(DIRECTION direction) {
        if (!(this.direction.getX() == direction.getX())) {
            move(direction.getX() - this.direction.getX(),
                    direction.getY() - this.direction.getY());

            Vector2f oldSize = this.getSize();
            this.setSize(new Vector2f(oldSize.y, oldSize.x));
        }
    }

    private void rotateUv(DIRECTION direction) {
        if (this.getUV() != null) {
            int c = (direction.getNumber() - this.direction.getNumber() + 4) % 4;

            for (int i = 0; i < c; i++) {
                rotateUvCoord();
            }
        } else {
            // TODO: get rid of that check
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

    public void moveUp() {
//        positionCoord.y += 1;
//        setPositionCoord(positionCoord.x, positionCoord.y);
        move(0, 1);
    }

    public void moveLeft() {
//        positionCoord.x -= 1;
//        setPositionCoord(positionCoord.x, positionCoord.y);
        move(-1, 0);
    }

    public void moveDown() {
//        positionCoord.y -= 1;
//        setPositionCoord(positionCoord.x, positionCoord.y);
        move(0, -1);
    }

    public void moveRight() {
//        positionCoord.x += 1;
//        setPositionCoord(positionCoord.x, positionCoord.y);
        move(1, 0);
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
        UP(0, 0, 0), RIGHT(-1, 1, 1), DOWN(0, 0, 2), LEFT(-1, 1, 3);

        private final int x;
        private final int y;
        private final int number;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getNumber() {
            return number;
        }

        public static DIRECTION getDirectionByNumber(int number) {
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

        DIRECTION(int x, int y, int number) {
            this.x = x;
            this.y = y;
            this.number = number;
        }
    }
}
