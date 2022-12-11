package org.tinger.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Message<T> implements Serializable {
    private Integer code;
    private String info;
    private T data;

    public Message<T> success() {
        this.code = 200;
        return this;
    }

    public Message<T> warring() {
        this.code = 300;
        return this;
    }

    public Message<T> forbade() {
        this.code = 400;
        return this;
    }

    public Message<T> failure() {
        this.code = 500;
        return this;
    }
}
