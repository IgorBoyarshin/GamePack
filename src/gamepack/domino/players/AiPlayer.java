package gamepack.domino.players;

import gamepack.domino.Domino;
import gamepack.domino.Vector2i;

import java.util.List;

/**
 * Created by Igor on 27-Jun-15.
 */
public class AiPlayer extends Player {
    public AiPlayer(String name, List<Domino> dominoes) {
        super(name, dominoes);
    }

    public void reposition() {
        for (int i = 0; i < dominoes.size(); i++) {
            Domino domino = dominoes.get(i);
            domino.setPositionCoord(i * Domino.TILES_PER_SIDE + i * 2, 38);
            domino.setDirection(Domino.DIRECTION.UP, true);
            domino.flipUp();
        }
    }

    public void makeMove() {
        int counter = 0;

        if (dominoes.size() == 0) {
            if (table.getPoolSize() > 0) {
                dominoes.add(table.takeDominoFromPool());
            } else {
                moveMade = true;
                return;
            }
        }

        while (hasSide(dominoes.get(counter)) == -1) {
            counter++;

            if (counter == dominoes.size()) {
                Domino newDomino = table.takeDominoFromPool();
                if (newDomino != null) {
                    dominoes.add(newDomino);
                } else {
                    moveMade = true;
                    return;
                }
            }
        }

        putOnTable(dominoes.remove(counter));

        reposition();

        moveMade = true;
    }

    private void putOnTable(Domino domino) {
        domino.flipUp();

        if (hasSide(domino) == 1) {
            if (domino.getSide1() == domino.getSide2()) {
                putDouble(domino, table.getHeadPos(), table.getHeadDirection());
            } else {
                putDomino(domino, table.getHeadPos(), table.getHeadDirection(), table.getHeadNumber());
            }
        } else if (hasSide(domino) == 2) {
            if (domino.getSide1() == domino.getSide2()) {
                putDouble(domino, table.getTailPos(), table.getTailDirection());
            } else {
                putDomino(domino, table.getTailPos(), table.getTailDirection(), table.getTailNumber());
            }
        }

        placeDomino(domino);

//        System.out.println("AI:");
//        System.out.println("Placing domino " + domino.getSide1() + "*" + domino.getSide2());
//        System.out.println("At " + domino.getPositionCoord().x + ";" + domino.getPositionCoord().y);
//        System.out.println();
    }

    private void putDomino(Domino domino, Vector2i point, Domino.DIRECTION direction, int number) {
        if (table.getAmount() == 1) {
            switch (direction) {
                case UP:
                case DOWN: {
                    if (domino.getSide1() == number) {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x - 1, point.y + 1);
                    } else {
                        domino.setPositionCoord(point.x - 1, point.y + 1);
                    }

                    break;
                }
                case RIGHT:
                case LEFT: {
                    if (domino.getSide1() == number) {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x + 1, point.y - 1);
                    } else {
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x + 1, point.y - 1);
                    }

                    break;
                }
            }

            return;
        }

        switch (direction) {
            case UP:
            case DOWN: {
                Vector2i pos1 = new Vector2i(point.x, point.y - 2);
                Vector2i pos2 = new Vector2i(point.x, point.y - 4);

                if (domino.getSide1() == number) {
                    if (table.isPositionValid(pos1, pos2)) {
                        domino.setPositionCoord(point.x - 1, point.y - 5);
                    } else {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x - 1, point.y + 1);
                    }
                } else {
                    if (table.isPositionValid(pos1, pos2)) {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x - 1, point.y - 5);
                    } else {
                        domino.setPositionCoord(point.x - 1, point.y + 1);
                    }
                }

                break;
            }

            case RIGHT:
            case LEFT: {
                Vector2i pos1 = new Vector2i(point.x - 2, point.y);
                Vector2i pos2 = new Vector2i(point.x - 4, point.y);

                if (domino.getSide1() == number) {
                    if (table.isPositionValid(pos1, pos2)) {
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x - 5, point.y - 1);
                    } else {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x + 1, point.y - 1);
                    }
                } else {
                    if (table.isPositionValid(pos1, pos2)) {
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x - 5, point.y - 1);
                    } else {
                        domino.rotateClockWise();
                        domino.setPositionCoord(point.x + 1, point.y - 1);
                    }
                }

                break;
            }
        }
    }

    private void putDouble(Domino domino, Vector2i point, Domino.DIRECTION direction) {
        switch (direction) {
            case UP:
            case DOWN: {
                domino.rotateClockWise();

                Vector2i pos11 = new Vector2i(point.x - 1, point.y + 2);
                Vector2i pos12 = new Vector2i(point.x + 1, point.y + 2);

                if (table.isPositionValid(pos11, pos12)) {
                    domino.setPositionCoord(point.x - 2, point.y + 1);
                } else {
                    domino.setPositionCoord(point.x - 2, point.y - 3);
                }

                break;
            }
            case RIGHT:
            case LEFT: {
                Vector2i pos11 = new Vector2i(point.x - 2, point.y - 1);
                Vector2i pos12 = new Vector2i(point.x - 2, point.y + 1);

                if (table.isPositionValid(pos11, pos12)) {
                    domino.setPositionCoord(point.x - 3, point.y - 2);
                } else {
                    domino.setPositionCoord(point.x + 1, point.y - 2);
                }

                break;
            }
        }
    }

    private void rotateDomino(Domino domino, int direction) {
        domino.setDirection(Domino.DIRECTION.getDirectionByNumber(direction), true);
    }

    private int hasSide(Domino domino) {
        int head = table.getHeadNumber();
        int tail = table.getTailNumber();
        int side1 = domino.getSide1();
        int side2 = domino.getSide2();

        if (side1 == head || side2 == head) {
            return 1;
        } else if (side1 == tail || side2 == tail) {
            return 2;
        }

        return -1;
    }
}
