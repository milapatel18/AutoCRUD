package com.basic.crud.autocrud.controller;

import com.basic.crud.autocrud.entity.TableData;
import com.basic.crud.autocrud.entity.TableInfo;
import com.basic.crud.autocrud.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/table")
public class TablesController {

    @Autowired
    private TableService tableService;

    @PostMapping("/create/{databaseUniqueName}/{tableName}")
    public boolean createDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                     @PathVariable("tableName") String tableName,
                                        @RequestBody Map<String, Object> map){
        return  tableService.createNewTableData(databaseUniqueName,tableName,map);
    }

    @GetMapping("/list/{databaseUniqueName}/{tableName}")
    public TableData viewDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                        @PathVariable("tableName") String tableName){
        return tableService.getTableData(databaseUniqueName,tableName);
    }

    @GetMapping("/view/{databaseUniqueName}/{tableName}/{id}")
    public Map<String,Object> viewDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                        @PathVariable("tableName") String tableName,
                                        @PathVariable("id") Integer id){
        return tableService.getTableRecord(databaseUniqueName,tableName,id);
    }

    @GetMapping("/info/{databaseUniqueName}/{tableName}")
    public List<TableInfo> infoDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                              @PathVariable("tableName") String tableName){
        return tableService.getTableInfo(databaseUniqueName,tableName);
    }

    @GetMapping("/form/{databaseUniqueName}/{tableName}")
    public List<TableInfo> formDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                              @PathVariable("tableName") String tableName){
        return tableService.prepareFormDetails(databaseUniqueName,tableName);
    }

    @PutMapping("/edit/{databaseUniqueName}/{tableName}")
    public boolean editDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                  @PathVariable("tableName") String tableName,
                                        @RequestBody Map<String, Object> map){
        return  tableService.editTableData(databaseUniqueName,tableName,map);
    }

    @DeleteMapping("/delete/{databaseUniqueName}/{tableName}/{id}")
    public boolean deleteDbTableDetails(@PathVariable("databaseUniqueName") String databaseUniqueName,
                                     @PathVariable("tableName") String tableName,
                                     @PathVariable("id") Integer id){
        return  tableService.removeTableData(databaseUniqueName,tableName,id);
    }
}
