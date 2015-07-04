package gamepack.domino.players;

import gamepack.Game;
import gamepack.domino.CurrentDomino;
import gamepack.domino.Domino;
import gamepack.domino.Vector2i;
import himmel.graphics.Window;
import himmel.graphics.layers.Layer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;

/**
 * Created by Igor on 27-Jun-15.
 */
public class HumanPlayer extends Player {
    private Window window;

    private static CurrentDomino currentDomino;
    private int currentNumberDomino = 0;

    private static boolean firstLaunch = true;

    public HumanPlayer(String name, Window window, Layer gameLayer) {
        super(name);
        this.window = window;

        if (firstLaunch) {
            firstLaunch = false;

            currentDomino = new CurrentDomino(null);
            currentDomino.submitMaskTo(gameLayer);
        }
    }

    public HumanPlayer(String name, List<Domino> dominoes, Window window, Layer gameLayer) {
        super(name, dominoes);
        this.window = window;

        if (firstLaunch) {
            firstLaunch = false;

            currentDomino = new CurrentDomino(dominoes.get(0));
            currentDomino.submitMaskTo(gameLayer);
        }
    }

    public void reposition(Vector2i start) {
        if (dominoes.size() > 0) {
            for (int i = 0; i < dominoes.size(); i++) {
                Domino domino = dominoes.get(i);
                domino.setPositionCoord(start.x + i * Domino.TILES_PER_SIDE + i * 2, start.y);
                domino.setDirection(Domino.DIRECTION.UP, true);
            }

            currentDomino.setCurrentDomino(dominoes.get(0));
        }

        repositionStart = start;
    }

    public void makeMove() {
        if (dominoes.size() > 0) {
            if (currentDomino.getDomino() != dominoes.get(currentNumberDomino)) {
                currentDomino.setCurrentDomino(dominoes.get(currentNumberDomino));
            }
        }

        if (System.currentTimeMillis() - Game.lastKeyboard > Game.keyboardMillisDelay) {
            if (dominoes.size() > 0) {
                if (window.isKeyDown(GLFW_KEY_RIGHT)) {
                    Game.lastKeyboard = System.currentTimeMillis();

                    if (!currentDomino.isChosen()) {
                        currentNumberDomino = (currentNumberDomino + 1) % dominoes.size();
                        currentDomino.setCurrentDomino(dominoes.get(currentNumberDomino));
                    } else {
                        currentDomino.moveRight();
                        if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT) || window.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
                            currentDomino.moveRight();
                        }
//                    System.out.println("CUR: " +
//                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

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
                        if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT) || window.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
                            currentDomino.moveLeft();
                        }
//                    System.out.println("CUR: " +
//                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

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
                        if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT) || window.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
                            currentDomino.moveUp();
                        }
//                    System.out.println("CUR: " +
//                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

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
                        if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT) || window.isKeyDown(GLFW_KEY_RIGHT_SHIFT)) {
                            currentDomino.moveDown();
                        }
//                    System.out.println("CUR: " +
//                            currentDomino.getDomino().getPosition().x + ";" + currentDomino.getDomino().getPosition().y);

                        if (Player.table.isPositionValid(currentDomino.getDomino())) {
                            currentDomino.maskGreen();
                        } else {
                            currentDomino.maskRed();
                        }
                    }
                }
            }

            if (System.currentTimeMillis() - Game.lastKeyboard > Game.keyboardMillisDelayLong) {
                if (window.isKeyDown(GLFW_KEY_ENTER)) {
                    Game.lastKeyboard = System.currentTimeMillis();

                    if (dominoes.size() > 0) {
                        if (!currentDomino.isChosen()) {
                            if (table.getAmount() == 0) {
                                if (currentDomino.getDomino().getSide1() == currentDomino.getDomino().getSide2()) {
                                    currentDomino.setChosen();
                                }
                            } else {
                                currentDomino.setChosen();
                            }
                        } else {
                            if (table.isPositionValid(currentDomino.getDomino())) {
                                table.placeDomino(currentDomino.getDomino());
                                dominoes.remove(currentDomino.getDomino());
                                currentDomino.unChoose();

                                if (dominoes.size() > 0) {
                                    currentNumberDomino = 0;
                                    reposition(repositionStart);
                                    currentDomino.setCurrentDomino(dominoes.get(0));
                                } else {
                                    currentDomino.maskNull();
//                                currentDomino.setCurrentDomino(null);
                                }

                                moveMade = true;
                            }
                        }
                    }
                }
            }

            if (window.isKeyDown(GLFW_KEY_LEFT_CONTROL) || window.isKeyDown(GLFW_KEY_RIGHT_CONTROL)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (currentDomino.isChosen()) {
                    currentDomino.rotateClockWise();
                }
            }

            if (window.isKeyDown(GLFW_KEY_BACKSPACE)) {
                Game.lastKeyboard = System.currentTimeMillis();

                currentDomino.unChoose();
                reposition(repositionStart);
            }

            if (window.isKeyDown(GLFW_KEY_T)) {
                Game.lastKeyboard = System.currentTimeMillis();

                if (!currentDomino.isChosen()) {
                    if (table.getPoolSize() > 0) {
                        dominoes.add(table.takeDominoFromPool());
                        dominoes.get(dominoes.size() - 1).flipUp();
                        reposition(repositionStart);

                        if (dominoes.size() == 1) {
                            currentDomino.setCurrentDomino(dominoes.get(0));
                            currentDomino.maskSelected();
                            currentNumberDomino = 0;
                            reposition(repositionStart);
                        }
                    }
                }
            }
        }
    }
}
