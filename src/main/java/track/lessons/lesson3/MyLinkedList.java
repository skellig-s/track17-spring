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
    void add(int item) {
        if (lastIndex == 0) {
            enqueue(item);
        } else {
            push(item);
        }

    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx > lastIndex) {
            throw new NoSuchElementException();
        } else {
            Node current = first;
            for (int i = 0; i < idx; i++){
                current = current.next;
            }
            current.prev.next = current.next;
            current.next.prev = current.prev;
            return current.val;
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx > lastIndex) {
            throw new NoSuchElementException();
        } else {
            Node current = first;
            for (int i = 0; i < idx; i++){
                current = current.next;
            }
            return current.val;
        }
    }

    @Override
    int size() {
        return lastIndex;
    }


    public void enqueue(int value) {
        first = new Node(null, first, value);
        if (first.next != null) {
            first.next.prev = first;
        }
        lastIndex++;
    }

    public int dequeu() {
        int val = first.val;
        first.next.prev = null;
        Node newFirst = first.next;
        first.next = null;
        first = newFirst;
        lastIndex--;
        return val;
    } // вытащить первый элемент из очереди

    public void push(int value) {
        last = new Node(last, null, value);
        if (last.prev != null) {
            last.prev.next = last;
        }
    } // положить значение наверх стека

    public int pop(){
        int val = last.val;
        last.prev.next = null;
        Node newLast = last.prev;
        last.prev = null;
        last = newLast;
        return val;
    } // вытащить верхнее значение со стека
}
