package gamepack.domino;

import himmel.graphics.layers.Layer;
import himmel.math.Vector2f;
import himmel.math.Vector3f;

/**
 * Created by Igor on 17-Jun-15.
 */
public class CurrentDomino {
    private Domino domino;
    private static Domino mask = new Domino(0, 0, new Vector2i(0, 0), 0.0f, Domino.DIRECTION.UP, null);
    private boolean chosen = false;
    private final float currentDominoShift = 0.025f;
    private final float currentDominoMaskShift = 0.05f;

    public CurrentDomino(Domino current) {
//        prepareMaskAndDomino(current);
        setCurrentDomino(current);
    }

    public static void setMaskVisible(boolean visible) {
        if (visible) {
//            mask.uvMaskNull();
        } else {
//            mask.uvMaskSelected();
            mask.setNewPosition(new Vector3f(0.0f, 0.0f, -1.0f));
        }
    }

    // TODO: REWRITE!
//    private void prepareMaskAndDomino(Domino newDomino) {
//        if (newDomino == null) {
//            domino = null;
//            return;
//        }
//
//        if (domino == null) { // If first time - prep mask
////            mask = new Domino(0, 0, new Vector2i(0, 0), 0.0f, Domino.DIRECTION.UP, null);
//            mask.uvMaskSelected();
//        } else {
//            if (!domino.equals(newDomino)) {
//                domino.move(new Vector3f(0.0f, 0.0f, -currentDominoShift));
//            }
//        }
//
//        if (true) {
////        if (domino != newDomino) {
////        Vector3f pos = newDomino.getPosition();
//            domino = newDomino;
//            domino.move(new Vector3f(0.0f, 0.0f, currentDominoShift));
//            mask.setDirection(domino.getDirection(), true);
//            mask.setNewPosition(new Vector3f(0.0f, 0.0f, currentDominoMaskShift));
////        mask.setNewPosition(new Vector3f(pos.x, pos.y, pos.z + currentDominoMaskShift));
//            mask.setPositionCoord(domino.getPositionCoord().x, domino.getPositionCoord().y);
//        }
//    }

    public static void recalculateMaskParameters(Vector3f position, Vector2f newSize) {
        mask.setNewPosition(position);
        mask.setSize(newSize);
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

    public static Vector2f getMaskSize() {
        return mask.getSize();
    }

    public static Vector3f getMaskPosition() {
        return mask.getPosition();
    }

    public void setCurrentDomino(Domino current) {
        if (current == null) {
            maskNull();

            if(domino != null) {
                domino.move(new Vector3f(0.0f, 0.0f, -currentDominoShift));
            }
        } else {
            // TODO:
            if (true) {
//            if (!current.equals(domino)) {
                maskSelected();

                if(domino != null) {
                    domino.move(new Vector3f(0.0f, 0.0f, -currentDominoShift));
                }

                domino = current;

                domino.move(new Vector3f(0.0f, 0.0f, currentDominoShift));
                mask.setDirection(domino.getDirection(), true);
                mask.setNewPosition(new Vector3f(0.0f, 0.0f, currentDominoMaskShift));
                mask.setPositionCoord(domino.getPositionCoord().x, domino.getPositionCoord().y);
            }
        }
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
