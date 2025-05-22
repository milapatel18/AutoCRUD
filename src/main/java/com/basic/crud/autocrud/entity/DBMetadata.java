package com.basic.crud.autocrud.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DBMetadata {
    private String dbName;
    private Date lastFetchTime;
    private List<TableInfo> tableInfo; // TableName and associated details for each column
    // #TODO Prepare an API in DatabaseController which sends details from the  in-memory object's
}
