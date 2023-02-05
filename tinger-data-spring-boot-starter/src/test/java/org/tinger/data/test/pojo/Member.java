package org.tinger.data.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinger.data.core.anno.TingerDataColumn;
import org.tinger.data.core.anno.TingerDataPrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
    @TingerDataPrimaryKey
    private Long id;
    private Integer level;
    @TingerDataColumn("st")
    private Integer status;
    @TingerDataColumn("et")
    private Date expiryTime;
    @TingerDataColumn("ct")
    private Date createTime;
    @TingerDataColumn("ut")
    private Date updateTime;

}
