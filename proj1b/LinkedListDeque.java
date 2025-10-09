public class LinkedListDeque<T> implements Deque<T>{

    private static class Node<T> {
        private T item;
        private Node<T> previous;
        private Node<T> next;
        Node() {
            item = null;
            previous = null;
            next = null;
        }
        Node(T item, Node<T> previous, Node<T> next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
        Node(T item) {
            this.item = item;
            this.previous = null;
            this.next = null;
        }
        Node(Node<T> previous, T item) {
            this.item = item;
            this.previous = previous;
            this.next = null;
        }
        Node(T item, Node<T> next) {
            this.item = item;
            this.previous = null;
            this.next = next;
        }
    }

    private Node<T> headSentinel;
    private Node<T> endSentinel;
    private int size;
    public LinkedListDeque() {
        headSentinel = null;
        endSentinel = null;
        size = 0;
    }
    @Override
    public void addFirst(T item) {
        headSentinel = new Node<>(item,null, headSentinel);
        if (size > 0) {
            headSentinel.next.previous = headSentinel;
        } else {
            endSentinel = headSentinel;
        }
        size++;
    }
    @Override
    public void addLast(T item) {
        endSentinel = new Node<>(item, endSentinel,null);
        if (size > 0) {
            endSentinel.previous.next = endSentinel;
        } else {
            headSentinel = endSentinel;
        }
        size++;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        Node<T> ptr = headSentinel;
        while (ptr != null) {
            System.out.println(ptr.item);
            System.out.println(" ");
            ptr = ptr.next;
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (headSentinel != null) {
            Node<T> ptr = headSentinel;
            if (size != 1) {
                headSentinel = headSentinel.next;
                headSentinel.previous = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }
            T item = ptr.item;
            ptr.next = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }
    @Override
    public T removeLast() {
        if (endSentinel != null) {
            Node<T> ptr = endSentinel;
            if (size != 1) {
                endSentinel = endSentinel.previous;
                endSentinel.next = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }
            T item = ptr.item;
            ptr.previous = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            int i = 0;
            Node<T> ptr = headSentinel;
            while (i != index) {
                i++;
                ptr = ptr.next;
            }
            return ptr.item;
        }
    }
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(headSentinel, index);
    }
    private T getRecursiveHelper(Node<T> ptr, int index) {
        if (index == 0) {
            return ptr.item;
        }
        return getRecursiveHelper(ptr.next, index - 1);
    }
}
