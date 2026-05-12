package tools.structs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CircledArray <T> implements Iterable<T>{
    private final Object[] buffer;
    private final int capacity;
    private int size = 0;
    private int offset = 0;

    public CircledArray(int capacity){
        buffer = new Object[capacity];
        this.capacity = capacity;
    }

    public synchronized void add(T entry){
        int index = (offset + size) % capacity;
        buffer[index] = entry;

        if (size < capacity) size++;
        else offset = (offset + 1) % capacity;
    }

    public synchronized int size(){
        return size;
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new CircledArrayIterator();
    }

    private class CircledArrayIterator implements Iterator<T>{
        private final List<T> snapshot = subList(0, size);
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < snapshot.size();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return snapshot.get(index++);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized List<T> subList(int startFrom, int indexTo){
        if (startFrom < 0 || indexTo < startFrom || indexTo > size){
            throw new IllegalArgumentException("");
        }
        List<T> result = new ArrayList<>(indexTo - startFrom);
        for (int i = startFrom; i < indexTo; i++)
            result.add((T) buffer[(offset + i) % capacity]);
        return result;
    }
}
