package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> orgComparator;
    public MaxArrayDeque(Comparator<T> comparator) {
        super();
        orgComparator = comparator;
    }

    public T max() {
        if (!isEmpty()) {
            Iterator<T> iterator = iterator();
            T max = get(0);
            while (iterator.hasNext()) {
                T next = iterator.next();
                if (orgComparator.compare(max, next) < 0) {
                    max = next;
                }
            }
            return max;
        }
        return null;
    }

    public T max(Comparator<T> comparator) {
        if (!isEmpty()) {
            T max = get(0);
            Iterator<T> iterator = iterator();
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
}
