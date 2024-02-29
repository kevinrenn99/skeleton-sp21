package deque;

public class LinkedListDeque<T> {
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

    public void addFirst(T item) {
        firstSentinel.next = new Node<>(item, firstSentinel.next, firstSentinel);
        firstSentinel.next.next.previous = firstSentinel.next;
        size++;
    }

    public void addLast(T item) {
        lastSentinel.previous = new Node<>(item, lastSentinel, lastSentinel.previous);
        lastSentinel.previous.previous.next = lastSentinel.previous;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

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

}
