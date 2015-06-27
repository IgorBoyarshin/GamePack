package gamepack.domino.players;

import gamepack.Game;
import gamepack.domino.CurrentDomino;
import gamepack.domino.Domino;
import himmel.graphics.Window;
import himmel.graphics.layers.Layer;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;

/**
 * Created by Igor on 27-Jun-15.
 */
public class HumanPlayer extends Player {
    private Window window;

    private CurrentDomino currentDomino;
    private int currentNumberDomino = 0;

    public HumanPlayer(String name, List<Domino> dominoes, Window window, Layer gameLayer) {
        super(name, dominoes);
        this.window = window;

        currentDomino = new CurrentDomino(dominoes.get(0));
        currentDomino.submitMaskTo(gameLayer);
    }

    // TODO: rewrite to be arbitrary. Mb make private
    public void reposition() {
        for (int i = 0; i < dominoes.size(); i++) {
            Domino domino = dominoes.get(i);
            domino.setPosition(i * Domino.TILES_PER_SIDE + i * 2, 1);
        }

        currentDomino.setCurrentDomino(dominoes.get(0));
    }

    public void makeMove() {
        if (System.currentTimeMillis() - Game.lastKeyboard > Game.keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_RIGHT)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    currentNumberDomino = (currentNumberDomino + 1) % dominoes.size();
                    currentDomino.setCurrentDomino(dominoes.get(currentNumberDomino));
                } else {
                    currentDomino.moveRight();
                }
            }

            if (window.isKeyDown(GLFW_KEY_LEFT)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    currentNumberDomino = (currentNumberDomino + (dominoes.size() - 1)) % dominoes.size();
                    currentDomino.setCurrentDomino(dominoes.get(currentNumberDomino));
                } else {
                    currentDomino.moveLeft();
                }
            }

            if (window.isKeyDown(GLFW_KEY_UP)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.moveUp();
                }
            }

            if (window.isKeyDown(GLFW_KEY_DOWN)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.moveDown();
                }
            }

            if (window.isKeyDown(GLFW_KEY_ENTER)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    currentDomino.setChosen();
                } else {
                    table.placeDomino(currentDomino.getDomino());
                    dominoes.remove(currentDomino.getDomino());
                    currentDomino.unChoose();
                    currentNumberDomino = 0;
                    reposition();
                    currentDomino.setCurrentDomino(dominoes.get(0));

                    moveMade = true;
                }
            }

            if (window.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.rotateClockWise();
                }
            }
        }
    }
}
