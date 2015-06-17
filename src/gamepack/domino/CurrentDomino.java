package gamepack.domino;

import himmel.graphics.layers.Layer;
import himmel.math.Vector3f;

/**
 * Created by Igor on 17-Jun-15.
 */
public class CurrentDomino {
    private Domino domino;
    private Domino mask;

    public CurrentDomino(Domino current) {
        domino = current;
        Vector3f pos = current.getPosition();
        mask = new Domino(0, 0, new Vector3f(pos.x, pos.y, pos.z + 0.1f), current.getDirection(), null);
        mask.uvMaskSelected();
    }

    public void setCurrentDomino(Domino current) {
        domino = current;
        Vector3f pos = current.getPosition();
        mask.setNewPosition(new Vector3f(pos.x, pos.y, pos.z + 0.1f));
        mask.setDirection(domino.getDirection());
    }

    public void submitMaskTo(Layer layer) {
        layer.add(mask);
    }

    public void maskNull() {
        mask.uvMaskNull();
    }

    public void maskGreen() {
        mask.uvMaskGreen();
    }

    public void maskRed() {
        mask.uvMaskRed();
    }

    public void maskSelected() {
        mask.uvMaskSelected();
    }

    public void moveUp() {
        mask.moveUp();
        domino.moveUp();
    }

    public void moveDown() {
        mask.moveDown();
        domino.moveDown();
    }

    public void moveLeft() {
        mask.moveLeft();
        domino.moveLeft();
    }

    public void moveRight() {
        mask.moveRight();
        domino.moveRight();
    }
}
