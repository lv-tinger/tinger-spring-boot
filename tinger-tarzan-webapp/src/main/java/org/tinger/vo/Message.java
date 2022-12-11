package org.tinger.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Message implements Serializable {
    private Integer code;
    private String info;
    private Object data;

    public Message success() {
        this.code = 200;
        return this;
    }

    public Message warring() {
        this.code = 300;
        return this;
    }

    public Message forbade() {
        this.code = 400;
        return this;
    }

    public Message failure() {
        this.code = 500;
        return this;
    }
}
