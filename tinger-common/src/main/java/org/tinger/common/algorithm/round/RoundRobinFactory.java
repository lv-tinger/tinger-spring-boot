package org.tinger.common.algorithm.round;

import java.util.List;

public class RoundRobinFactory {
    public static final String COMMON = "COMMON";
    public static final String STATIC_WEIGHT = "STATIC_WEIGHT";

    public static <T> RoundRobin<T> build(String impl, List<T> nodes, List<Integer> weights) {
        return switch (impl) {
            case COMMON -> new CommonRoundRobin<>(nodes);
            case STATIC_WEIGHT -> new StaticWeightedRoundRobin<>(nodes, weights);
            default -> throw new RuntimeException();
        };
    }
}
