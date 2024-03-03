package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (this.isEmpty()) {
            return null;
        }
        T max = this.get(0);
        for (T item : this) {
            if (comparator.compare(item, max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }
        T max = this.get(0);
        for (T item : this) {
            if (c.compare(item, max) > 0) {
                max = item;
            }
        }
        return max;
    }
}
