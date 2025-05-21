package com.basic.crud.autocrud.controller;

import com.basic.crud.autocrud.entity.DBApplication;
import com.basic.crud.autocrud.entity.DBApplicationList;
import com.basic.crud.autocrud.entity.DBMetadata;
import com.basic.crud.autocrud.service.DatabaseService;
import com.basic.crud.autocrud.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private TableService tableService;

    @PostMapping("/create")
    public DBApplication createDbApplication(@RequestBody DBApplication dbApplication){
        return databaseService.createDbApplication(dbApplication);
    }

    @GetMapping("/view/{uniqueName}")
    public DBApplication viewDbApplication(@PathVariable("uniqueName") String uniqueName){
        return databaseService.viewDbApplication(uniqueName);
    }

    @GetMapping("/meta/{uniqueName}")
    public DBMetadata viewDbMetadata(@PathVariable("uniqueName") String uniqueName){
        return tableService.getMetaData(uniqueName);
    }

    @GetMapping("/init/{uniqueName}")
    public DBMetadata initDbMetadata(@PathVariable("uniqueName") String uniqueName){
        databaseService.prepareDBApplication(uniqueName);
        return tableService.getMetaData(uniqueName);
    }

    @GetMapping("/list")
    public DBApplicationList viewDbApplication(){
        return databaseService.getDbApplicationList();
    }

    @GetMapping("/health")
    public String createDbApplication(){
        return "healthy";
    }
}
