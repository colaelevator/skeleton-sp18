package lab9;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key,p.left);
        } else {
            return getHelper(key,p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key,root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key,value);
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key,value,p.left);
        } else {
            p.right = putHelper(key,value,p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key,value,root);
        size++;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    private Node removeHelper(K key,Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) < 0) {
            p.left = removeHelper(key,p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeHelper(key, p.right);
        } else if (p.left == null){
            return p.right;
        } else if (p.right == null) {
            return p.left;
        } else {
            p.right = swapSmallest(p.right,p);
        }
        return p;
    }

    private Node swapSmallest(Node p,Node q) {
        if (p.left == null) {
            q.key = p.key;
            q.value = p.value;
            return p.right;
        } else {
            p.left = swapSmallest(p.left,q);
            return p;
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V temp = get(key);
        root = removeHelper(key,root);
        return temp;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (value != get(key)) {
            return null;
        }
        root = removeHelper(key,root);
        return value;
    }

    private class InorderIterator<K> implements Iterator<K> {

        private Stack<Node> toDo = new Stack<>();

        private void pushLeft(Node root) {
            while (root != null) {
                toDo.push(root);
                root = root.left;
            }
        }

        public InorderIterator(Node root) {
            pushLeft(root);
        }

        @Override
        public boolean hasNext() {
            return !toDo.empty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node temp = toDo.pop();
            K k = (K) temp.key;
            if (temp.right != null) {
                pushLeft(temp.right);
            }
            return k;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new InorderIterator<>(root);
    }


}
