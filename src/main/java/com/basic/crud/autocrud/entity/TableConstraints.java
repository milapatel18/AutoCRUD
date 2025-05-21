package com.basic.crud.autocrud.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableConstraints {
    private String dbName;
    private String tableName;
    private String tableColumn;
    private String referenceTableName;
    private String referenceTableColumn;
}
