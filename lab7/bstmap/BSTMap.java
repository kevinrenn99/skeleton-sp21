package bstmap;

import java.util.Iterator;
import java.util.Set;


public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;
    private int size;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public BSTMap() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        Node curr = root;
        while (curr != null) {
            if (key.compareTo(curr.key) < 0) {
                curr = curr.left;
            } else if (key.compareTo(curr.key) > 0) {
                curr = curr.right;
            } else {
                return true;
            }
        }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        Node curr = root;
        while (curr != null) {
            if (key.compareTo(curr.key) < 0) {
                curr = curr.left;
            } else if (key.compareTo(curr.key) > 0) {
                curr = curr.right;
            } else {
                return curr.val;
            }
        }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        size++;
    }

    private Node putHelper(K key, V value, Node root) {
        if (root == null) {
            return new Node(key, value);
        }
        if (key.compareTo(root.key) < 0) {
            root.left = putHelper(key, value, root.left);
        } else if (key.compareTo(root.key) > 0) {
            root.right = putHelper(key, value, root.right);
        } else {
            root.val = value;
        }
        return root;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("operation not supported");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("operation not supported");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("operation not supported");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("operation not supported");
    }

    public void printInOrder() {
        printHelper(root);
    }

    private void printHelper(Node root) {
        if (root == null) {
            return;
        }
        printHelper(root.left);
        printHelper(root.right);
        System.out.print(root.key + ", " + root.val + " ");
        return;
    }
}
