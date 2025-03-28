package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    private T findMax() {
        if (!isEmpty()) {
            Iterator<T> iterator = iterator();
            T max = get(0);
            while (iterator.hasNext()) {
                T next = iterator.next();
                if (comparator.compare(max, next) < 0) {
                    max = next;
                }
            }
            return max;
        }
        return null;
    }

    public T max() {
        return findMax();
    }

    public T max(Comparator<T> comparator) {
        return findMax();
    }
}
