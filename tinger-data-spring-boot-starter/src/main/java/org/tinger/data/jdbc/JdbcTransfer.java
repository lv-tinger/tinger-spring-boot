package org.tinger.data.jdbc;

import lombok.Builder;
import lombok.Getter;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.core.tsql.*;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
public class JdbcTransfer {
    private final List<JdbcHandler<?>> handlers = new LinkedList<>();
    private final List<Object> parameters = new LinkedList<>();
    private Criteria criteria;
    private Ordered ordered;
    private Update update;
    private TingerMetadata<?> metadata;
    private String updateExpression;
    private String whereExpression;
    private String orderExpression;

    public JdbcTransfer resolve() {
        resolveUpdate();
        resolveCriteria();
        return this;
    }

    private void resolveCriteria() {
        this.whereExpression = resolveCriteria(criteria).trim();
    }

    private String resolveCriteria(Criteria criteria) {
        if (CollectionUtils.isNotEmpty(criteria.getCriteriaList())) {
            List<String> strings = criteria.getCriteriaList().stream().map(x -> "(" + resolveCriteria(x) + ")").collect(Collectors.toList());
            return StringUtils.join(strings, " " + criteria.getOp().code + " ").trim();
        } else {
            return resolveSingletCriteria(criteria).trim();
        }
    }
    private String resolveSingletCriteria(Criteria criteria) {
        Map<String, OperateValue> mapper = criteria.getOperationMapper();
        List<String> strings = mapper.entrySet().stream().map(x -> {
            List<String> items = x.getValue().entrySet().stream().map(y -> resolve(x.getKey(), y.getKey(), y.getValue())).collect(Collectors.toList());
            if (items.size() == 1) {
                return items.get(0);
            } else {
                return StringUtils.join(items, " " + criteria.getOp().code + " ");
            }
        }).collect(Collectors.toList());
        if (strings.size() == 1) {
            return strings.get(0);
        } else {
            return StringUtils.join(strings, " " + criteria.getOp().code + " ");
        }
    }


    private String resolve(String name, Operation op, Object value) {
        TingerProperty property = this.metadata.getPropertyByName(name);
        switch (op) {
            case EQ, GE, GT, LE, LT, NEQ -> {
                this.handlers.add(JdbcHandlerHolder.getInstance().get(property.getType()));
                this.parameters.add(value);
                return column(property.getColumn()) + " " + op.code + " ?";
            }
            case NULL -> {
                return column(property.getColumn()) + " IS NULL ";
            }
            case NON -> {
                return column(property.getColumn()) + " IS NOT NULL ";
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    private void resolveUpdate() {
        if (this.update == null) {
            return;
        }

        Collection<String> columns = update.updateColumns();
        String[] updateColumns = new String[columns.size()];
        int i = 0;
        JdbcHandlerHolder handlerHolder = JdbcHandlerHolder.getInstance();
        for (String column : columns) {
            TingerProperty property = this.metadata.getPropertyByName(column);
            updateColumns[i] = column(column) + " = ?";
            this.handlers.add(handlerHolder.get(property.getType()));
        }

        this.updateExpression = StringUtils.join(updateColumns, ", ").trim();
    }

    private String column(String column) {
        return "`" + column + "`";
    }
}
