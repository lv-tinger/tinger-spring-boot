package org.tinger.data.jdbc.source.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public abstract class AbstractDataSourceConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 3776071168266011412L;
    private String name;
    private List<String> alias;
}