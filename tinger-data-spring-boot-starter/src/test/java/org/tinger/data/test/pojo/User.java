package org.tinger.data.test.pojo;

import lombok.Data;
import org.tinger.data.core.anno.TingerDataColumn;
import org.tinger.data.core.anno.TingerDataPrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    @TingerDataPrimaryKey
    private Long id;
    @TingerDataColumn("st")
    private Integer status;
    @TingerDataColumn("ct")
    private Date createTime;
    @TingerDataColumn("ut")
    private Date updateTime;
}