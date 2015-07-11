package gamepack.domino.players;

import gamepack.domino.Domino;
import gamepack.domino.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Igor on 27-Jun-15.
 */
public class AiPlayer extends Player {
    public AiPlayer(String name) {
        super(TYPE.AI, name);
    }

    public AiPlayer(String name, List<Domino> dominoes) {
        super(TYPE.AI, name, dominoes);
    }

    public static boolean showAiDominoes = false;

    private final Vector2i AiDoubleStart = new Vector2i(35,35);

    private boolean startedThinking = false;
    private long thinkingStart;
    private static final long thinkingDurationLong = 20;
    private static final long thinkingDurationSmall = 150;
    private static long thinkingDuration = thinkingDurationSmall;

    public void reposition(Vector2i start) {
        for (int i = 0; i < dominoes.size(); i++) {
            Domino domino = dominoes.get(i);
            domino.setPositionCoord(start.x + i * Domino.TILES_PER_SIDE + i * 2, start.y);
            domino.setDirection(Domino.DIRECTION.UP, true);
//            domino.flipUp();
        }

        repositionStart = start;
    }

    public void makeMove() {
        if (!startedThinking) {
            startedThinking = true;
            thinkingStart = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - thinkingStart < thinkingDuration) {
            return;
        }

//        System.out.println();
//        System.out.println("---- STARTING NEW MOVE ----");

        // If the first move of the game
        if (table.getAmount() == 0) {
            int counter = 0;
            Domino domino = dominoes.get(counter);
            while (dominoes.get(counter).getSide1() != dominoes.get(counter).getSide2()) {
                counter++;
                if (counter == dominoes.size()) {
                    dominoes.add(table.takeDominoFromPool());
                    reposition(repositionStart);
                }
                domino = dominoes.get(counter);
            }

            domino.flipUp();
            domino.setPositionCoord(AiDoubleStart.x, AiDoubleStart.y);
            table.placeDomino(domino);
            dominoes.remove(domino);
            reposition(repositionStart);

            moveMade = true;
            startedThinking = false;
            return;
        }

        int counter = 0;

        if (dominoes.size() == 0) {
            if (table.getPoolSize() > 0) {
                dominoes.add(table.takeDominoFromPool());
                reposition(repositionStart);
            } else {
                moveMade = true;
                startedThinking = false;
                return;
            }
        }

        while (hasSide(dominoes.get(counter)) == -1) {
            counter++;

            if (counter == dominoes.size()) {
                Domino newDomino = table.takeDominoFromPool();
                if (newDomino != null) {
                    dominoes.add(newDomino);
                    // Should do repositioning here(after each take), but it is gonna be done anyway later at the end
                } else {
                    moveMade = true;
                    startedThinking = false;
                    reposition(repositionStart); // The case when we took from pool but didn't move domino to the AI
                    return;
                }
            }
        }

        makeMoveWith(dominoes.remove(counter));

        reposition(repositionStart);

        moveMade = true;
        startedThinking = false;
    }

    private void makeMoveWith(Domino domino) {
        domino.flipUp();

        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        boolean isTableHead = hasSide(domino) == 1;

        Vector2i centerPosition = isTableHead ?
                new Vector2i(table.getHeadPos().x, table.getHeadPos().y) :
                new Vector2i(table.getTailPos().x, table.getTailPos().y);

        List<Position> possiblePositions = (domino.getSide1() == domino.getSide2() ?
                getPositionsListForDouble(centerPosition) : getPositionsList(centerPosition))
                .stream()
                .filter(position -> table.isPositionValid(position.p1, position.p2))
                .filter(position -> canPlaceDoubleAndNext(position.p1, position.p2, domino))
                .collect(Collectors.toList());
//        System.out.println("Lambda returned " + possiblePositions.size() + " instances for "
//                + domino.getSide1() + "-" + domino.getSide2());

        if (possiblePositions.size() == 0) {
            System.out.println("NULL");
//            System.out.println("Domino: " + domino.getSide1() + "-" + domino.getSide2());
//            table.printMaskAround(centerPosition);
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        positionRotateAndPutOnTable(domino, possiblePositions.get(Math.abs(r.nextInt()) % possiblePositions.size()),
                isTableHead ? table.getHeadNumber() : table.getTailNumber());
    }

    private void positionRotateAndPutOnTable(Domino domino, Position position, int side) {
        Vector2i pos = ((position.p1.x <= position.p2.x) && (position.p1.y <= position.p2.y)) ? position.p1 : position.p2;
        domino.setPositionCoord(pos.x - 1, pos.y - 1);
        domino.setDirectionUvAndSize(determineDirection(position.p1, position.p2, domino.getSide1() == side));

        table.placeDomino(domino);
    }

    private boolean canPlaceDoubleAndNext(Vector2i p1, Vector2i p2, Domino domino) {
        if (domino.getSide1() == domino.getSide2()) {
            return true;
        }

        boolean isHead = hasSide(domino) == 1;
        boolean isPlacingOnDouble = isHead ? table.isHeadDouble() : table.isTailDouble();

        if (isPlacingOnDouble) {
            return true;
        }

        int number =  isHead ?
                table.getHeadNumber() :
                table.getTailNumber();
        int side = domino.getSide1() == number ? domino.getSide2() : domino.getSide1();
        boolean doubleIsUsed = doubleHasBeenUsed(side);

        Vector2i diff = new Vector2i(p2.x - p1.x, p2.y - p1.y);
        Vector2i center = new Vector2i((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

        Vector2i doubleP1 = new Vector2i(center.x + (diff.x * 3 / 2) + diff.y / 2, center.y + (diff.y * 3 / 2) + diff.x / 2);
        Vector2i doubleP2 = new Vector2i(center.x + (diff.x * 3 / 2) - diff.y / 2, center.y + (diff.y * 3 / 2) - diff.x / 2);
        Vector2i nextP1 = new Vector2i(center.x + (diff.x * 5 / 2), center.y + (diff.y * 5 / 2));
        Vector2i nextP2;
        if (doubleIsUsed) {
            nextP2 = new Vector2i(center.x + (diff.x * 3 / 2), center.y + (diff.y * 3 / 2));
        } else {
            nextP2 = new Vector2i(center.x + (diff.x * 7 / 2), center.y + (diff.y * 7 / 2));
        }

//        System.out.println("For (" + p1.x + " " + p1.y + ") (" + p2.x + " " + p2.y + ")");
//        System.out.println("Double: (" + doubleP1.x + " " + doubleP1.y + ") (" + doubleP2.x + " " + doubleP2.y + ")");
//        System.out.println("Next: (" + nextP1.x + " " + nextP1.y + ") (" + nextP2.x + " " + nextP2.y + ")");

        if (!doubleIsUsed) {
            if (!table.isPositionValid(doubleP1, doubleP2)) {
//            System.out.println("Returning false");
                return false;
            }
        }
        if (!table.isPositionValid(nextP1, nextP2)) {
//            System.out.println("Returning false 2");
            return false;
        }

        return true;
    }

    private Domino.DIRECTION determineDirection(Vector2i head, Vector2i tail, boolean headToHead) {
        Domino.DIRECTION direction;

        if (head.x == tail.x) {
            if (head.y > tail.y) {
                direction = Domino.DIRECTION.UP;
            } else {
                direction = Domino.DIRECTION.DOWN;
            }
        } else {
            if (head.x > tail.x) {
                direction = Domino.DIRECTION.RIGHT;
            } else {
                direction = Domino.DIRECTION.LEFT;
            }
        }

        if (!headToHead) {
            direction = Domino.DIRECTION.getDirectionByNumber((direction.getNumber() + 2) % 4);
        }

        return direction;
    }

    private boolean doubleHasBeenUsed(int side) {
        return table.getTableDominoes()
                .stream()
                .filter(vector -> vector.x == vector.y)
                .filter(vector -> vector.x == side)
                .count() == 1;
    }

    /**
     * @param domino
     * @return -1 if there is not side
     * @return1 if matches the table head
     * @return2 if matches the table tail
     */
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

    private List<Position> getPositionsList(Vector2i center) {
        List<Position> positions = new ArrayList<>();

        positions.add(new Position(new Vector2i(center.x + 2, center.y), new Vector2i(center.x + 4, center.y)));
        positions.add(new Position(new Vector2i(center.x - 2, center.y), new Vector2i(center.x - 4, center.y)));
        positions.add(new Position(new Vector2i(center.x, center.y + 2), new Vector2i(center.x, center.y + 4)));
        positions.add(new Position(new Vector2i(center.x, center.y - 2), new Vector2i(center.x, center.y - 4)));

        return positions;
    }

    private List<Position> getPositionsListForDouble(Vector2i center) {
        List<Position> positions = new ArrayList<>();

        positions.add(new Position(new Vector2i(center.x + 2, center.y + 1), new Vector2i(center.x + 2, center.y - 1)));
        positions.add(new Position(new Vector2i(center.x - 2, center.y + 1), new Vector2i(center.x - 2, center.y - 1)));
        positions.add(new Position(new Vector2i(center.x - 1, center.y + 2), new Vector2i(center.x + 1, center.y + 2)));
        positions.add(new Position(new Vector2i(center.x - 1, center.y - 2), new Vector2i(center.x + 1, center.y - 2)));

        return positions;
    }

    public static void setThinkingDurationSmall() {
        thinkingDuration = thinkingDurationSmall;
    }

    public static void setThinkingDurationLong() {
        thinkingDuration = thinkingDurationLong;
    }

    private class Position {
        public Vector2i p1;
        public Vector2i p2;

        public Position(Vector2i p1, Vector2i p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}
