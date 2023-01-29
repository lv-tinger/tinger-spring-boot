package org.tinger.data.core.tsql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Limited {
    private int skip;
    private int limit;
}
