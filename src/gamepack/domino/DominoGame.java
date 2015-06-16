package gamepack.domino;

import gamepack.Game;
import gamepack.Menu;
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

    private final int DOMINOES_AMOUNT = 28;
    private final int HAND_AMOUNT = 5;
    private List<Domino> allDominoes;
    private List<Domino> pool;
    private List<Domino> player1;
    private List<Domino> player2;

    //    private List<Texture> dominoTextures;
    private Texture tileTexture;
    private Map<Integer, List<Domino>> dominoes;

    public DominoGame(float width, float height, Window window) {
        super(width, height, window);

//        dominoTextures = new ArrayList<>();
        gameLayer = new Layer();
        menuLayer = new Layer();

        menu = new Menu();

        dominoes = new HashMap<>();
        tileTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//tile4_4.png", Texture.TYPE_RGB);

        prepareDominoes();
    }

    private void prepareDominoes() {
        Texture dominoesTexture = new Texture(
                System.getProperty("user.dir") + "//resources//domino//textures//dominoes2.png", Texture.TYPE_RGB);
        Domino.setDominoesTexture(dominoesTexture);
        final float imageWidth = 448.0f;
        final float imageHeight = 2.0f * imageWidth;
        final float dominoWidth = 64.0f;
        final float dominoHeight = 2.0f * dominoWidth;

        for (int i = 6; i >= 0; i--) {
            dominoes.put(i, new ArrayList<>());
            for (int j = i; j >= 0; j--) {
                List<Vector2f> uv = new ArrayList<>();
                Vector4f coords = new Vector4f(
                        (6 - i) * dominoWidth / imageWidth,
                        (i - j) * dominoHeight / imageHeight,
                        (6 - i + 1) * dominoWidth / imageWidth,
                        (i - j + 1) * dominoHeight / imageHeight);

//                uv.add(new Vector2f(coords.x, coords.w));
//                uv.add(new Vector2f(coords.x, coords.y));
//                uv.add(new Vector2f(coords.z, coords.y));
//                uv.add(new Vector2f(coords.z, coords.w));

                uv.add(new Vector2f(coords.x, coords.y));
                uv.add(new Vector2f(coords.x, coords.w));
                uv.add(new Vector2f(coords.z, coords.w));
                uv.add(new Vector2f(coords.z, coords.y));

                Vector3f position = new Vector3f((6 - i) * 8.0f, HEIGHT - (i - j) * 8.0f, gameLayerDominoesZ);
                Vector2f size = new Vector2f(5.0f * 0.7f, -10.0f * 0.7f);

                Domino domino = new Domino(i, j, position, size, uv);

                // TODO: decide what structure to use
                dominoes.get(i).add(domino);
                allDominoes.add(domino);

                gameLayer.add(domino);
            }
        }
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

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for (int i = 0; i < DOMINOES_AMOUNT; i++) {
            int number = random.nextInt() % numbers.size();

            if (i < HAND_AMOUNT) {
                player1.add(allDominoes.get(number));
            } else if (i < 2*HAND_AMOUNT){
                player2.add(allDominoes.get(number));
            } else {
                pool.add(allDominoes.get(number));
            }

            numbers.remove(number);
        }
    }

    @Override
    public void update(float delta) {
        keyboard();
    }

    @Override
    public void render() {
        gameLayer.render();
    }

    private void keyboard() {
        if (System.currentTimeMillis() - lastKeyboard > keyboardMillisDelay) {
            if (window.isKeyDown(GLFW_KEY_ESCAPE)) {
                lastKeyboard = System.currentTimeMillis();
                alive = false;
            }
        }
    }
}
