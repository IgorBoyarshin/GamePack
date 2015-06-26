package gamepack.domino;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Igor on 18-Jun-15.
 */
public class Table {
//    private TableElement root;
//    private TableElement currentNext;
//    private TableElement currentPrev;

    private Vector2i prevPos;
    private Vector2i nextPos;
    private int prevNumber;
    private int nextNumber;

    private int amount;

    private final int fieldSize;

    private boolean field[][];

    public Table(int fieldSize) {
        this.fieldSize = fieldSize;

        field = new boolean[fieldSize][fieldSize];
    }

    public void placeDomino(Domino domino) {
        if (isPositionValid(domino)) {
            if (amount == 0) {
//                root = new TableElement(domino);
//                currentNext = root;
//                currentPrev = root;
//                numberNext = domino.getSide1();// they are supposed to be the same
//                numberPrev = domino.getSide1();


                nextNumber = domino.getSide2();
                prevNumber = domino.getSide1();

                int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
                int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

                switch (domino.getDirection()) {
                    case UP: {
                        if (domino.getSide1() == domino.getSide2()) {
                            nextPos = new Vector2i(posX, posY + 1);
                            prevPos = new Vector2i(posX, posY + 1);
                        } else {
                            nextPos = new Vector2i(posX, posY + 2);
                            prevPos = new Vector2i(posX, posY);
                        }
                        break;
                    }
                    case RIGHT: {
                        if (domino.getSide1() == domino.getSide2()) {
                            nextPos = new Vector2i(posX + 1, posY - 2);
                            prevPos = new Vector2i(posX + 1, posY - 2);
                        } else {
                            nextPos = new Vector2i(posX + 2, posY - 2);
                            prevPos = new Vector2i(posX, posY - 2);
                        }
                        break;
                    }
                    case DOWN: {
                        if (domino.getSide1() == domino.getSide2()) {
                            nextPos = new Vector2i(posX, posY - 3);
                            prevPos = new Vector2i(posX, posY - 3);
                        } else {
                            nextPos = new Vector2i(posX - 2, posY - 4);
                            prevPos = new Vector2i(posX - 2, posY - 2);
                        }
                        break;
                    }
                    case LEFT: {
                        if (domino.getSide1() == domino.getSide2()) {
                            nextPos = new Vector2i(posX - 3, posY + 1);
                            prevPos = new Vector2i(posX - 3, posY + 1);
                        } else {
                            nextPos = new Vector2i(posX - 2, posY - 2);
                            prevPos = new Vector2i(posX - 4, posY - 2);
                        }
                        break;
                    }
                }

            } else {
                int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
                int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

                if (isPrev(domino)) {
                    if (domino.getSide1() == domino.getSide2()) {
                        prevPos.x = posX + (int)domino.getDirection().getX();
                        prevPos.y = posY + (int)domino.getDirection().getY();
                    } else {
                        if (domino.getSide2() == prevNumber) {

                        } else {

                        }
                    }
                } else {

                }
            }


            amount++;
        }
    }


    // TODO: rewrite. Wrong sizes
    public boolean isPrev(Domino domino) {
        int posX = (int) (domino.getPosition().x / Domino.getTileWidth());
        int posY = (int) (domino.getPosition().y / Domino.getTileWidth());

        switch (domino.getDirection()) {
            case UP: {
                if (prevPos.x >= posX - 2 && prevPos.x <= posX + 4) {
                    if (prevPos.y >= posY - 2 && prevPos.y <= posY + 6) {
                        return true;
                    }
                }
                break;
            }
            case RIGHT: {
                if (prevPos.x >= posX - 2 && prevPos.x <= posX + 6) {
                    if (prevPos.y >= posY - 4 && prevPos.y <= posY + 2) {
                        return true;
                    }
                }
                break;
            }
            case DOWN: {
                if (prevPos.x >= posX - 4 && prevPos.x <= posX + 2) {
                    if (prevPos.y >= posY - 6 && prevPos.y <= posY + 2) {
                        return true;
                    }
                }
                break;
            }
            case LEFT: {
                if (prevPos.x >= posX - 6 && prevPos.x <= posX + 2) {
                    if (prevPos.y >= posY - 2 && prevPos.y <= posY + 4) {
                        return true;
                    }
                }
                break;
            }
        }

        return false;
    }

    public boolean isPositionValid(Domino domino) {
        return true;
    }
}
