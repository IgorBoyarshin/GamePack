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

import java.util.List;

/**
 * Created by Igor on 14-Jun-15.
 */
public class Button {
    private Sprite button;
    private Texture textureActive;
    private Texture textureIdle; // May be the same
    private List<Vector2f> uvActive;
    private List<Vector2f> uvIdle;
    private String name;

    public Button(String name, Vector3f position, Vector2f size,
                  Texture textureActive, Texture textureIdle,
                  List<Vector2f> uvActive, List<Vector2f> uvIdle, boolean isActive,
                  Renderer renderer, Shader shader) {
        this.name = name;
        this.uvActive = uvActive;
        this.uvIdle = uvIdle;
        this.textureActive = textureActive;
        this.textureIdle = textureIdle;

        button = new Sprite(position, size, isActive ? textureActive : textureIdle, renderer, shader);
        button.setUv(isActive ? uvActive : uvIdle);
    }

    public String getName() {
        return name;
    }

    public void setPosition(Vector3f position) {
        button.setNewPosition(position);
    }

    public void setSize(Vector2f size) {
        button.setSize(size);
    }

    public void submitTo(Layer layer) {
        layer.add(button);
    }

    public void setActive() {
        button.setTexture(textureActive);
        button.setUv(uvActive);
    }

    public void setIdle() {
        button.setTexture(textureIdle);
        button.setUv(uvIdle);
    }
}
