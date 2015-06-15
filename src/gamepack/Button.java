package gamepack;

import himmel.graphics.Shader;
import himmel.graphics.Sprite;
import himmel.graphics.Texture;
import himmel.graphics.fonts.Text;
import himmel.graphics.layers.Layer;
import himmel.graphics.renderers.Renderer;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

/**
 * Created by Igor on 14-Jun-15.
 */
public class Button {
    private Sprite background;
    private Texture active;
    private Texture idle;
    private String name;

    private static Renderer renderer;
    private static Shader shader;

    public Button(String name, Vector3f position, Vector2f size, Texture active, Texture idle, boolean isActive) {
        this.name = name;
        this.active = active;
        this.idle = idle;
        background = new Sprite(position, size, isActive ? active : idle, renderer, shader);
    }

    public String getName() {
        return name;
    }

    public void setPosition(Vector3f position) {
        background.setNewPosition(position);
    }

    public void setSize(Vector2f size) {
        background.setSize(size);
    }

    public static void setRenderer(Renderer theRenderer) {
        renderer = theRenderer;
    }

    public static void setShader(Shader theShader) {
        shader = theShader;
    }

    public void submitTo(Layer layer) {
        layer.add(background);
    }

    public void setActive() {
        background.setTexture(active);
    }

    public void setIdle() {
        background.setTexture(idle);
    }
}
