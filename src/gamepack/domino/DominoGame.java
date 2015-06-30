package gamepack.domino;

import gamepack.Game;
import gamepack.Menu;
import gamepack.domino.players.AiPlayer;
import gamepack.domino.players.HumanPlayer;
import gamepack.domino.players.Player;
import himmel.graphics.Sprite;
import himmel.graphics.Texture;
import himmel.graphics.Window;
import himmel.graphics.layers.Layer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

import java.util.*;

/**
 * Created by Igor on 14-Jun-15.
 */
public class DominoGame extends Game {
    private Layer menuLayer;
    private Layer gameLayer;
    private final float menuLayerZ = 0.5f;
    private final float gameLayerDominoesZ = 0.0f;
    private final float gameLayerFieldZ = -0.5f;
    private Menu menu;

    private Texture tileTexture;
    private final float tileSize = 1.4f;
    private final int fieldBlockSize = 11;
    private final int tilesPerBlock = 4;
    private Sprite field[][];

    private final int DOMINOES_AMOUNT = 28;
    private final int HAND_AMOUNT = 5;
    private List<Domino> allDominoes;

    private Player player1;
    private Player player2;

    private boolean gameEnded = false;
    private boolean player1Move = true;

    public DominoGame(float width, float height, Window window) {
        super(width, height, window);

        gameLayer = new Layer();
        menuLayer = new Layer();

        field = new Sprite[fieldBlockSize][fieldBlockSize];

        allDominoes = new ArrayList<>();
        menu = new Menu();

        tileTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//tile4_4.png", Texture.TYPE_RGB);

        prepareDominoes();
        prepareField();
        preparePoolAndPlayers();
    }

    private void prepareField() {
        for (int i = 0; i < fieldBlockSize; i++) {
            for (int j = 0; j < fieldBlockSize; j++) {
                final float blockSize = 4 * tileSize;

                field[i][j] = new Sprite(
                        new Vector3f(i * blockSize, j * blockSize, gameLayerFieldZ),
                        new Vector2f(blockSize, blockSize),
                        tileTexture,
                        Game.spriteRenderer,
                        Game.spriteShader);

                gameLayer.add(field[i][j]);
            }
        }
    }

    private void restart() {
        preparePoolAndPlayers();
    }

    private void prepareDominoes() {
        Texture dominoesTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//dominoes3.png", Texture.TYPE_RGBA);

        Domino.setDominoesTexture(dominoesTexture);
        Domino.setTileWidth(tileSize);

        final float imageWidth = 448.0f;
        final float imageHeight = 2.0f * imageWidth;
        final float dominoWidth = 64.0f;
        final float dominoHeight = 2.0f * dominoWidth;

        for (int i = 6; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                List<Vector2f> uv = new ArrayList<>();
                Vector4f coords = new Vector4f(
                        (6 - i) * dominoWidth / imageWidth,
                        (i - j) * dominoHeight / imageHeight,
                        (6 - i + 1) * dominoWidth / imageWidth,
                        (i - j + 1) * dominoHeight / imageHeight);

                uv.add(new Vector2f(coords.x, coords.w));
                uv.add(new Vector2f(coords.x, coords.y));
                uv.add(new Vector2f(coords.z, coords.y));
                uv.add(new Vector2f(coords.z, coords.w));

//                Vector3f position = new Vector3f((6 - i) * 8.0f, HEIGHT - (i - j) * 8.0f, gameLayerDominoesZ);

                Domino domino = new Domino(i, j, new Vector2i(i, j), gameLayerDominoesZ, Domino.DIRECTION.UP, uv);

                allDominoes.add(domino);

                gameLayer.add(domino);
            }
        }

        float ratioW = dominoWidth / imageWidth;
        float rationH = dominoHeight / imageHeight;
        float shift = 1.0f;

        List<Vector2f> uvDown = new ArrayList<>();
        uvDown.add(new Vector2f(6.0f * ratioW, (1.0f + shift) * rationH));
        uvDown.add(new Vector2f(6.0f * ratioW, (0.0f + shift) * rationH));
        uvDown.add(new Vector2f(7.0f * ratioW, (0.0f + shift) * rationH));
        uvDown.add(new Vector2f(7.0f * ratioW, (1.0f + shift) * rationH));

        shift += 1.0f;

        List<Vector2f> uvSelected = new ArrayList<>();
        uvSelected.add(new Vector2f(6.0f * ratioW, (1.0f + shift) * rationH));
        uvSelected.add(new Vector2f(6.0f * ratioW, (0.0f + shift) * rationH));
        uvSelected.add(new Vector2f(7.0f * ratioW, (0.0f + shift) * rationH));
        uvSelected.add(new Vector2f(7.0f * ratioW, (1.0f + shift) * rationH));

        shift += 1.0f;

        List<Vector2f> uvGreen = new ArrayList<>();
        uvGreen.add(new Vector2f(6.0f * ratioW, (1.0f + shift) * rationH));
        uvGreen.add(new Vector2f(6.0f * ratioW, (0.0f + shift) * rationH));
        uvGreen.add(new Vector2f(7.0f * ratioW, (0.0f + shift) * rationH));
        uvGreen.add(new Vector2f(7.0f * ratioW, (1.0f + shift) * rationH));

        shift += 1.0f;

        List<Vector2f> uvRed = new ArrayList<>();
        uvRed.add(new Vector2f(6.0f * ratioW, (1.0f + shift) * rationH));
        uvRed.add(new Vector2f(6.0f * ratioW, (0.0f + shift) * rationH));
        uvRed.add(new Vector2f(7.0f * ratioW, (0.0f + shift) * rationH));
        uvRed.add(new Vector2f(7.0f * ratioW, (1.0f + shift) * rationH));

        shift += 1.0f;

        List<Vector2f> uvNull = new ArrayList<>();
        uvNull.add(new Vector2f(6.0f * ratioW, (1.0f + shift) * rationH));
        uvNull.add(new Vector2f(6.0f * ratioW, (0.0f + shift) * rationH));
        uvNull.add(new Vector2f(7.0f * ratioW, (0.0f + shift) * rationH));
        uvNull.add(new Vector2f(7.0f * ratioW, (1.0f + shift) * rationH));

        Domino.setUvs(uvDown, uvGreen, uvRed, uvSelected, uvNull);
    }

    private Domino getDomino(int side1, int side2) {
        for (Domino domino : allDominoes) {
            if (domino.getSide1() == side1 && domino.getSide2() == side2) {
                return domino;
            }
        }

        return null;
    }

    private void preparePoolAndPlayers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < DOMINOES_AMOUNT; i++) {
            numbers.add(i);
        }

        List<Domino> player1Dominoes = new ArrayList<>();
        List<Domino> player2Dominoes = new ArrayList<>();
        List<Domino> pool = new ArrayList<>();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for (int i = 0; i < DOMINOES_AMOUNT; i++) {
            int number = Math.abs(random.nextInt()) % numbers.size();

            if (i < HAND_AMOUNT) {
                player1Dominoes.add(allDominoes.get(numbers.get(number)));
                allDominoes.get(numbers.get(number)).flipUp();
            } else if (i < 2 * HAND_AMOUNT) {
                player2Dominoes.add(allDominoes.get(numbers.get(number)));
                allDominoes.get(numbers.get(number)).flipDown();
            } else {
                pool.add(allDominoes.get(numbers.get(number)));
                allDominoes.get(numbers.get(number)).flipDown();
            }

            numbers.remove(number);
        }

        player1 = new HumanPlayer("Player", player1Dominoes, window, gameLayer);
        player2 = new AiPlayer("AI", player2Dominoes);
        Player.prepareTable(pool, fieldBlockSize * tilesPerBlock);

        positionPoolAndPlayers();
    }

    private void positionPoolAndPlayers() {
        player1.reposition();
        player2.reposition();
        Player.repositionPool();
    }

    private void keyboard() {
        if (System.currentTimeMillis() - lastKeyboard > keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
                lastKeyboard = System.currentTimeMillis();

                alive = false;
            }

            if (window.isKeyDown(GLFW_KEY_R)) {
                lastKeyboard = System.currentTimeMillis();

                restart();
            }

            if (window.isKeyDown(GLFW_KEY_W)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(0, 1));
            }

            if (window.isKeyDown(GLFW_KEY_A)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(-1, 0));
            }

            if (window.isKeyDown(GLFW_KEY_S)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(0, -1));
            }

            if (window.isKeyDown(GLFW_KEY_D)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(1, 0));
            }
        }
    }

    public void destroy() {

    }

    private void processEndOfGame() {
        gameEnded = true;

        int scorePlayer1 = player1.getScore();
        int scorePlayer2 = player2.getScore();

        System.out.println("GAME ENDED");
        System.out.println("SCORE:");
        System.out.println(player1.getName() + ": " + scorePlayer1);
        System.out.println(player2.getName() + ": " + scorePlayer2);

        if (scorePlayer1 > scorePlayer2) {
            System.out.println(player2.getName() + " WON!");
        } else if (scorePlayer1 < scorePlayer2) {
            System.out.println(player1.getName() + " WON!");
        } else {
            System.out.println("LET'S CALL IT A DRAW!");
        }
    }

    @Override
    public void update(float delta) {
        keyboard();

        if (!gameEnded) {
            if (player1Move) {
                if (player1.canMakeMove()) {
                    player1.makeMove();
                    if (player1.isMoveMade()) {
                        player1Move = false;
                        player1.endMove();
                    }
                } else {
                    if (player2.canMakeMove()) {
                        player1Move = false;
                    } else {
                        // End of game
                        processEndOfGame();
                    }
                }
            } else {
                if (player2.canMakeMove()) {
                    player2.makeMove();
                    if (player2.isMoveMade()) {
                        player1Move = true;
                        player2.endMove();
                    }
                } else {
                    if (player1.canMakeMove()) {
                        player1Move = true;
                    } else {
                        // End of game
                        processEndOfGame();
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        gameLayer.render();
    }
}