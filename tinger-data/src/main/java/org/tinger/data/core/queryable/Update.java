package org.tinger.data.core.queryable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tinger on 2022-10-16
 */
public class Update {
    private Map<String, Object> valueMapper = new LinkedHashMap<>();

    public static Update build(String name, Object value) {
        return new Update().set(name, value);
    }

    public Update set(String name, Object value) {
        this.valueMapper.put(name, value);
        return this;
    }

    public Update non(String name) {
        this.valueMapper.put(name, null);
        return this;
    }

    public Collection<String> updateColumns() {
        return this.valueMapper.keySet();
    }

    public Object updateValue(String name) {
        return this.valueMapper.get(name);
    }
}
