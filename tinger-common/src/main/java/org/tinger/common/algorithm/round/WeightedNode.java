package org.tinger.common.algorithm.round;

import lombok.Getter;

public class WeightedNode<T> implements Comparable<WeightedNode<T>> {
    @Getter
    private int current = 0;
    @Getter
    private int weight = 0;
    @Getter
    private T node;

    public WeightedNode(T node, int weight) {
        this.weight = weight;
        this.node = node;
    }

    public WeightedNode<T> reset() {
        this.current = this.weight;
        return this;
    }

    public void incr() {
        this.current = this.current + weight;
    }

    @Override
    public int compareTo(WeightedNode<T> o) {
        return this.current - o.getCurrent();
    }

    public void decr(int total) {
        this.current -= total;
    }
}