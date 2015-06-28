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
            domino.setPositionCoord(i * Domino.TILES_PER_SIDE + i * 2, 1);
            domino.setDirection(Domino.DIRECTION.UP, true);
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
                    System.out.println("CUR: " +
                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

                    if (Player.table.isPositionValid(currentDomino.getDomino())) {
                        currentDomino.maskGreen();
                    } else {
                        currentDomino.maskRed();
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_LEFT)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    currentNumberDomino = (currentNumberDomino + (dominoes.size() - 1)) % dominoes.size();
                    currentDomino.setCurrentDomino(dominoes.get(currentNumberDomino));
                } else {
                    currentDomino.moveLeft();
                    System.out.println("CUR: " +
                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

                    if (Player.table.isPositionValid(currentDomino.getDomino())) {
                        currentDomino.maskGreen();
                    } else {
                        currentDomino.maskRed();
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_UP)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.moveUp();
                    System.out.println("CUR: " +
                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

                    if (Player.table.isPositionValid(currentDomino.getDomino())) {
                        currentDomino.maskGreen();
                    } else {
                        currentDomino.maskRed();
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_DOWN)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.moveDown();
                    System.out.println("CUR: " +
                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

                    if (Player.table.isPositionValid(currentDomino.getDomino())) {
                        currentDomino.maskGreen();
                    } else {
                        currentDomino.maskRed();
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_ENTER)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    currentDomino.setChosen();
                } else {
                    if (table.isPositionValid(currentDomino.getDomino())) {
                        table.placeDomino(currentDomino.getDomino());
                        dominoes.remove(currentDomino.getDomino());
                        currentDomino.unChoose();

                        if (dominoes.size() > 0) {
                            currentNumberDomino = 0;
                            reposition();
                            currentDomino.setCurrentDomino(dominoes.get(0));
                        }

                        moveMade = true;
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.rotateClockWise();
                }
            }

            if (window.isKeyDown(GLFW_KEY_T)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    dominoes.add(table.takeDominoFromPool());
                    dominoes.get(dominoes.size() - 1).flipUp();
                    reposition();
                }
            }
        }
    }
}
