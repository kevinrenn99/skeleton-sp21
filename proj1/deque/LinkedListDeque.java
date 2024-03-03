package deque;
import afu.org.checkerframework.checker.oigj.qual.O;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    public class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> previous;
        public Node(T item, Node<T> next, Node<T> previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }
    }

    private Node<T> firstSentinel;
    private Node<T> lastSentinel;
    private int size;

    public LinkedListDeque() {
        firstSentinel = new Node<>(null, null, null);
        lastSentinel = new Node<>(null, firstSentinel, firstSentinel);
        firstSentinel.next = lastSentinel;
        firstSentinel.previous = lastSentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        firstSentinel.next = new Node<>(item, firstSentinel.next, firstSentinel);
        firstSentinel.next.next.previous = firstSentinel.next;
        size++;
    }

    @Override
    public void addLast(T item) {
        lastSentinel.previous = new Node<>(item, lastSentinel, lastSentinel.previous);
        lastSentinel.previous.previous.next = lastSentinel.previous;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> n = firstSentinel;
        if (isEmpty()) {
            return;
        }
        for (int i = 0; i < size; i++) {
            n = n.next;
            System.out.print(n.item + " ");
        }
        System.out.println("");
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = firstSentinel.next.item;
        firstSentinel.next = firstSentinel.next.next;
        firstSentinel.next.previous = firstSentinel;
        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item = lastSentinel.previous.item;
        lastSentinel.previous = lastSentinel.previous.previous;
        lastSentinel.previous.next = lastSentinel;
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node<T> n = firstSentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.item;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(index, firstSentinel.next);
    }

    private T getRecursiveHelper(int index, Node<T> node) {
        if (index == 0) {
            return node.item;
        }
        return getRecursiveHelper(index - 1, node.next);
    }

    @Override
    public Iterator<T> iterator() {
        return new LLIterator();
    }

    private class LLIterator implements Iterator<T> {
        private Node<T> current;
        public LLIterator() {
            current = firstSentinel.next;
        }

        public boolean hasNext() {
            return current.item != null;
        }

        public T next() {
            T returnItem = current.item;
            current = current.next;
            return returnItem;
        }
    }
    /*
    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque other) {
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
