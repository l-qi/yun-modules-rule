package com.yun.rule.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 琪
 * 2023/7/27
 */
@Data
@NoArgsConstructor
public class Cnarea {

    /**
     *
     */
    private String id;
    /**
     * 层级
     */
    private String level;
    /**
     * 父级行政代码
     */
    private String parentCode;
    /**
     * 行政代码
     */
    private String areaCode;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     *
     */
    private String name;
    /**
     * 简称
     */
    private String shortName;
    /**
     * 组合名
     */
    private String mergerName;

    public Cnarea(String id, String level, String parentCode, String areaCode, String zipCode, String name, String shortName, String mergerName) {
        this.id = id;
        this.level = level;
        this.parentCode = parentCode;
        this.areaCode = areaCode;
        this.zipCode = zipCode;
        this.name = name;
        this.shortName = shortName;
        this.mergerName = mergerName;
    }
}
