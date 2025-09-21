public class LinkedListDeque<Type> {

    private static class Node<Type> {
        public Type item;
        public Node<Type> previous;
        public Node<Type> next;
        public Node() {
            item = null;
            previous = null;
            next = null;
        }
        public Node(Type item, Node<Type> previous, Node<Type> next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
        public Node(Type item) {
            this.item = item;
            this.previous = null;
            this.next = null;
        }
        public Node(Node<Type> previous, Type item) {
            this.item = item;
            this.previous = previous;
            this.next = null;
        }
        public Node(Type item, Node<Type> next) {
            this.item = item;
            this.previous = null;
            this.next = next;
        }
    }

    private Node<Type> headSentinel;
    private Node<Type> endSentinel;
    private int size;
    public LinkedListDeque() {
        headSentinel = null;
        endSentinel = null;
        size = 0;
    }
    public LinkedListDeque(Type item) {
        headSentinel = new Node<>(item);
        endSentinel = headSentinel;
        size = 1;
    }
    public void addFirst(Type item) {
        headSentinel = new Node<>(item,headSentinel);
        if(size>0) {
            headSentinel.next.previous = headSentinel;
        } else {
            endSentinel = headSentinel;
        }
        size++;
    }
    public void addLast(Type item) {
        endSentinel = new Node<>(endSentinel,item);
        if(size>0) {
            endSentinel.previous.next = endSentinel;
        } else {
            headSentinel = endSentinel;
        }
        size++;
    }
    public boolean isEmpty() {
        return size==0;
    }
    public int size() {
        return size;
    }
    public void printDeque() {
        Node<Type> ptr = headSentinel;
        while (ptr!=null) {
            System.out.println(ptr.item);
            System.out.println(" ");
            ptr = ptr.next;
        }
        System.out.println();
    }
    public Type removeFirst() {
        if (headSentinel!=null) {
            Node<Type> ptr = headSentinel;
            if (size!=1) {
                headSentinel = headSentinel.next;
                headSentinel.previous = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }
            Type item = ptr.item;
            ptr.next = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }
    public Type removeLast() {
        if (endSentinel!=null) {
            Node<Type> ptr = endSentinel;
            if (size!=1) {
                endSentinel.previous = endSentinel;
                endSentinel.next = null;
            } else {
                headSentinel = null;
                endSentinel = null;
            }
            Type item = ptr.item;
            ptr.previous = null;
            ptr = null;
            size--;
            return item;
        } else {
            return null;
        }
    }
    public Type get(int index) {
        if(index<0||index>size) {
            return null;
        } else {
            int i = 0;
            Node<Type> ptr = headSentinel;
            while (i != index) {
                i++;
                ptr = ptr.next;
            }
            return ptr.item;
        }
    }
    public Type getRecursive(int index) {
        if(index<0||index>size) {
            return null;
        }
        return getRecursiveHelper(headSentinel,index);
    }
    private Type getRecursiveHelper(Node<Type> ptr, int index) {
        if (index == 0) {
            return ptr.item;
        }
        return getRecursiveHelper(ptr.next,index-1);
    }
}