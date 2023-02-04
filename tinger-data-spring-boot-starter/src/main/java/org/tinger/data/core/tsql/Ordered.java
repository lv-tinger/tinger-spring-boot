package org.tinger.data.core.tsql;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tinger on 2022-10-15
 */
@Getter
public class Ordered {
    private List<String> properties;
    private Operation operation;
    private List<Ordered> ordering;

    private Ordered() {
    }

    public static Ordered ordered(Operation operation, String... properties) {
        Ordered ordered = new Ordered();
        ordered.operation = operation;
        ordered.properties = Arrays.asList(properties);
        return ordered;
    }

    public static Ordered ordered(Operation operation, Ordered... ordering) {
        Ordered ordered = new Ordered();
        ordered.operation = operation;
        ordered.ordering = Arrays.asList(ordering);
        return ordered;
    }

}
