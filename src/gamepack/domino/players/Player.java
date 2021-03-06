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

    public enum TYPE {
        HUMAN, AI;
    }

    protected final TYPE type;
    protected static Table table;
    protected List<Domino> dominoes;
    protected String name;
    protected Vector2i repositionStart;

    protected boolean moveMade;

    public Player(TYPE type, String name, List<Domino> dominoes) {
        this.type = type;
        this.dominoes = dominoes;
        this.name = name;

        repositionStart = new Vector2i(0,0);

        moveMade = false;
    }

    public Player(TYPE type, String name) {
        this.type = type;
        this.name = name;

        repositionStart = new Vector2i(0,0);

        moveMade = false;
    }

    public static void repositionTablePool(Vector2i start, Vector2i finish) {
        table.repositionPool(start, finish);
    }

    public void flipDominoesDown() {
        for (Domino domino : dominoes) {
            domino.flipDown();
        }
    }

    public void flipDominoesUp() {
        for (Domino domino : dominoes) {
            domino.flipUp();
        }
    }

    public static void shiftTable(Vector2i vector) {
        table.shift(vector);
    }

    public static void prepareTable(List<Domino> pool, int fieldSize, float fieldZ) {
        table = new Table(fieldSize, fieldZ);
        table.setPool(pool);
    }

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

        if (dominoes.size() == 1) {
            if (dominoes.get(0).getSide1() == 0 && dominoes.get(0).getSide2() == 0) {
                return 10;
            }
        }

        return score;
    }

    public void setDominoes(List<Domino> dominoes) {
        this.dominoes = dominoes;
        reposition(repositionStart);
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
        moveMade = true;
    }

    public void resetMoveMade() {
        moveMade = false;
    }

    public static int getPoolSize() {
        return table.getPoolSize();
    }

    public boolean isMoveMade() {
        return moveMade;
    }

    public TYPE getType() {
        return type;
    }
}
