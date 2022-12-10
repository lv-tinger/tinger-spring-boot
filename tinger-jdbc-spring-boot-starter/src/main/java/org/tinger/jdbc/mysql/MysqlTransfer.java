package org.tinger.jdbc.mysql;

import lombok.Builder;
import lombok.Getter;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcProperty;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.OperateValue;
import org.tinger.jdbc.queryable.Operation;
import org.tinger.jdbc.queryable.Update;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
public class MysqlTransfer {
    private final List<JdbcHandler<?>> jdbcHandlers = new LinkedList<>();
    private final List<Object> parameters = new LinkedList<>();
    private Criteria criteria;
    private Update update;
    private JdbcMetadata<?, ?> metadata;
    private String updateExpression;
    private String whereExpression;


    public MysqlTransfer resolve() {
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
        JdbcProperty property = this.metadata.getPropertyByName(name);
        switch (op) {
            case EQ:
            case GE:
            case GT:
            case LE:
            case LT:
            case NEQ:
                this.jdbcHandlers.add(property.getHandler());
                this.parameters.add(value);
                return property.getColumn() + " " + op.code + " ?";
            case NULL:
                return property.getColumn() + " IS NULL";
            case NON:
                return property.getColumn() + " IS NOT NULL";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void resolveUpdate() {

        if (this.update == null) {
            return;
        }

        Collection<String> columns = update.updateColumns();
        String[] updateColumns = new String[columns.size()];
        int i = 0;
        for (String column : columns) {
            JdbcProperty jdbcProperty = this.metadata.getPropertyByName(column);
            updateColumns[i] = jdbcProperty.getColumn() + " = ?";
            this.jdbcHandlers.add(jdbcProperty.getHandler());
        }

        this.updateExpression = StringUtils.join(updateColumns, ", ").trim();
    }

}
