package track.lessons.lesson3;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private static final int defSize = 10;
    private int[] array;
    private int lastIndex;


    public MyArrayList() {
        this.array = new int[10];
        this.lastIndex = 9;

    }

    public MyArrayList(int capacity) {
        this.array = new int[capacity];
        this.lastIndex = capacity - 1;
    }

    @Override
    void add(int item) {
        if (this.lastIndex + 1 == this.array.length) {
            int[] buffArray = new int[array.length * 3 / 2 + 1];
            System.arraycopy(this.array, 0, buffArray, 0, this.array.length);
            array = buffArray;
        }
        this.lastIndex++;
        array[lastIndex] = item;

    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx > lastIndex){
            throw new NoSuchElementException();
        } else {
            int removingElement = array[idx];
            int[] buff = new int[array.length - 1];
            System.arraycopy(array, 0, buff, 0, idx);
            if (idx != lastIndex) {
                System.arraycopy(array, idx + 1, buff, idx, buff.length - idx);
            }
            array = buff;
            lastIndex--;
            return removingElement;
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx > this.lastIndex) {
            throw new NoSuchElementException();
        } else {
            return this.array[idx];
        }
    }

    @Override
    int size() {
        return lastIndex + 1;
    }
}
