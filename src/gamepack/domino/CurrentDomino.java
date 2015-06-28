package gamepack.domino;

import himmel.graphics.layers.Layer;
import himmel.math.Vector3f;

/**
 * Created by Igor on 17-Jun-15.
 */
public class CurrentDomino {
    private Domino domino;
    private Domino mask;
    private boolean chosen = false;
    private final float currentDominoShift = 0.05f;
    private final float currentDominoMaskShift = 0.1f;

    public CurrentDomino(Domino current) {
        prepareMaskAndDomino(current);
    }

    private void prepareMaskAndDomino(Domino newDomino) {
        if (domino == null) { // If first time - prep mask
            mask = new Domino(0, 0, new Vector2i(0, 0), 0.0f, Domino.DIRECTION.UP, null);
            mask.uvMaskSelected();
        } else {
            domino.move(new Vector3f(0.0f, 0.0f, -currentDominoShift));
        }

//        Vector3f pos = newDomino.getPosition();
        domino = newDomino;
        domino.move(new Vector3f(0.0f, 0.0f, currentDominoShift));
        mask.setDirection(domino.getDirection(), true);
        mask.setNewPosition(new Vector3f(0.0f, 0.0f, currentDominoMaskShift));
//        mask.setNewPosition(new Vector3f(pos.x, pos.y, pos.z + currentDominoMaskShift));
        mask.setPositionCoord(domino.getPositionCoord().x, domino.getPositionCoord().y);
    }

    public Domino getDomino() {
        return domino;
    }

    public boolean isChosen() {
        return chosen;
    }

    private void restoreUvDirection() {
        mask.setDirection(Domino.DIRECTION.UP, true);
    }

    public void unChoose() {
        chosen = false;
        maskSelected();
    }

    public void setChosen() {
        chosen = true;
        maskGreen();
    }

    public void setCurrentDomino(Domino current) {
        prepareMaskAndDomino(current);
    }

    public void submitMaskTo(Layer layer) {
        layer.add(mask);
    }

    public void maskNull() {
        Domino.DIRECTION direction = mask.getDirection();
        restoreUvDirection();
        mask.uvMaskNull();
        mask.setDirection(direction, true);
    }

    public void maskGreen() {
        Domino.DIRECTION direction = mask.getDirection();
        restoreUvDirection();
        mask.uvMaskGreen();
        mask.setDirection(direction, true);
    }

    public void maskRed() {
        Domino.DIRECTION direction = mask.getDirection();
        restoreUvDirection();
        mask.uvMaskRed();
        mask.setDirection(direction, true);
    }

    public void maskSelected() {
        Domino.DIRECTION direction = mask.getDirection();
        restoreUvDirection();
        mask.uvMaskSelected();
        mask.setDirection(direction, true);
    }

    public void rotateClockWise() {
        domino.rotateClockWise();
        mask.rotateClockWise();
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
