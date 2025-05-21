package com.basic.crud.autocrud.entity;

import lombok.*;

@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TableInfo{
    private String tableName;
    private String columnName;
    private String dataType;
    private Long dataLength;
    private Boolean isNullable;
    private Boolean isPrimaryKey;
    private Boolean isForeignKey;
    private String defaultValue;
    private Integer ordinalPosition; //this is an index of the column in the database table
    //Applicable for Foreign Key
    private String foreignKeyTableName;
    private String foreignKeyTablePrimaryKey;
}


