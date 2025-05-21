package com.basic.crud.autocrud.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DBApplicationList {
    private Integer totalCount;
    private List<DBApplication> entities;
}
