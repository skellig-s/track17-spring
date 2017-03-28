package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, Queue {

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */

    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node last;
    private Node first;
    private int lastIndex = 0;


    @Override
    public void add(int item) {
        push(item);
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if ((idx >= lastIndex) || (idx < 0)) {
            throw new NoSuchElementException();
        }
        Node current = first;
        if (idx == 0) {
            dequeu();
            return current.val;
        }
        for (int i = 0; i < idx; i++) {
            current = current.next;
        }
        if (current.prev != null && current.next != null) {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            lastIndex--;
        } else {
            pop();
        }
        return current.val;

    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if ((idx >= lastIndex) || (idx < 0)) {
            throw new NoSuchElementException();
        } else {
            Node current = first;
            for (int i = 0; i < idx; i++) {
                current = current.next;
            }
            return current.val;
        }
    }

    @Override
    public int size() {
        return lastIndex;
    }


    public void enqueue(int value) {
        Node newNode = new Node(null, first, value);
        if (first != null) {
            first.prev = newNode;
        } else {
            last = newNode;
        }
        first = newNode;

        lastIndex++;
    }

    public int dequeu() {
        if (lastIndex == 0) {
            throw new NoSuchElementException();
        }
        int val = first.val;
        if (lastIndex == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        lastIndex--;
        return val;
    } // вытащить первый элемент из очереди

    public void push(int value) {
        Node newNode = new Node(last, null, value);
        if (last != null) {
            last.next = newNode;
        } else {
            first = newNode;
        }
        last = newNode;
        lastIndex++;
    } // положить значение наверх стека

    public int pop() {
        if (lastIndex == 0) {
            throw new NoSuchElementException();
        }
        int val = last.val;
        if (lastIndex == 1) {
            last = null;
            first = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        lastIndex--;
        return val;
    } // вытащить верхнее значение со стека
}
