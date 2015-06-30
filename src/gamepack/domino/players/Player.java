package gamepack.domino.players;

import gamepack.domino.Domino;
import gamepack.domino.Table;
import gamepack.domino.Vector2i;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import java.util.List;

/**
 * Created by Igor on 24-Jun-15.
 */
public abstract class Player {
    protected static Table table;
    protected List<Domino> dominoes;
    protected String name;
    protected Vector2i repositionStart;

    protected boolean moveMade;

    public Player(String name, List<Domino> dominoes) {
        this.dominoes = dominoes;
        this.name = name;

        repositionStart = new Vector2i(0,0);

        moveMade = false;
    }

    public static void repositionTablePool(Vector2i vector) {
        table.repositionPool(vector);
    }

    public static void shiftTable(Vector2i vector) {
        table.shift(vector);
    }

    public static void prepareTable(List<Domino> pool, int fieldSize) {
        table = new Table(fieldSize);
        table.setPool(pool);
    }

//    public static void repositionPool() {
//        for (Domino domino : table.getPool()) {
//            domino.setPositionCoord(38, 20);
//        }
//    }

    public boolean canMakeMove() {
        if (table.getPoolSize() > 0) {
            return true;
        }

        for (Domino domino : dominoes) {
            if (canBePlaced(domino)) {
                return true;
            }
        }

        return false;
    }

    protected boolean canBePlaced(Domino domino) {
        int headNumber = table.getHeadNumber();
        int tailNumber = table.getTailNumber();
        int side1 = domino.getSide1();
        int side2 = domino.getSide2();

        if (side1 == headNumber || side1 == tailNumber || side2 == headNumber || side2 == tailNumber) {
            return true;
        }

        return false;
    }

    public int getScore() {
        int score = 0;

        for (Domino domino : dominoes) {
            score += domino.getSide1();
            score += domino.getSide2();
        }

        return score;
    }

    public int getAmount() {
        return dominoes.size();
    }

    protected static void placeDomino(Domino domino) {
        table.placeDomino(domino);
    }

    public abstract void reposition(Vector2i start);

    public String getName() {
        return name;
    }

    public abstract void makeMove();

    public void endMove() {
        moveMade = false;
    }

    public boolean isMoveMade() {
        return moveMade;
    }
}
