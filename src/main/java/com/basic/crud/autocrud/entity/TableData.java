package com.basic.crud.autocrud.entity;

import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TableData {
    private Integer total;//total record in current response
    private Integer totalCount;//total record in current search
    private Object[] data;
    private Integer page;
}
