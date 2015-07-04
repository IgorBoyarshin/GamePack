package gamepack.domino;

import himmel.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 18-Jun-15.
 */
public class Table {
    private Vector2i tailPos;
    private Vector2i headPos;
    private int tailNumber;
    private int headNumber;
    private Domino.DIRECTION headDirection;
    private Domino.DIRECTION tailDirection;
    private List<Domino> dominoes;

    private List<Domino> pool;

    private int amount;

    private final int fieldMargin = 30;
    private Vector2i currentShift = new Vector2i(fieldMargin, fieldMargin);
    private final int fieldSize;
    private boolean field[][];

    private Vector2i poolStart;
    private Vector2i poolFinish;

    private final float fieldZ;

    public Table(int fieldSize, float fieldZ) {
        this.fieldSize = fieldSize;
        this.fieldZ = fieldZ;

        dominoes = new ArrayList<>();

        field = new boolean[fieldSize + 2 * fieldMargin][fieldSize + 2 * fieldMargin];
    }

    public List<Domino> getPool() {
        return pool;
    }

    public void setPool(List<Domino> pool) {
        this.pool = pool;
    }

    public Domino takeDominoFromPool() {
        if (pool.size() > 0) {
            Domino d = pool.remove(0);
            repositionPool();
            return d;
        }

        return null;
    }

    private void repositionPool() {
        repositionPool(poolStart, poolFinish);
    }

    public void repositionPool(Vector2i start, Vector2i finish) {
        poolStart = start;
        poolFinish = finish;

        int counter = 0;

        int rows = (start.y - finish.y) / 5;
        int columns = (finish.x - start.x) / 3;
        while (rows * (columns - 1) >= pool.size()) {
            columns--;
            start.x += 3;
        }

        for (Domino domino : pool) {
            int x = (counter - counter % rows) / rows;
            int y = counter % rows;

            domino.setPositionCoord(start.x + x * 3, start.y - 4 - y * 5);

            counter++;
        }
    }

    public Domino.DIRECTION getHeadDirection() {
        return headDirection;
    }

    public Domino.DIRECTION getTailDirection() {
        return tailDirection;
    }

    public Vector2i getHeadPos() {
        return new Vector2i(headPos.x - currentShift.x, headPos.y - currentShift.y);
    }

    public Vector2i getTailPos() {
        return new Vector2i(tailPos.x - currentShift.x, tailPos.y - currentShift.y);
    }

    public int getTailNumber() {
        return tailNumber;
    }

    public int getHeadNumber() {
        return headNumber;
    }

    public void placeDomino(Domino domino) {
        if (true) {
            int side1 = domino.getSide1();
            int side2 = domino.getSide2();

//            int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
//            int posY = (int) (domino.getPosition().y / Domino.getTileWidth());
//            System.out.println("Placing: " + posX + " " + posY);

            if (amount == 0) {
                headNumber = side1;
                tailNumber = side2;

//                headDirection = domino.getDirection();
//                tailDirection = headDirection;

                if (side1 == side2) {
                    headPos = getCenterPos(domino);
                    tailPos = getCenterPos(domino);

                    headDirection = Domino.DIRECTION.getDirectionByNumber((domino.getDirection().getNumber() + 1) % 4);
                } else {
                    headPos = getHeadPos(domino);
                    tailPos = getTailPos(domino);

                    headDirection = domino.getDirection();
                }

                tailDirection = headDirection;

            } else {
                if (isHead(domino)) {
                    if (amount > 1) {
                        markTrueX(headPos);
                    }

                    if (domino.getSide1() == domino.getSide2()) {
                        headPos = getCenterPos(domino);

                        headDirection = Domino.DIRECTION.getDirectionByNumber((domino.getDirection().getNumber() + 1) % 4);
                    } else {
                        if (domino.getSide1() == headNumber) {
                            headNumber = domino.getSide2();
                            headPos = getTailPos(domino);
                            markTrueX(getHeadPos(domino));
                        } else {
                            headNumber = domino.getSide1();
                            headPos = getHeadPos(domino);
                            markTrueX(getTailPos(domino));
                        }

                        headDirection = domino.getDirection();
                    }

                } else {
                    if (amount > 1) {
                        markTrueX(tailPos);
                    }

                    if (domino.getSide1() == domino.getSide2()) {
                        tailPos = getCenterPos(domino);

                        tailDirection = Domino.DIRECTION.getDirectionByNumber((domino.getDirection().getNumber() + 1) % 4);
                    } else {
                        if (domino.getSide1() == tailNumber) {
                            tailNumber = domino.getSide2();
                            tailPos = getTailPos(domino);
                            markTrueX(getHeadPos(domino));
                        } else {
                            tailNumber = domino.getSide1();
                            tailPos = getHeadPos(domino);
                            markTrueX(getTailPos(domino));
                        }

                        tailDirection = domino.getDirection();
                    }
                }
            }

            Vector2i pos1 = getHeadPos(domino);
            Vector2i pos2 = getTailPos(domino);

            field[pos1.x - 1][pos1.y - 1] = true;
            field[pos1.x - 1][pos1.y] = true;
            field[pos1.x][pos1.y - 1] = true;
            field[pos1.x][pos1.y] = true;

            field[pos2.x - 1][pos2.y - 1] = true;
            field[pos2.x - 1][pos2.y] = true;
            field[pos2.x][pos2.y - 1] = true;
            field[pos2.x][pos2.y] = true;

            amount++;

            domino.move(new Vector3f(0.0f, 0.0f, fieldZ + 0.05f));
            dominoes.add(domino);

//            System.out.println("Current table status:");
//            System.out.println("Head pos: " + headPos.x + ";" + headPos.y + " = " + headNumber);
//            System.out.println("Tail pos: " + tailPos.x + ";" + tailPos.y + " = " + tailNumber);
//            System.out.println();

//            System.out.println("Pool left: " + pool.size());
//            System.out.println();
        }
    }

    public void shift(Vector2i vector) {
        currentShift.x += vector.x;
        currentShift.y += vector.y;

        if (currentShift.x < 0) {
            vector.x = currentShift.x - vector.x;
            currentShift.x = 0;
        }
        if (currentShift.y < 0) {
            vector.y = currentShift.y - vector.y;
            currentShift.y = 0;
        }
        if (currentShift.x >= 2 * fieldMargin) {
            vector.x = 2 * fieldMargin - (currentShift.x - vector.x) - 1;
            currentShift.x = 2 * fieldMargin - 1;
        }
        if (currentShift.y >= 2 * fieldMargin) {
            vector.y = 2 * fieldMargin - (currentShift.y - vector.y) - 1;
            currentShift.y = 2 * fieldMargin - 1;
        }

//        System.out.println("CuR: " + currentShift.x + ";" + currentShift.y);

        shiftDominoes(vector);
    }

    private void shiftDominoes(Vector2i vector) {
        for (Domino domino : dominoes) {
            domino.move(-vector.x, -vector.y);
        }
    }

    private void markTrueX(Vector2i point) {
        for (int i = point.x - 3; i < point.x + 3; i++) {
            for (int j = point.y - 1; j < point.y + 1; j++) {
                field[i][j] = true;
            }
        }

        for (int i = point.x - 1; i < point.x + 1; i++) {
            for (int j = point.y - 3; j < point.y + 3; j++) {
                field[i][j] = true;
            }
        }
    }

    private Vector2i getCenterPos(Domino domino) {
//        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
//        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

        int posX = domino.getPositionCoord().x + currentShift.x;
        int posY = domino.getPositionCoord().y + currentShift.y;

        switch (domino.getDirection()) {
            case UP:
                return new Vector2i(posX + 1, posY + 2);
            case RIGHT:
                return new Vector2i(posX + 2, posY + 1);
            case DOWN:
                return new Vector2i(posX + 1, posY + 2);
            case LEFT:
                return new Vector2i(posX + 2, posY + 1);
            default:
                return null;
        }
    }

    private Vector2i getHeadPos(Domino domino) {
//        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
//        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

        int posX = domino.getPositionCoord().x + currentShift.x;
        int posY = domino.getPositionCoord().y + currentShift.y;

        switch (domino.getDirection()) {
            case UP:
                return new Vector2i(posX + 1, posY + 3);
            case RIGHT:
                return new Vector2i(posX + 3, posY + 1);
            case DOWN:
                return new Vector2i(posX + 1, posY + 1);
            case LEFT:
                return new Vector2i(posX + 1, posY + 1);
            default:
                return null;
        }
    }

    private Vector2i getTailPos(Domino domino) {
//        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
//        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

        int posX = domino.getPositionCoord().x + currentShift.x;
        int posY = domino.getPositionCoord().y + currentShift.y;

        switch (domino.getDirection()) {
            case UP:
                return new Vector2i(posX + 1, posY + 1);
            case RIGHT:
                return new Vector2i(posX + 1, posY + 1);
            case DOWN:
                return new Vector2i(posX + 1, posY + 3);
            case LEFT:
                return new Vector2i(posX + 3, posY + 1);
            default:
                return null;
        }
    }

    private boolean isHead(Domino domino) {
//        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
//        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

        int posX = domino.getPositionCoord().x + currentShift.x;
        int posY = domino.getPositionCoord().y + currentShift.y;

        int centerX = 0;
        int centerY = 0;

        // Determine where the center of the new domino is
        switch (domino.getDirection()) {
            case UP: {
                centerX = posX + 1;
                centerY = posY + 2;
                break;
            }
            case RIGHT: {
                centerX = posX + 2;
                centerY = posY + 1;
                break;
            }
            case DOWN: {
                centerX = posX + 1;
                centerY = posY + 2;
                break;
            }
            case LEFT: {
                centerX = posX + 2;
                centerY = posY + 1;
                break;
            }
        }

        if (distance(headPos.x, headPos.y, centerX, centerY) < distance(tailPos.x, tailPos.y, centerX, centerY)) {
            return true;
        }

        return false;
    }

    public int getPoolSize() {
        return pool.size();
    }

    public int getAmount() {
        return amount;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean isPositionValid(Vector2i pos1, Vector2i pos2) {
        Vector2i p1 = new Vector2i(pos1.x + currentShift.x, pos1.y + currentShift.y);
        Vector2i p2 = new Vector2i(pos2.x + currentShift.y, pos2.y + currentShift.y);

        if (field[p1.x - 1][p1.y - 1]) {
            return false;
        }
        if (field[p1.x][p1.y - 1]) {
            return false;
        }
        if (field[p1.x - 1][p1.y]) {
            return false;
        }
        if (field[p1.x][p1.y]) {
            return false;
        }

        if (field[p2.x - 1][p2.y - 1]) {
            return false;
        }
        if (field[p2.x][p2.y - 1]) {
            return false;
        }
        if (field[p2.x - 1][p2.y]) {
            return false;
        }
        if (field[p2.x][p2.y]) {
            return false;
        }

        return true;
    }

    public boolean isPositionValid(Domino domino) {
        Vector2i pos1 = getHeadPos(domino);
        Vector2i pos2 = getTailPos(domino);

        if (field[pos1.x - 1][pos1.y - 1]) {
            return false;
        }
        if (field[pos1.x][pos1.y - 1]) {
            return false;
        }
        if (field[pos1.x - 1][pos1.y]) {
            return false;
        }
        if (field[pos1.x][pos1.y]) {
            return false;
        }

        if (field[pos2.x - 1][pos2.y - 1]) {
            return false;
        }
        if (field[pos2.x][pos2.y - 1]) {
            return false;
        }
        if (field[pos2.x - 1][pos2.y]) {
            return false;
        }
        if (field[pos2.x][pos2.y]) {
            return false;
        }

        return true;
    }
}