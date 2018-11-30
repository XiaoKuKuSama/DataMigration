package com.ydx.datamigration.model.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础model 定义id  乐观锁
 */
@Data
public class BaseModel implements Serializable {

    //mybatis主键
    @TableId
    private Long id;
    //乐观锁
    @Version
    private Long optimistic = 0L;

}


