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
import himmel.graphics.fonts.Text;
import himmel.graphics.layers.Layer;
import himmel.math.Matrix4f;
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
    private Layer victoryLayer;

    private final float victoryLayerZ = 0.3f;
    private final float menuLayerZ = 0.5f;
    private final float gameLayerDominoesZ = 0.0f;
    private final float gameLayerDeskZ = -0.3f;
    private final float gameLayerFieldZ = -0.5f;

    private Menu menu;

    private final float maxTileSize = 2.0f;
    private final float minTileSize = 1.0f;
    private float tileSize = 1.4f;
    private final int fieldBlockSize = 14;
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

    private Text victoryText;
    private Text victoryText2;
    private Text victoryText3;
    private Text victoryText4;

    private boolean menuOpen = false;
    private boolean gameEnded = false;
    private boolean player1Move = true;

    public DominoGame(float width, float height, Window window) {
        super(width, height, window);

        gameLayer = new Layer();
        menuLayer = new Layer();
        victoryLayer = new Layer();

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
        player1Move = true;
    }

    private void setup() {
        prepareTextures();
        prepareVictoryBoard();
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
        stuffTextureSize = new Vector2f(600.0f, 224.0f);
    }

    private void prepareVictoryBoard() {
        Sprite victoryBoard = new Sprite(new Vector3f(14.0f, 14.0f, victoryLayerZ - 0.05f), new Vector2f(32.0f, 32.0f),
                new Vector4f(0.0f, 0.0f, 0.0f, 0.8f),
                Game.spriteRenderer, Game.spriteShader);
        victoryLayer.add(victoryBoard);

        victoryText = new Text("", 20, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
        victoryText2 = new Text("", 18, new Vector4f(1.0f, 0.0f, 0.0f, 0.9f));
        victoryText3 = new Text("", 18, new Vector4f(1.0f, 0.0f, 0.0f, 0.9f));
        victoryText4 = new Text("", 25, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));

        victoryLayer.add(victoryText);
        victoryLayer.add(victoryText2);
        victoryLayer.add(victoryText3);
        victoryLayer.add(victoryText4);
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

        player1.setDominoes(player1Dominoes);
        player2.setDominoes(player2Dominoes);
        Player.prepareTable(pool, fieldBlockSize * tilesPerBlock, gameLayerFieldZ);

        positionPoolAndPlayers(player1, player2, tileSize);
    }

    private void prepareMenu() {
        final Vector2f menuStart = new Vector2f(WIDTH / 6.0f, HEIGHT / 6.0f);
        final Vector2f menuSize = new Vector2f(WIDTH * 4.0f / 6.0f, HEIGHT * 4.0f / 6.0f);
        final float buttonMarginX = WIDTH / 6.0f / 2.0f;
        final float buttonMarginY = HEIGHT / 6.0f / 2.0f;
        final float buttonZ = menuLayerZ + 0.05f;
        final Vector2f buttonUvStart = new Vector2f(0.0f / stuffTextureSize.x, 64.0f / stuffTextureSize.y);
        final Vector2f buttonUvSize = new Vector2f(300.0f / stuffTextureSize.x, 40.0f / stuffTextureSize.y);
        final float buttonSizeY = (menuSize.x - 2 * buttonMarginX)
                / (buttonUvSize.x * stuffTextureSize.x) * (buttonUvSize.y * stuffTextureSize.y);

        List<Vector2f> buttonRestartUvActive = new ArrayList<>();
        List<Vector2f> buttonModePvpUvActive = new ArrayList<>();
        List<Vector2f> buttonModePvaUvActive = new ArrayList<>();
        List<Vector2f> buttonExitUvActive = new ArrayList<>();
        List<Vector2f> buttonRestartUvIdle = new ArrayList<>();
        List<Vector2f> buttonModePvpUvIdle = new ArrayList<>();
        List<Vector2f> buttonModePvaUvIdle = new ArrayList<>();
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

        Sprite menuBackground = new Sprite(
                new Vector3f(menuStart.x, menuStart.y, menuLayerZ),
                new Vector2f(menuSize.x, menuSize.y),
                new Vector4f(0.25f, 0.1f, 0.0f, 0.95f),
                Game.spriteRenderer,
                Game.spriteShader);

        menu.addButton(new Button(
                "Restart",
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 4 * buttonMarginY + 3 * buttonSizeY, buttonZ),
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
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 3 * buttonMarginY + 2 * buttonSizeY, buttonZ),
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
                new Vector3f(menuStart.x + buttonMarginX, menuStart.y + 2 * buttonMarginY + 1 * buttonSizeY, buttonZ),
                new Vector2f(menuSize.x - 2 * buttonMarginX, buttonSizeY),
                stuffTexture,
                stuffTexture,
                buttonModePvaUvActive,
                buttonModePvaUvIdle,
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

    private void keyboard() {
        if (System.currentTimeMillis() - lastKeyboard > keyboardMillisDelay) {
//            if (window.isKeyDown(GLFW_KEY_Q)) {
//                lastKeyboard = System.currentTimeMillis();
//
//                alive = false;
//            }

            if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
                lastKeyboard = System.currentTimeMillis();

                menuOpen = true;
            }

//            if (window.isKeyDown(GLFW_KEY_R)) {
//                lastKeyboard = System.currentTimeMillis();
//
//                restart(player1, player2);
//            }

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

        player1.flipDominoesUp();
        player2.flipDominoesUp();

        int scorePlayer1 = player1.getScore();
        int scorePlayer2 = player2.getScore();

        victoryText.setText("THE END");
        victoryText2.setText(player1.getName() + ": " + scorePlayer1);
        victoryText3.setText(player2.getName() + ": " + scorePlayer2);

        if (scorePlayer1 > scorePlayer2) {
            victoryText4.setText(player2.getName() + " WON");
        } else if (scorePlayer1 < scorePlayer2) {
            victoryText4.setText(player1.getName() + " WON");
        } else {
            victoryText4.setText("CALL IT A DRAW");
        }

        victoryText.transform(Matrix4f.scaling(new Vector3f(1.2f, 1.0f, 1.0f))
                .translate(new Vector3f(24f, 42.0f, victoryLayerZ + 0.05f)));
        victoryText2.transform(Matrix4f.scaling(new Vector3f(1.2f, 1.0f, 1.0f))
                .translate(new Vector3f(15.0f, 38.0f, victoryLayerZ + 0.05f)));
        victoryText3.transform(Matrix4f.scaling(new Vector3f(1.2f, 1.0f, 1.0f))
                .translate(new Vector3f(15.0f, 34.0f, victoryLayerZ + 0.05f)));
        victoryText4.transform(Matrix4f.scaling(new Vector3f(1.2f, 1.0f, 1.0f))
                .translate(new Vector3f(15.0f, 26.0f, victoryLayerZ + 0.05f)));

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

    @Override
    public void update(float delta) {
        if (menuOpen) {
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
                            menuOpen = false;
                            break;
                        case "ModePvp":
//                            restart(new AiPlayer("JAMES"), new AiPlayer("BOB"));
//                            restart(new HumanPlayer("JAMES", window, gameLayer), new HumanPlayer("BOB", window, gameLayer));
//                            menuOpen = false;
                            break;
                        case "ModePva":
                            restart(new HumanPlayer("RICHARD", window, gameLayer), new AiPlayer("ARNOLD"));
                            menuOpen = false;
                            break;
                        case "Exit":
                            alive = false;
                            break;
                    }
                }
            }
        } else {
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
    }

    @Override
    public void render() {
        gameLayer.render();

        if (gameEnded) {
            victoryLayer.render();
        }

        if (menuOpen) {
            menuLayer.render();
        }
    }
}