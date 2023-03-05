package org.tinger.data.test.pojo;

import lombok.Data;
import lombok.ToString;
import org.tinger.data.core.anno.TingerDataColumn;
import org.tinger.data.core.anno.TingerDataPrimaryKey;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1002468166060438698L;
    @TingerDataPrimaryKey
    private Long id;
    @TingerDataColumn("st")
    private Integer status;
    @TingerDataColumn("ct")
    private Date createTime;
    @TingerDataColumn("ut")
    private Date updateTime;
}