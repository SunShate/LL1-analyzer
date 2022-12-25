package org.example.ll_one_analyzer;

import java.util.Objects;

public final class Pair<T, U> {

    private final T first;
    private final U second;

    /**
     * Pair's constructor
     * @param first pair item
     * @param second pair item
     */
    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
