package gamepack;

import himmel.graphics.Sprite;
import himmel.graphics.layers.Layer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;
import himmel.math.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Igor on 15-Jun-15.
 */
public class BackgroundAnimation {
    private final float WIDTH;
    private final float HEIGHT;
    private Layer layer;
    private final float z;

    private Sprite background;
    private List<Box> boxes;

    public BackgroundAnimation(float width, float height, Layer layer, float z) {
        WIDTH = width;
        HEIGHT = height;
        this.layer = layer;
        this.z = z;

        boxes = new ArrayList<>();

        Box.z = z;
        Box.maxX = width;
        Box.maxY = height;

        final float color = 1.0f;
        Sprite bg = new Sprite(
                new Vector3f(0.0f, 0.0f, z - 0.1f),
                new Vector2f(width, height),
                new Vector4f(color, color, color, 0.95f),
                Game.spriteRenderer, Game.spriteShader);

        layer.add(bg);

        populate();
    }

    private void populate() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        for (int i = 0; i < 5; i++) {
            float x = random.nextFloat() * WIDTH * 0.9f + 0.05f * WIDTH;
            float y = random.nextFloat() * HEIGHT * 0.9f + 0.05f * HEIGHT;
            final float speed = 14.0f;
            Vector2f velocity = new Vector2f(
                    (random.nextInt() % 2 == 0 ? 1.0f : -1.0f) * speed,
                    (random.nextInt() % 2 == 0 ? 1.0f : -1.0f) * speed);

            Box box = new Box(new Vector2f(x, y), velocity);
            box.submitTo(layer);
            boxes.add(box);
        }
    }

    public void update(float delta) {
        for (Box box : boxes) {
            box.update(delta);
        }
    }

    private static class Box {
        private static Vector4f color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

        public static float z = 0.0f;
        public static float maxX = 0.0f;
        public static float maxY = 0.0f;
        public static Vector2f size = new Vector2f(2.0f, 2.0f);
        private Vector2f position;
        private Vector2f velocity;
        private Sprite sprite;

        public Box(Vector2f position, Vector2f velocity) {
            this.position = position;
            this.velocity = velocity;

            sprite = new Sprite(
                    new Vector3f(position.x, position.y, z),
                    new Vector2f(size.x, size.y),
                    new Vector4f(color.x, color.y, color.z, color.w),
                    Game.spriteRenderer, Game.spriteShader);
        }

        public void submitTo(Layer layer) {
            layer.add(sprite);
        }

        public void update(float delta) {
            float shiftX = delta * velocity.x;
            float shiftY = delta * velocity.y;

            if (position.x + shiftX <= 0.0f || position.x + shiftX >= maxX) {
                velocity.x *= -1.0f;
                position.x -= shiftX;
            } else {
                position.x += shiftX;
            }

            if (position.y + shiftY <= 0.0f || position.y + shiftY >= maxY) {
                velocity.y *= -1.0f;
                position.y -= shiftY;
            } else {
                position.y += shiftY;
            }

            sprite.setNewPosition(new Vector3f(position.x, position.y, z));
        }
    }
}
