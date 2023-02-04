package org.tinger.data.core.tsql;

import lombok.Builder;
import lombok.Data;

/**
 * Created by tinger on 2023-02-03
 */
@Data
@Builder
public class Updatable {
    private Update update;
    private Criteria criteria;
}
