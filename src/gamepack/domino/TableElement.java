package gamepack.domino;

/**
 * Created by Igor on 18-Jun-15.
 */
public class TableElement {
    private TableElement next;
    private TableElement prev;
    private Domino domino;

    public TableElement(Domino domino) {
        this.domino = domino;
    }

    public TableElement(Domino domino, TableElement prev, TableElement next) {
        this.domino = domino;
        this.prev = prev;
        this.next = next;
    }

    public Domino getDomino() {
        return domino;
    }

    public void setPrev(TableElement prev) {
        this.prev = prev;
    }

    public void setNext(TableElement next) {
        this.next = next;
    }

    public TableElement getPrev() {
        return prev;
    }

    public TableElement getNext() {
        return next;
    }
}
