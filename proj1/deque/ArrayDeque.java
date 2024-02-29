package deque;

import java.lang.reflect.Array;

public class ArrayDeque<T> {
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

    public void addFirst(T item) {
        if (array.length == size) {
            resize();
        }
        if (array[front] == null) {
            array[front] = item;
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

    public void addLast(T item) {
        if (array.length == size) {
            resize();
        }
        if (array[last] == null) {
            array[last] = item;
            size++;
            return;
        }
        int next = (last + 1) % array.length;
        array[next] = item;
        last = next;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[(front + i) % array.length] + " ");
        }
        System.out.println("");
    }

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

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return array[(index + front) % array.length];
    }

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
}
