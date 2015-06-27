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
            domino.setPosition(i * Domino.TILES_PER_SIDE + i * 2, 25);
        }
    }

    public void makeMove() {
        System.out.println("AI making move");
        moveMade = true;
    }
}
