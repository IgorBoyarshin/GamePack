package gamepack.domino;

import gamepack.Game;
import himmel.graphics.Sprite;
import himmel.graphics.fonts.Text;
import himmel.graphics.layers.Layer;
import himmel.math.Matrix4f;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 11-Jul-15.
 */
public class InfoWindow {
    private float zValue;
    private Layer layer;
    private boolean visible;
    private List<Text> texts;
    private Vector2f windowStart;

    public InfoWindow(Vector3f start, Vector2f size, Vector4f background) {
        zValue = start.z;
        windowStart = new Vector2f(start.x, start.y);
        visible = false;

        texts = new ArrayList<>();
        layer = new Layer();
        Sprite sprite = new Sprite(start, size, background, Game.spriteRenderer, Game.spriteShader);

        layer.add(sprite);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void render() {
        if (visible) {
            layer.render();
        }
    }

    public void setText(int id, String string) {
        texts.get(id).setText(string);
    }

    public void addText(String string, Vector2f localPosition, Vector4f color, int fontSize) {
        Text text = new Text("", fontSize, color);
        texts.add(text);

        text.setText(string);
        text.transform(Matrix4f.scaling(new Vector3f(1.2f, 1.0f, 1.0f))
                .translate(new Vector3f(windowStart.x + localPosition.x, windowStart.y + localPosition.y, zValue + 0.05f)));

        layer.add(text);
    }
}
