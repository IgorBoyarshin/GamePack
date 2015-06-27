package gamepack.domino.players;

import gamepack.domino.Domino;
import gamepack.domino.Table;

import java.util.List;

/**
 * Created by Igor on 24-Jun-15.
 */
public abstract class Player {
    protected static Table table;
    protected List<Domino> dominoes;
    protected String name;

    protected boolean moveMade;

    public Player(String name, List<Domino> dominoes) {
        this.dominoes = dominoes;
        this.name = name;

        moveMade = false;
    }

    public static void prepareTable(List<Domino> pool) {
        table = new Table(30);
        table.setPool(pool);
    }

    public static void repositionTable() {
        for (Domino domino : table.getPool()) {
            domino.setPosition(20, 20);
        }
    }

    protected static void placeDomino(Domino domino) {
        table.placeDomino(domino);
    }

    public abstract void reposition();

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
