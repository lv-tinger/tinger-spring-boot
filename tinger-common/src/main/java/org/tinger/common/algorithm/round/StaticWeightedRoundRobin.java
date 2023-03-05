package org.tinger.common.algorithm.round;

import org.tinger.common.utils.CollectionUtils;

import java.util.List;

public class StaticWeightedRoundRobin<T> extends CommonRoundRobin<T> {
    public StaticWeightedRoundRobin(List<T> list, List<Integer> weight) {
        super(round(list, weight));
    }

    private static <T> List<T> round(List<T> nodes, List<Integer> weights) {
        if (CollectionUtils.isEmpty(nodes) || CollectionUtils.isEmpty(weights) || nodes.size() != weights.size()) {
            throw new RuntimeException();
        }

        int length = nodes.size();
        WeightedNodeWrapper<T> weightedNodeWrapper = new WeightedNodeWrapper<>();
        for (int i = 0; i < length; i++) {
            weightedNodeWrapper.append(nodes.get(i), weights.get(i));
        }

        return weightedNodeWrapper.spread();
    }
}