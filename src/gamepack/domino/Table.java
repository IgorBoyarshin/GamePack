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
        if (isPositionValid(domino)) {
            int side1 = domino.getSide1();
            int side2 = domino.getSide2();

            int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
            int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

            if (amount == 0) {
                headNumber = side1;
                tailNumber = side2;

                switch (domino.getDirection()) {
                    case UP: {
                        if (side1 == side2) {
                            headPos = new Vector2i(posX + 1, posY + 2);
                            tailPos = new Vector2i(posX + 1, posY + 2);
                        } else {
                            headPos = new Vector2i(posX + 1, posY + 3);
                            tailPos = new Vector2i(posX + 1, posY + 1);
                        }
                        break;
                    }
                    case RIGHT: {
                        if (side1 == side2) {
                            headPos = new Vector2i(posX + 2, posY + 1);
                            tailPos = new Vector2i(posX + 2, posY + 1);

                        } else {
                            headPos = new Vector2i(posX + 3, posY + 1);
                            tailPos = new Vector2i(posX + 1, posY + 1);
                        }
                        break;
                    }
                    case DOWN: {
                        if (side1 == side2) {
                            headPos = new Vector2i(posX + 1, posY + 2);
                            tailPos = new Vector2i(posX + 1, posY + 2);
                        } else {
                            headPos = new Vector2i(posX + 1, posY + 1);
                            tailPos = new Vector2i(posX + 1, posY + 3);
                        }
                        break;
                    }
                    case LEFT: {
                        if (side1 == side2) {
                            headPos = new Vector2i(posX + 2, posY + 1);
                            tailPos = new Vector2i(posX + 2, posY + 1);
                        } else {
                            headPos = new Vector2i(posX + 1, posY + 1);
                            tailPos = new Vector2i(posX + 3, posY + 1);
                        }
                        break;
                    }
                }

            } else {
                if (isHead(domino)) {
                    if (domino.getSide1() == domino.getSide2()) {
                        tailPos = getCenterPos(domino);
                    } else {
                        if (domino.getSide1() == tailNumber) {
                            tailNumber = domino.getSide2();
                            tailPos = getTailPos(domino);
                        } else {
                            tailNumber = domino.getSide1();
                            tailPos = getHeadPos(domino);
                        }
                    }
                } else {
                    if (domino.getSide1() == domino.getSide2()) {
                        headPos = getCenterPos(domino);
                    } else {
                        if (domino.getSide1() == tailNumber) {
                            headNumber = domino.getSide2();
                            headPos = getTailPos(domino);
                        } else {
                            headNumber = domino.getSide1();
                            headPos = getHeadPos(domino);
                        }
                    }
                }
            }


            amount++;
        }
    }

    private Vector2i getCenterPos(Domino domino) {
        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

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
        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

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
        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

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
        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());
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

    private int distance(int x1, int y1, int x2, int y2) {
        return ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean isPositionValid(Domino domino) {
        return true;
    }
}