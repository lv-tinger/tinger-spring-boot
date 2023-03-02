package org.tinger.common.algorithm.round;

import java.util.ArrayList;
import java.util.List;

public class StaticWeightedRoundRobin<T> extends CommonRoundRobin<T> {
    public StaticWeightedRoundRobin(List<T> list, List<Integer> weight) {
        super(round(list, weight));
    }

    private static <T> List<T> round(List<T> list, List<Integer> weight) {

        return new ArrayList<>();
    }
}
