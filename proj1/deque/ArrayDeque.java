package deque;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] array;
    private int size;
    private int front;
    private int last;
    private static final int INITIAL_SIZE = 8;
    public ArrayDeque() {
        array = (T[]) new Object[INITIAL_SIZE];
        size = 0;
        front = 0;
        last = 0;
    }

    @Override
    public void addFirst(T item) {
        if (array.length == size) {
            resize();
        }
        if (array[front] == null) {
            array[front] = item;
            last = front;
            size++;
            return;
        }
        int previous = (front + array.length - 1) % array.length;
        array[previous] = item;
        front = previous;
        size++;
    }

    public void resize() {
        T[] resized = (T[]) new Object[size * 2];
        for (int i = 0; i < size; i++) {
            resized[i] = array[(front + i) % array.length];
        }
        array = resized;
        front = 0;
        last = size - 1;
    }

    @Override
    public void addLast(T item) {
        if (array.length == size) {
            resize();
        }
        if (array[last] == null) {
            array[last] = item;
            front = last;
            size++;
            return;
        }
        int next = (last + 1) % array.length;
        array[next] = item;
        last = next;
        size++;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[(front + i) % array.length] + " ");
        }
        System.out.println("");
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        double usageFactor = (size - 1.0) / array.length;
        if (array.length >= 16 && usageFactor < 0.25) {
            sizeDown();
        }
        T item = array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        double usageFactor = (size - 1.0) / array.length;
        if (array.length >= 16 && usageFactor < 0.25) {
            sizeDown();
        }
        T item = array[last];
        array[last] = null;
        last = (last + array.length - 1) % array.length;
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return array[(index + front) % array.length];
    }

    @Override
    public int size() {
        return size;
    }

    public void sizeDown() {
        T[] resized = (T[]) new Object[array.length / 2];
        for (int i = 0; i < size; i++) {
            resized[i] = array[(front + i) % array.length];
        }
        array = resized;
        front = 0;
        last = size - 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new AIterator(front);
    }

    private class AIterator implements Iterator<T> {
        private int index;
        public AIterator(int index) {
            this.index = index;
        }

        public boolean hasNext() {
            return get(index) != null;
        }

        public T next() {
            T returnItem = get(index);
            index++;
            return returnItem;
        }
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (o instanceof ArrayDeque other) {
            if (this.size() != other.size()) {
                return false;
            }
            Iterator<T> currentIterator = this.iterator();
            Iterator<T> otherIterator = other.iterator();
            while (currentIterator.hasNext() && otherIterator.hasNext()) {
                if (currentIterator.next() != otherIterator.next()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }*/
}
