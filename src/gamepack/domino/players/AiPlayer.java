package gamepack.domino.players;

import gamepack.domino.Domino;

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
            domino.setPositionCoord(i * Domino.TILES_PER_SIDE + i * 2, 30);
            domino.setDirection(Domino.DIRECTION.UP, true);
        }
    }

    public void makeMove() {
        int counter = 0;

        while (hasSide(dominoes.get(counter)) == -1) {
            counter++;
            if (counter == dominoes.size()) {
                dominoes.add(table.takeDominoFromPool());
            }
        }
        //TODO: Process the case when the pool ends

        putOnTable(dominoes.remove(counter));

        moveMade = true;
    }

    private void putOnTable(Domino domino) {
        domino.flipUp();

        if (hasSide(domino) == 1) {
            if (domino.getSide1() == domino.getSide2()) {
                rotateDomino(domino, (table.getHeadDirection().getNumber() + 1) % 4);


            } else if (domino.getSide1() == table.getHeadNumber()) {
                Domino.DIRECTION headDirection = table.getHeadDirection();
            } else {

            }
        } else if (hasSide(domino) == 2) {

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
