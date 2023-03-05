package org.tinger.common.algorithm.round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WeightedNodeWrapper<T> {
    private final List<WeightedNode<T>> weightedNodes = new LinkedList<>();
    private int totalWeight;
    public WeightedNodeWrapper<T> append(T node, int weight) {
        WeightedNode<T> weightedNode = new WeightedNode<>(node, weight);
        this.weightedNodes.add(weightedNode);
        return this;
    }

    public List<T> spread() {
        if (weightedNodes.isEmpty()) {
            return Collections.emptyList();
        }

        this.weightedNodes.forEach(WeightedNode::reset);
        List<Integer> weights = this.weightedNodes.stream().map(WeightedNode::getWeight).toList();
        for (Integer weight : weights) {
            totalWeight += weight;
        }
        List<T> list = new ArrayList<>(this.totalWeight);
        for (int i = 0; i < this.totalWeight; i++) {
            this.weightedNodes.forEach(WeightedNode::incr);
            WeightedNode<T> node = this.weightedNodes.stream().max(WeightedNode::compareTo).orElseThrow();
            list.add(node.getNode());
            node.decr(totalWeight);
        }
        return list;
    }
}