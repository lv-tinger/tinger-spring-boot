package org.tinger.data.jdbc.statement.template.queryable;

import lombok.Builder;
import lombok.Data;
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

/**
 * Created by tinger on 2023-02-03
 */
@Builder
@Getter
public class MysqlTransfer {
    private final List<JdbcHandler<?>> jdbcHandlers = new LinkedList<>();
    private final List<Integer> parameters = new LinkedList<>();
    private Criteria criteria;
    private Ordered ordered;
    private Update update;
    private Limited limited;

    private TingerMetadata<?> metadata;
    private String updateExpression;
    private String whereExpression;
    private String orderExpression;
    private String limitExpression;


    public MysqlTransfer resolve() {
        resolveUpdate();
        resolveCriteria();
        resolveOrdered();
        resolveLimited();
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
            Map<String, OperateValue> mapper = criteria.getOperationMapper();
            List<String> strings = mapper.entrySet().stream().map(x -> {
                List<String> items = x.getValue().entrySet().stream().map(y -> resolve(x.getKey(), y.getKey(), y.getValue())).collect(Collectors.toList());
                if (items.size() == 1) {
                    return items.get(0).trim();
                } else {
                    return StringUtils.join(items, " " + criteria.getOp().code + " ").trim();
                }
            }).collect(Collectors.toList());
            if (strings.size() == 1) {
                return strings.get(0).trim();
            } else {
                return StringUtils.join(strings, " " + criteria.getOp().code + " ").trim();
            }
        }
    }

    private String resolve(String name, Operation op, Integer value) {
        TingerProperty property = this.metadata.getPropertyByName(name);
        switch (op) {
            case EQ:
            case GE:
            case GT:
            case LE:
            case LT:
            case NEQ:
                JdbcHandler<?> handler = JdbcHandlerHolder.getInstance().get(property.getType());
                this.jdbcHandlers.add(handler);
                this.parameters.add(value);
                return column(property.getColumn()) + " " + op.code + " ?";
            case NULL:
                return column(property.getColumn()) + " IS NULL";
            case NON:
                return column(property.getColumn()) + " IS NOT NULL";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void resolveUpdate() {
        if (this.update == null || this.update.getValueMapper().isEmpty()) {
            return;
        }
        String[] updateColumns = new String[this.update.getValueMapper().size()];
        int i = 0;
        for (Map.Entry<String, Integer> item : this.update.getValueMapper().entrySet()) {
            TingerProperty property = this.metadata.getPropertyByName(item.getKey());
            updateColumns[i] = column(property.getColumn()) + " = ?";
            JdbcHandler<?> handler = JdbcHandlerHolder.getInstance().get(property.getType());
            this.jdbcHandlers.add(handler);
            this.parameters.add(item.getValue());
            i++;
        }
        this.updateExpression = StringUtils.join(updateColumns, ", ").trim();
    }

    private void resolveOrdered() {
        this.orderExpression = resolveOrdered(this.ordered);
    }

    private String resolveOrdered(Ordered ordered) {
        if (ordered == null) {
            return null;
        }

        if (CollectionUtils.isNotEmpty(ordered.getOrdering())) {
            List<String> strings = ordered.getOrdering().stream().map(this::resolveOrdered).collect(Collectors.toList());
            return StringUtils.join(strings, ", ");
        } else {
            List<String> strings = ordered.getProperties().stream().map(x -> this.metadata.getPropertyByName(x)).map(TingerProperty::getColumn).collect(Collectors.toList());
            return StringUtils.join(strings, ", ") + " " + ordered.getOperation().code;
        }
    }

    private void resolveLimited() {
        if (this.limited == null) {
            return;
        }

        if (limited.isFix()) {
            this.limitExpression = this.limited.getSkip() + ", " + this.limited.getTake();
        } else {
            this.limitExpression = "?, ?";
            JdbcHandler<Integer> handler = JdbcHandlerHolder.getInstance().get(Integer.class);
            this.jdbcHandlers.add(handler);
            this.jdbcHandlers.add(handler);
            this.parameters.add(this.limited.getSkip(), this.limited.getTake());
        }
    }

    private String column(String column) {
        return "`" + column + "`";
    }
}
