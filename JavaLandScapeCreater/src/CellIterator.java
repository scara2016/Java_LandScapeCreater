import java.util.Iterator;

public class CellIterator implements Iterator<Cell>{

    Cell[][] data;
    int outer;
    int inner;
    boolean ended;

    public CellIterator(Cell[][] data){
        this.data = data;
        outer = 0;
        inner = 0;
        ended = false;
    }

    @Override
    public boolean hasNext() {
        return !ended;
    }

    @Override 
    public Cell next(){
        Cell ret = data[outer][inner];
        inner++;
        if (inner >= data[outer].length){
            inner = 0;
            outer++;
            if (outer >= data.length){
                ended = true;
            }
        }

        return ret;
    }
}
