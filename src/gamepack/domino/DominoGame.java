package gamepack.domino;

import gamepack.Button;
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
//    private Layer victoryLayer;

    private InfoWindow victoryWindow;
    private InfoWindow avaEntranceWindow;
    private InfoWindow controlsWindow;
    private InfoWindow pvpWindow;

    private final float victoryLayerZ = 0.2f;
    private final float avaEntranceLayerZ = 0.65f;
    private final float pvpWindowLayerZ = 0.35f;
    private final float menuLayerZ = 0.5f;
    private final float gameLayerDominoesZ = 0.0f;
    private final float gameLayerDeskZ = -0.3f;
    private final float gameLayerFieldZ = -0.5f;

    private Menu menu;
    private String currentMode = "pva";

    private final float maxTileSize = 2.0f;
    private final float minTileSize = 0.8f;
    private float tileSize = 1.4f;
    private final int fieldBlockSize = 20;
    private final int tilesPerBlock = 4;
    private Sprite field[][];

    private final int DOMINOES_AMOUNT = 28;
    private final int HAND_AMOUNT = 5;
    private List<Domino> allDominoes;

    private Texture dominoTexture;
    private Texture stuffTexture;
    private Vector2f dominoTextureSize;
    private Vector2f stuffTextureSize;

    private Sprite desk1;
    private Sprite desk2;
//    private Sprite deskPool;

    private Player player1;
    private Player player2;
    private Player currentPlayer;
//    private int movesMade;

    private boolean menuOpen = false;
    private boolean gameEnded = false;
    private boolean player1Move = true;

    public DominoGame(float width, float height, Window window) {
        super(width, height, window);

        gameLayer = new Layer();
        menuLayer = new Layer();

        field = new Sprite[fieldBlockSize][fieldBlockSize];

        allDominoes = new ArrayList<>();
        menu = new Menu();

        setup();

        restart(new HumanPlayer("RICHARD", window, gameLayer), new AiPlayer("ARNOLD"));
    }

    private void restart(Player player1, Player player2) {
        resetDominoesDirectionAndZ();
        fillAndPositionPoolAndPlayers(player1, player2);
        this.player1 = player1;
        this.player2 = player2;

        gameEnded = false;
        victoryWindow.setVisible(false);
        player1Move = true;
//        movesMade = 0;
        currentPlayer = player1Move ? player1 : player2;
        if (currentMode.equals("pvp")) {
            pvpWindow.setVisible(true);
            pvpWindow.setText(1, currentPlayer.getName());
        } else {
            pvpWindow.setVisible(false);
        }
    }

    private void setup() {
        prepareTextures();
        prepareVictoryBoard();
        prepareAvaEntrance();
        preparePvpWindow();
        prepareMenu();
        prepareField();
        prepareDesks();
        prepareDominoes();
    }

    private void prepareTextures() {
        stuffTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//stuff.png", Texture.TYPE_RGB);
        dominoTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//dominoes.png", Texture.TYPE_RGBA);

        dominoTextureSize = new Vector2f(448.0f, 896.0f);
        stuffTextureSize = new Vector2f(600.0f, 264.0f);
    }

    private void prepareVictoryBoard() {
        victoryWindow = new InfoWindow(new Vector3f(14.0f, 14.0f, victoryLayerZ - 0.05f), new Vector2f(32.0f, 32.0f),
                new Vector4f(0.0f, 0.0f, 0.0f, 0.8f));

        victoryWindow.addText("", new Vector2f(10.0f, 28.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 20);
        victoryWindow.addText("", new Vector2f(1.0f, 24.0f), new Vector4f(1.0f, 0.0f, 0.0f, 0.9f), 18);
        victoryWindow.addText("", new Vector2f(1.0f, 20.0f), new Vector4f(1.0f, 0.0f, 0.0f, 0.9f), 18);
        victoryWindow.addText("", new Vector2f(1.0f, 12.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 25);
    }

    private void prepareAvaEntrance() {
        avaEntranceWindow = new InfoWindow(
                new Vector3f(WIDTH / 8.0f, HEIGHT / 8.0f, avaEntranceLayerZ),
                new Vector2f(WIDTH / 4.0f * 3.0f, HEIGHT / 4.0f * 3.0f),
                new Vector4f(0.2f, 0.0f, 0.0f, 0.9f));

        avaEntranceWindow.addText("THIS MODE IS EXPERIMENTAL NOW",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 3.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
        avaEntranceWindow.addText("MOSTLY USED FOR AI DEBUGGING",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 6.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
        avaEntranceWindow.addText("USE IT AT YOUR OWN RISK",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 9.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
        avaEntranceWindow.addText("I DO NOT TAKE RESPONSIBILITY",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 12.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
        avaEntranceWindow.addText("FOR ANYTHING THAT YOU ENCOUNTER HERE",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 15.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);

        avaEntranceWindow.addText("SOME GAME SESSIONS MAY CRASH",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 19.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
        avaEntranceWindow.addText("BUT OTHERWISE IT IS AN AMAZING THING TO SEE",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 22.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);

        avaEntranceWindow.addText("PRESS ENTER IF YOU WISH TO PROCEED",
                new Vector2f(1.0f, HEIGHT / 4.0f * 3 - 26.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 12);
    }

    private void preparePvpWindow() {
        pvpWindow = new InfoWindow(
                new Vector3f(WIDTH / 6.0f, HEIGHT / 6.0f * 2.0f, pvpWindowLayerZ),
                new Vector2f(WIDTH / 3.0f * 2.0f, HEIGHT / 6.0f * 2.0f),
                new Vector4f(0.1f, 0.1f, 0.1f, 0.9f));

        pvpWindow.addText("PASS THE KEYBOARD TO",
                new Vector2f(1.0f, HEIGHT / 6.0f * 2.0f - 4.0f), new Vector4f(0.9f, 0.0f, 0.0f, 1.0f), 16);
        pvpWindow.addText("NAME",
                new Vector2f(1.0f, HEIGHT / 6.0f * 2.0f - 7.0f), new Vector4f(0.9f, 0.0f, 0.0f, 1.0f), 16);

        pvpWindow.setVisible(false);
    }

    private void prepareField() {
        for (int i = 0; i < fieldBlockSize; i++) {
            for (int j = 0; j < fieldBlockSize; j++) {
                field[i][j] = new Sprite(
                        new Vector3f(), new Vector2f(),
                        stuffTexture,
                        Game.spriteRenderer,
                        Game.spriteShader);

                List<Vector2f> uv = new ArrayList<>();
                Vector4f coords = new Vector4f(
                        512.0f / stuffTextureSize.x,
                        0.0f / stuffTextureSize.y,
                        (512.0f + 64.0f) / stuffTextureSize.x,
                        64.0f / stuffTextureSize.y);

                uv.add(new Vector2f(coords.x, coords.w));
                uv.add(new Vector2f(coords.x, coords.y));
                uv.add(new Vector2f(coords.z, coords.y));
                uv.add(new Vector2f(coords.z, coords.w));

                field[i][j].setUv(uv);

                gameLayer.add(field[i][j]);
            }
        }

        positionField(tileSize);
    }

    private void prepareDesks() {
        desk1 = new Sprite(new Vector3f(), new Vector2f(),
                stuffTexture, Game.spriteRenderer, Game.spriteShader);

        desk2 = new Sprite(new Vector3f(), new Vector2f(),
                stuffTexture, Game.spriteRenderer, Game.spriteShader);

        List<Vector2f> uv = new ArrayList<>();
        Vector4f coords = new Vector4f(
                0.0f / stuffTextureSize.x,
                0.0f / stuffTextureSize.y,
                512.0f / stuffTextureSize.x,
                64.0f / stuffTextureSize.y);

        uv.add(new Vector2f(coords.x, coords.w));
        uv.add(new Vector2f(coords.x, coords.y));
        uv.add(new Vector2f(coords.z, coords.y));
        uv.add(new Vector2f(coords.z, coords.w));

        desk1.setUv(uv);
        desk2.setUv(uv);

        positionDesks(tileSize);

        gameLayer.add(desk1);
        gameLayer.add(desk2);
    }

    private void prepareDominoes() {
        Domino.setDominoesTexture(dominoTexture);
        Domino.setTileWidth(tileSize);

        final float dominoWidth = 64.0f;
        final float dominoHeight = 2.0f * dominoWidth;

        for (int i = 6; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                List<Vector2f> uv = new ArrayList<>();
                Vector4f coords = new Vector4f(
                        (6 - i) * dominoWidth / dominoTextureSize.x,
                        (i - j) * dominoHeight / dominoTextureSize.y,
                        (6 - i + 1) * dominoWidth / dominoTextureSize.x,
                        (i - j + 1) * dominoHeight / dominoTextureSize.y);

                uv.add(new Vector2f(coords.x, coords.w));
                uv.add(new Vector2f(coords.x, coords.y));
                uv.add(new Vector2f(coords.z, coords.y));
                uv.add(new Vector2f(coords.z, coords.w));

                // Nice positioning in order to see everything
//                Vector3f position = new Vector3f((6 - i) * 8.0f, HEIGHT - (i - j) * 8.0f, gameLayerDominoesZ);

                Domino domino = new Domino(i, j, new Vector2i(i, j), gameLayerDominoesZ, Domino.DIRECTION.UP, uv);

                allDominoes.add(domino);

                gameLayer.add(domino);
            }
        }

        float ratioW = dominoWidth / dominoTextureSize.x;
        float rationH = dominoHeight / dominoTextureSize.y;
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

    private void fillAndPositionPoolAndPlayers(Player player1, Player player2) {
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
                allDominoes.get(numbers.get(number)).flipDown();
            } else if (i < 2 * HAND_AMOUNT) {
                player2Dominoes.add(allDominoes.get(numbers.get(number)));
                allDominoes.get(numbers.get(number)).flipDown();
            } else {
                pool.add(allDominoes.get(numbers.get(number)));
                allDominoes.get(numbers.get(number)).flipDown();
            }

            numbers.remove(number);
        }

        player1.setDominoes(player1Dominoes);
        player2.setDominoes(player2Dominoes);
        Player.prepareTable(pool, fieldBlockSize * tilesPerBlock, gameLayerFieldZ);

        positionPoolAndPlayers(player1, player2, tileSize);
    }

    private void prepareMenu() {
        final int amountOfButtons = 5;
        final Vector2f menuStart = new Vector2f(WIDTH / 6.0f, HEIGHT / 6.0f);
        final Vector2f menuSize = new Vector2f(WIDTH * 4.0f / 6.0f, HEIGHT * 4.0f / 6.0f);
        final float buttonMarginX = WIDTH / 6.0f / 2.0f;
//        final float buttonMarginY = HEIGHT / 6.0f / 2.0f;
        final float buttonZ = menuLayerZ + 0.05f;
        final Vector2f buttonUvStart = new Vector2f(0.0f / stuffTextureSize.x, 64.0f / stuffTextureSize.y);
        final Vector2f buttonUvSize = new Vector2f(300.0f / stuffTextureSize.x, 40.0f / stuffTextureSize.y);
        final float buttonSizeY = (menuSize.x - 2 * buttonMarginX)
                / (buttonUvSize.x * stuffTextureSize.x) * (buttonUvSize.y * stuffTextureSize.y);
        final float buttonMarginY = (menuSize.y - amountOfButtons * buttonSizeY) / (amountOfButtons + 1);

        List<Vector2f> buttonRestartUvActive = new ArrayList<>();
        List<Vector2f> buttonModePvpUvActive = new ArrayList<>();
        List<Vector2f> buttonModePvaUvActive = new ArrayList<>();
        List<Vector2f> buttonModeAvaUvActive = new ArrayList<>();
        List<Vector2f> buttonExitUvActive = new ArrayList<>();
        List<Vector2f> buttonRestartUvIdle = new ArrayList<>();
        List<Vector2f> buttonModePvpUvIdle = new ArrayList<>();
        List<Vector2f> buttonModePvaUvIdle = new ArrayList<>();
        List<Vector2f> buttonModeAvaUvIdle = new ArrayList<>();
        List<Vector2f> buttonExitUvIdle = new ArrayList<>();

        buttonRestartUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 0 * (buttonUvSize.y) + buttonUvSize.y));
        buttonRestartUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 0 * (buttonUvSize.y)));
        buttonRestartUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 0 * (buttonUvSize.y)));
        buttonRestartUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 0 * (buttonUvSize.y) + buttonUvSize.y));

        buttonRestartUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 0 * (buttonUvSize.y) + buttonUvSize.y));
        buttonRestartUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 0 * (buttonUvSize.y)));
        buttonRestartUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 0 * (buttonUvSize.y)));
        buttonRestartUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 0 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModePvpUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 1 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModePvpUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 1 * (buttonUvSize.y)));
        buttonModePvpUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 1 * (buttonUvSize.y)));
        buttonModePvpUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 1 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModePvpUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 1 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModePvpUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 1 * (buttonUvSize.y)));
        buttonModePvpUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 1 * (buttonUvSize.y)));
        buttonModePvpUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 1 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModePvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 2 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModePvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 2 * (buttonUvSize.y)));
        buttonModePvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 2 * (buttonUvSize.y)));
        buttonModePvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 2 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModePvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 2 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModePvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 2 * (buttonUvSize.y)));
        buttonModePvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 2 * (buttonUvSize.y)));
        buttonModePvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 2 * (buttonUvSize.y) + buttonUvSize.y));

        buttonExitUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 3 * (buttonUvSize.y) + buttonUvSize.y));
        buttonExitUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 3 * (buttonUvSize.y)));
        buttonExitUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 3 * (buttonUvSize.y)));
        buttonExitUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 3 * (buttonUvSize.y) + buttonUvSize.y));

        buttonExitUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 3 * (buttonUvSize.y) + buttonUvSize.y));
        buttonExitUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 3 * (buttonUvSize.y)));
        buttonExitUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 3 * (buttonUvSize.y)));
        buttonExitUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 3 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModeAvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 4 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModeAvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x), buttonUvStart.y + 4 * (buttonUvSize.y)));
        buttonModeAvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 4 * (buttonUvSize.y)));
        buttonModeAvaUvIdle.add(
                new Vector2f(buttonUvStart.x + 0 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 4 * (buttonUvSize.y) + buttonUvSize.y));

        buttonModeAvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 4 * (buttonUvSize.y) + buttonUvSize.y));
        buttonModeAvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x), buttonUvStart.y + 4 * (buttonUvSize.y)));
        buttonModeAvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 4 * (buttonUvSize.y)));
        buttonModeAvaUvActive.add(
                new Vector2f(buttonUvStart.x + 1 * (buttonUvSize.x) + buttonUvSize.x, buttonUvStart.y + 4 * (buttonUvSize.y) + buttonUvSize.y));

        Sprite menuBackground = new Sprite(
                new Vector3f(menuStart.x, menuStart.y, menuLayerZ),
                new Vector2f(menuSize.x, menuSize.y),
                new Vector4f(0.25f, 0.1f, 0.0f, 0.95f),
                Game.spriteRenderer,
                Game.spriteShader);

        menu.addButton(new Button(
                "Restart",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 5 * buttonMarginY + 4 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonRestartUvActive,
                buttonRestartUvIdle,
                false,
                Game.spriteRenderer,
                Game.spriteShader));
        menu.addButton(new Button(
                "ModePvp",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 4 * buttonMarginY + 3 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonModePvpUvActive,
                buttonModePvpUvIdle,
                false,
                Game.spriteRenderer,
                Game.spriteShader));
        menu.addButton(new Button(
                "ModePva",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 3 * buttonMarginY + 2 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonModePvaUvActive,
                buttonModePvaUvIdle,
                false,
                Game.spriteRenderer,
                Game.spriteShader));
        menu.addButton(new Button(
                "ModeAva",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 2 * buttonMarginY + 1 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonModeAvaUvActive,
                buttonModeAvaUvIdle,
                false,
                Game.spriteRenderer,
                Game.spriteShader));
        menu.addButton(new Button(
                "Exit",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 1 * buttonMarginY + 0 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonExitUvActive,
                buttonExitUvIdle,
                false,
                Game.spriteRenderer,
                Game.spriteShader));

        menu.setCurrent("Restart");

        menuLayer.add(menuBackground);
        menu.submitAll(menuLayer);
    }

    // ---+++---

    private void resetDominoesDirectionAndZ() {
        for (Domino domino : allDominoes) {
            domino.setDirection(Domino.DIRECTION.UP, true);
            domino.setNewPosition(new Vector3f(0.0f, 0.0f, gameLayerDominoesZ));
        }
    }

    private void positionDesks(float newTileSize) {
        int desk2Y = (int) ((HEIGHT - newTileSize * 5.0f) / newTileSize);

        desk1.setNewPosition(new Vector3f(0.0f, 0.5f * newTileSize, gameLayerDeskZ));
        desk2.setNewPosition(new Vector3f(0.0f, desk2Y * newTileSize - 0.5f * newTileSize, gameLayerDeskZ));

        desk1.setSize(new Vector2f(WIDTH, 5 * newTileSize));
        desk2.setSize(new Vector2f(WIDTH, 5 * newTileSize));
    }

    private void positionField(float newTileSize) {
        for (int i = 0; i < fieldBlockSize; i++) {
            for (int j = 0; j < fieldBlockSize; j++) {
                final float blockSize = 4 * newTileSize;

                field[i][j].setNewPosition(new Vector3f(i * blockSize, j * blockSize, gameLayerFieldZ));
                field[i][j].setSize(new Vector2f(blockSize, blockSize));
            }
        }
    }

    private void setNewTileSize(float newTileSize) {
        if (newTileSize >= maxTileSize || newTileSize < minTileSize) {
            return;
        }

        for (Domino domino : allDominoes) {
            Vector3f oldPosition = domino.getPosition();
            domino.setNewPosition(new Vector3f(
                    oldPosition.x / Domino.getTileWidth() * newTileSize,
                    oldPosition.y / Domino.getTileWidth() * newTileSize,
                    oldPosition.z));

            Vector2f oldSize = domino.getSize();
            domino.setSize(new Vector2f(
                    oldSize.x / Domino.getTileWidth() * newTileSize,
                    oldSize.y / Domino.getTileWidth() * newTileSize));
        }

        Vector3f pos = CurrentDomino.getMaskPosition();
        pos.x = pos.x / Domino.getTileWidth() * newTileSize;
        pos.y = pos.y / Domino.getTileWidth() * newTileSize;
        pos.z = pos.z / Domino.getTileWidth() * newTileSize;

        Vector2f size = CurrentDomino.getMaskSize();
        size.x = size.x / Domino.getTileWidth() * newTileSize;
        size.y = size.y / Domino.getTileWidth() * newTileSize;

        CurrentDomino.recalculateMaskParameters(pos, size);

        Domino.setTileWidth(newTileSize);

        positionField(newTileSize);
        positionPoolAndPlayers(player1, player2, newTileSize);
        positionDesks(newTileSize);

        tileSize = newTileSize;
    }

    private void positionPoolAndPlayers(Player player1, Player player2, float newTileSize) {
        Vector2i newPlayer1Start = new Vector2i(1, 1);
        Vector2i newPlayer2Start = new Vector2i(1, 0);
        Vector2i newPoolStart = new Vector2i(0, 0);
        Vector2i newPoolFinish = new Vector2i(0, 0);

        newPlayer2Start.y = (int) ((HEIGHT - newTileSize * 5.0f) / newTileSize);

        newPoolStart.x = (int) ((WIDTH * (5.0f / 8.0f)) / newTileSize);
        newPoolStart.y = (int) ((HEIGHT - 6.0f * newTileSize) / newTileSize);
        newPoolFinish.x = (int) ((WIDTH - 1.0f * newTileSize) / newTileSize);
        newPoolFinish.y = (int) ((6.0f * newTileSize) / newTileSize);

        player1.reposition(newPlayer1Start);
        player2.reposition(newPlayer2Start);
        Player.repositionTablePool(newPoolStart, newPoolFinish);
    }

    private void gameKeyboard() {
        if (System.currentTimeMillis() - lastKeyboard > keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
                lastKeyboard = System.currentTimeMillis();

                menuOpen = true;
            }

            if (window.isKeyDown(GLFW_KEY_W)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(0, -1));
            }

            if (window.isKeyDown(GLFW_KEY_A)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(1, 0));
            }

            if (window.isKeyDown(GLFW_KEY_S)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(0, 1));
            }

            if (window.isKeyDown(GLFW_KEY_D)) {
                lastKeyboard = System.currentTimeMillis();

                Player.shiftTable(new Vector2i(-1, 0));
            }

            if (window.isKeyDown(GLFW_KEY_EQUAL)) {
                lastKeyboard = System.currentTimeMillis();

                setNewTileSize(tileSize + 0.1f);
            }

            if (window.isKeyDown(GLFW_KEY_MINUS)) {
                lastKeyboard = System.currentTimeMillis();

                setNewTileSize(tileSize - 0.1f);
            }
        }
    }

    public void destroy() {

    }

    private void processEndOfGame() {
        gameEnded = true;
        player1.resetMoveMade();
        player2.resetMoveMade();
        pvpWindow.setVisible(false);

        player1.flipDominoesUp();
        player2.flipDominoesUp();

        int scorePlayer1 = player1.getScore();
        int scorePlayer2 = player2.getScore();

        victoryWindow.setText(0, "THE END");
        victoryWindow.setText(1, player1.getName() + ": " + scorePlayer1);
        victoryWindow.setText(2, player2.getName() + ": " + scorePlayer2);

        if (scorePlayer1 > scorePlayer2) {
            victoryWindow.setText(3, player2.getName() + " WON");
        } else if (scorePlayer1 < scorePlayer2) {
            victoryWindow.setText(3, player1.getName() + " WON");
        } else {
            victoryWindow.setText(3, "CALL IT A DRAW");
        }

        victoryWindow.setVisible(true);

//        System.out.println("GAME ENDED!");
//        System.out.println("SCORE:");
//        System.out.println(" --- " + player1.getName() + ": " + scorePlayer1);
//        System.out.println(" --- " + player2.getName() + ": " + scorePlayer2);

//        if (scorePlayer1 > scorePlayer2) {
//            System.out.println(player2.getName() + " WON!");
//        } else if (scorePlayer1 < scorePlayer2) {
//            System.out.println(player1.getName() + " WON!");
//        } else {
//            System.out.println("LET'S CALL IT A DRAW!");
//        }
    }

    private void menuKeyboard() {
        if (System.currentTimeMillis() - Game.lastKeyboard > Game.keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
                Game.lastKeyboard = System.currentTimeMillis();

                menuOpen = false;
            }

            if (window.isKeyDown(GLFW_KEY_UP)) {
                Game.lastKeyboard = System.currentTimeMillis();

                menu.moveUp();
            }

            if (window.isKeyDown(GLFW_KEY_DOWN)) {
                Game.lastKeyboard = System.currentTimeMillis();

                menu.moveDown();
            }

            if (window.isKeyDown(GLFW_KEY_ENTER)) {
                Game.lastKeyboard = System.currentTimeMillis();

                switch (menu.getCurrentButtonName()) {
                    case "Restart":
                        restart(player1, player2);
                        menu.setCurrent(0);
                        menuOpen = false;
                        break;
                    case "ModePvp":
//                            restart(new AiPlayer("JAMES"), new AiPlayer("BOB"));
                        CurrentDomino.setMaskVisible(true);
                        currentMode = "pvp";
                        restart(new HumanPlayer("JAMES", window, gameLayer), new HumanPlayer("BOB", window, gameLayer));
                        menu.setCurrent(0);
                        menuOpen = false;
                        pvpWindow.setVisible(true);
                        pvpWindow.setText(1, player1.getName());
                        break;
                    case "ModePva":
                        CurrentDomino.setMaskVisible(true);
                        currentMode = "pva";
                        restart(new HumanPlayer("RICHARD", window, gameLayer), new AiPlayer("ARNOLD"));
                        AiPlayer.setThinkingDurationSmall();
                        AiPlayer.showAiDominoes = false;
                        menu.setCurrent(0);
                        menuOpen = false;
                        break;
                    case "ModeAva":
                        if (avaEntranceWindow.isVisible()) {
                            avaEntranceWindow.setVisible(false);
                            currentMode = "ava";
                            restart(new AiPlayer("IGOR"), new AiPlayer("JARVIS"));
                            CurrentDomino.setMaskVisible(false);
                            AiPlayer.setThinkingDurationMedium();
                            AiPlayer.showAiDominoes = true;
                            menu.setCurrent(0);
                            menuOpen = false;

                            if (tileSize >= 1.2f) {
                                setNewTileSize(1.0f);
                            }
                        } else {
//                                menuOpen = false;
                            avaEntranceWindow.setVisible(true);
                        }
                        break;
                    case "Exit":
                        alive = false;
                        break;
                }
            }
        }
    }

    private void waitForPvpWindowToClose() {
        if (pvpWindow.isVisible() && currentMode.equals("pvp")) {
            if (System.currentTimeMillis() - Game.lastKeyboard > Game.keyboardMillisDelay) {
                if (window.isKeyDown(GLFW_KEY_ENTER)) {
                    Game.lastKeyboard = System.currentTimeMillis();

                    pvpWindow.setVisible(false);
                }
            }
        }
    }

    private void gameLogic() {
        if (!gameEnded) {
            if (currentMode.equals("pvp") && pvpWindow.isVisible()) {
                waitForPvpWindowToClose();
            } else {
                if (!currentPlayer.isMoveMade()) {
                    if (currentPlayer.canMakeMove()) {
                        if (currentPlayer.getType().equals(Player.TYPE.AI)) {
                            if (AiPlayer.showAiDominoes) {
                                currentPlayer.flipDominoesUp();
                            }
                        } else {
                            currentPlayer.flipDominoesUp();
                        }

                        Player theOtherPlayer = player1Move ? player2 : player1;
                        if (player1Move) {
                            if (theOtherPlayer.getAmount() == 0 && Player.getPoolSize() == 0) {
                                processEndOfGame();
                                return;
                            }
                        }

                        currentPlayer.makeMove();
                    } else {
                        if (currentPlayer.getAmount() == 0) {
                            currentPlayer.endMove();
                        } else {
                            Player theOtherPlayer = player1Move ? player2 : player1;
                            if (theOtherPlayer.canMakeMove()) {
                                currentPlayer.endMove();
                            } else {
                                processEndOfGame();
                                //return;
                            }
                        }
                    }
                } else {
                    currentPlayer.resetMoveMade();
                    currentPlayer.flipDominoesDown();
                    if (currentMode.equals("pvp")) {
                        CurrentDomino.setMaskVisible(false);
                    }

//                    if (player1Move && currentPlayer.getAmount() == 0) {
//                        // player2 makes 1 move and then END
//                    }
                    if (!player1Move) {
                        Player theOtherPlayer = player1;
                        if (currentPlayer.getAmount() == 0 || theOtherPlayer.getAmount() == 0) {
                            if (Player.getPoolSize() == 0) {
                                processEndOfGame();
                                return;
                            }
                        }
                    }

                    player1Move = !player1Move;
                    currentPlayer = player1Move ? player1 : player2;

                    if (currentMode.equals("pvp")) {
                        pvpWindow.setVisible(true);
                        pvpWindow.setText(1, currentPlayer.getName());
                    }
                }
            }
        }

//        if (!gameEnded) {
//            if (player1Move) {
//                if (player1.canMakeMove()) {
//                    player2.flipDominoesDown();
//
//                    if (player1.getType().equals(Player.TYPE.HUMAN)) {
//                        if (currentMode.equals("pvp")) {
//                            if (!pvpWindow.isVisible()) {
//                                player1.flipDominoesUp();
//                            }
//                        } else {
//                            player1.flipDominoesUp();
//                        }
//                    } else {
//                        if (AiPlayer.showAiDominoes) {
//                            player1.flipDominoesUp();
//                        }
//                    }
//                    player1.makeMove();
//                    if (player1.isMoveMade()) {
//                        player1Move = false;
//                        if (currentMode.equals("pvp")) {
//                            pvpWindow.setVisible(true);
//                            if (player2.canMakeMove()) {
//                                pvpWindow.setText(1, player2.getName());
//                            } else {
//                                // leave that name
//                            }
//                        }
//                        player1.resetMoveMade();
//                    }
//                } else {
//                    if (player2.canMakeMove()) {
//                        if (player1.getAmount() == 0) {
//                            if (player2.isMoveMade()) {
//                                processEndOfGame();
//                            } else {
//                                player2.makeMove();
//                            }
//                        } else {
//                            player1Move = false;
//                        }
//                    } else {
//                        // End of game
//                        processEndOfGame();
//                    }
//                }
//            } else {
//                if (player2.canMakeMove()) {
//                    player1.flipDominoesDown();
//
//                    if (player2.getType().equals(Player.TYPE.HUMAN)) {
//                        if (currentMode.equals("pvp")) {
//                            if (!pvpWindow.isVisible()) {
//                                player2.flipDominoesUp();
//                            }
//                        } else {
//                            player2.flipDominoesUp();
//                        }
//                    } else {
//                        if (AiPlayer.showAiDominoes) {
//                            player2.flipDominoesUp();
//                        }
//                    }
//                    player2.makeMove();
//                    if (player2.isMoveMade()) {
//                        player1Move = true;
//                        if (currentMode.equals("pvp")) {
//                            pvpWindow.setVisible(true);
//                            if (player1.canMakeMove()) {
//                                pvpWindow.setText(1, player1.getName());
//                            } else {
//                                // leave that name
//                            }
//                        }
//                        player2.resetMoveMade();
//                    }
//                } else {
//                    if (player1.canMakeMove()) {
//                        if (player2.getAmount() == 0) {
//                            if (player1.isMoveMade()) {
//                                processEndOfGame();
//                            } else {
//                                player1.makeMove();
//                            }
//                        } else {
//                            player1Move = true;
//                        }
//                    } else {
//                        // End of game
//                        processEndOfGame();
//                    }
//                }
//            }
//        }
    }

    @Override
    public void update(float delta) {
        if (menuOpen) {
            menuKeyboard();
        } else {
            gameKeyboard();
            gameLogic();
        }
    }

    @Override
    public void render() {
        gameLayer.render();

        victoryWindow.render();
        avaEntranceWindow.render();
        pvpWindow.render();

        if (menuOpen) {
            menuLayer.render();
        }
    }
}