package gamepack.domino;

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

    private List<Domino> pool;

    private int amount;

    private final int fieldSize;
    private boolean field[][];

    public Table(int fieldSize) {
        this.fieldSize = fieldSize;

        field = new boolean[fieldSize][fieldSize];
    }

    public List<Domino> getPool() {
        return pool;
    }

    public void setPool(List<Domino> pool) {
        this.pool = pool;
    }

    public Domino takeDominoFromPool() {
        if (pool.size() > 0) {
            return pool.remove(0);
        }

        return null;
    }

    public Domino.DIRECTION getHeadDirection() {
        return headDirection;
    }

    public Domino.DIRECTION getTailDirection() {
        return tailDirection;
    }

    public Vector2i getHeadPos() {
        return headPos;
    }

    public Vector2i getTailPos() {
        return tailPos;
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

                headDirection = domino.getDirection();
                tailDirection = headDirection;

                if (side1 == side2) {
                    headPos = getCenterPos(domino);
                    tailPos = getCenterPos(domino);
                } else {
                    headPos = getHeadPos(domino);
                    tailPos = getTailPos(domino);
                }

//                switch (domino.getDirection()) {
//                    case UP: {
//                        if (side1 == side2) {
//                            headPos = new Vector2i(posX + 1, posY + 2);
//                            tailPos = new Vector2i(posX + 1, posY + 2);
//                        } else {
//                            headPos = new Vector2i(posX + 1, posY + 3);
//                            tailPos = new Vector2i(posX + 1, posY + 1);
//                        }
//                        break;
//                    }
//                    case RIGHT: {
//                        if (side1 == side2) {
//                            headPos = new Vector2i(posX + 2, posY + 1);
//                            tailPos = new Vector2i(posX + 2, posY + 1);
//
//                        } else {
//                            headPos = new Vector2i(posX + 3, posY + 1);
//                            tailPos = new Vector2i(posX + 1, posY + 1);
//                        }
//                        break;
//                    }
//                    case DOWN: {
//                        if (side1 == side2) {
//                            headPos = new Vector2i(posX + 1, posY + 2);
//                            tailPos = new Vector2i(posX + 1, posY + 2);
//                        } else {
//                            headPos = new Vector2i(posX + 1, posY + 1);
//                            tailPos = new Vector2i(posX + 1, posY + 3);
//                        }
//                        break;
//                    }
//                    case LEFT: {
//                        if (side1 == side2) {
//                            headPos = new Vector2i(posX + 2, posY + 1);
//                            tailPos = new Vector2i(posX + 2, posY + 1);
//                        } else {
//                            headPos = new Vector2i(posX + 1, posY + 1);
//                            tailPos = new Vector2i(posX + 3, posY + 1);
//                        }
//                        break;
//                    }
//                }

            } else {
                System.out.println("isHead: " + isHead(domino));
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

                        headDirection = Domino.DIRECTION.getDirectionByNumber((domino.getDirection().getNumber() + 1) % 4);
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

            System.out.println("Current table status:");
            System.out.println("Head pos: " + headPos.x + ";" + headPos.y + " = " + headNumber);
            System.out.println("Tail pos: " + tailPos.x + ";" + tailPos.y + " = " + tailNumber);
            System.out.println();
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

        int posX = domino.getPositionCoord().x;
        int posY = domino.getPositionCoord().y;

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

        int posX = domino.getPositionCoord().x;
        int posY = domino.getPositionCoord().y;

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

        int posX = domino.getPositionCoord().x;
        int posY = domino.getPositionCoord().y;

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

        int posX = domino.getPositionCoord().x;
        int posY = domino.getPositionCoord().y;

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

    public int getSize() {
        return amount;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean isPositionValid(Vector2i pos1, Vector2i pos2) {
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