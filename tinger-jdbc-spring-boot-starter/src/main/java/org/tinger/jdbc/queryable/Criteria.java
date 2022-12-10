package org.tinger.jdbc.queryable;

import lombok.Getter;

import java.util.*;

/**
 * Created by tinger on 2022-10-15
 */
public class Criteria {

    @Getter
    private Operation op;

    @Getter
    private List<Criteria> criteriaList;

    @Getter
    private Map<String, OperateValue> operationMapper;

    private Criteria() {
    }

    public static Criteria where(String column, Operation op, Object value) {
        Criteria criteria = new Criteria();
        criteria.operationMapper = new LinkedHashMap<>();
        OperateValue operateValue = new OperateValue();
        operateValue.put(op, value);
        criteria.operationMapper.put(column, operateValue);
        return criteria;
    }

    public static Criteria and(Criteria... criteria) {
        Criteria c = new Criteria();
        c.criteriaList = Arrays.asList(criteria);
        c.op = Operation.AND;
        return c;
    }

    public static Criteria or(Criteria... criteria) {
        Criteria c = new Criteria();
        c.criteriaList = Arrays.asList(criteria);
        c.op = Operation.OR;
        return c;
    }

    public Criteria and(String column, Operation op, Object value) {
        if (this.operationMapper == null) {
            throw new RuntimeException();
        }
        if (this.op == null) {
            this.op = Operation.AND;
        } else if (this.op != Operation.AND) {
            throw new RuntimeException();
        }

        attach(column, op, value);

        return this;
    }

    public Criteria or(String column, Operation op, Object value) {
        if (this.operationMapper == null) {
            throw new RuntimeException();
        }
        if (this.op == null) {
            this.op = Operation.OR;
        } else if (this.op != Operation.OR) {
            throw new RuntimeException();
        }

        attach(column, op, value);

        return this;
    }

    private void attach(String column, Operation op, Object value) {
        OperateValue operateValue = this.operationMapper.get(column);
        if (operateValue == null) {
            operateValue = new OperateValue();
            operateValue.put(op, value);
            this.operationMapper.put(column, operateValue);
        } else if (operateValue.containsValue(op)) {
            throw new RuntimeException();
        } else {
            operateValue.put(op, value);
        }
    }

    public Criteria and(Criteria criteria) {
        return this.attach(Operation.AND, criteria);
    }

    public Criteria or(Criteria criteria) {
        return this.attach(Operation.OR, criteria);
    }

    private Criteria attach(Operation op, Criteria criteria) {
        if (this.criteriaList == null) {
            Criteria c = new Criteria();
            c.criteriaList = new ArrayList<>();
            c.criteriaList.add(this);
            c.criteriaList.add(criteria);
            c.op = op;
            return this;
        } else if (this.op != op) {
            throw new RuntimeException();
        } else {
            this.criteriaList.add(criteria);
            return this;
        }
    }
}
