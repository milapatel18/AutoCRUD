package com.basic.crud.autocrud.service;


import com.basic.crud.autocrud.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    // #TODO : For MYSQL like other databases, This service implementation will be changed.

    private static Map<String, DBMetadata> connectionMap = new HashMap<>();

    @Autowired
    private DBConnectionService dbConnectionService;

    public TableService(DBConnectionService dbConnectionService) {
        this.dbConnectionService = dbConnectionService;
    }

    public void prepareDatabaseMetadata(String dbUniqueName, String dbName){
        try {
            Connection connection=dbConnectionService.getConnection(dbUniqueName);
            //Collect the constraints - START
            PreparedStatement ps2=connection.prepareStatement("SELECT CONSTRAINT_NAME, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME," +
                    "REFERENCED_TABLE_SCHEMA, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                    "FROM information_schema.key_column_usage " +
                    "WHERE TABLE_SCHEMA=?");
            ps2.setString(1,dbName);
            List<TableConstraints> tableConstraintsList=new ArrayList<>();
            try (ResultSet rs = ps2.executeQuery()) {
                while(rs.next()){
                    if(!rs.getString("CONSTRAINT_NAME").equals(DataUtils.PRIMARY_TEXT)){
                        TableConstraints tableConstraints=new TableConstraints();
                        tableConstraints.setTableColumn(rs.getString("COLUMN_NAME"));
                        tableConstraints.setTableName(rs.getString("TABLE_NAME"));
                        tableConstraints.setReferenceTableColumn(rs.getString("REFERENCED_COLUMN_NAME"));
                        tableConstraints.setReferenceTableName(rs.getString("REFERENCED_TABLE_NAME"));
                        tableConstraints.setDbName(rs.getString("TABLE_SCHEMA"));
                        tableConstraintsList.add(tableConstraints);
                    }
                }
            }
            ps2.close();
            //Collect the constraints - END   // Can be set in another function #TODO

            PreparedStatement ps=connection.prepareStatement("SELECT *    FROM information_schema.columns " +
                    "WHERE (table_schema=?)   order by TABLE_NAME,ORDINAL_POSITION");
            ps.setString(1,dbName);
            DBMetadata dbMetadata=new DBMetadata();
            try (ResultSet rs = ps.executeQuery()) {
                dbMetadata.setDbName(dbName);
                dbMetadata.setLastFetchTime(new Date());
                List<TableInfo> tableInfoList=new ArrayList<>();
                while(rs.next()){
                    TableInfo tableInfo=new TableInfo();
                    tableInfo.setTableName(rs.getString("TABLE_NAME"));
                    tableInfo.setColumnName(rs.getString("COLUMN_NAME"));
                    tableInfo.setDataType(rs.getString("DATA_TYPE"));
                    tableInfo.setDataLength(rs.getString("CHARACTER_MAXIMUM_LENGTH")==null?0:rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
                    tableInfo.setIsNullable(!rs.getString("IS_NULLABLE").equals(DataUtils.NO));
                    tableInfo.setIsForeignKey(!rs.getString("COLUMN_KEY").equals(DataUtils.PRIMARY_KEY));
                    tableInfo.setIsPrimaryKey(rs.getString("COLUMN_KEY").equals(DataUtils.PRIMARY_KEY));
                    tableInfo.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
                    tableInfo.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
                    //Managing the foreign key - START    << This can be decorated in another function - #TODO
                    if(tableInfo.getIsForeignKey()){
                        TableConstraints tableConst = tableConstraintsList.stream()
                                .filter(t->tableInfo.getColumnName().equals(t.getTableColumn()))
                                .filter(t->tableInfo.getTableName().equals(t.getTableName()))
                                .findAny()
                                .orElse(null);
                        if(tableConst!=null){
                            tableInfo.setForeignKeyTableName(tableConst.getReferenceTableName());
                            tableInfo.setForeignKeyTablePrimaryKey(tableConst.getReferenceTableColumn());
                        }
                    }
                    //Managing the foreign key - END
                    tableInfoList.add(tableInfo);
                }
                dbMetadata.setTableInfo(tableInfoList);
                dbMetadata.setTableList(tableInfoList.stream().map(TableInfo::getTableName).collect(Collectors.toSet()).stream().toList());
            }
            ps.close();
            connectionMap.put(dbUniqueName,dbMetadata);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public TableData getTableData(String dbUniqueName, String tableName){
        try {
            try (ResultSet rs = dbConnectionService.getConnection(dbUniqueName)
                    .prepareStatement("SELECT * FROM " + tableName).executeQuery()) {
                List<TableInfo> tableInfoList=connectionMap.get(dbUniqueName).getTableInfo().stream()
                        .filter(obj -> obj.getTableName().equals(tableName))
                        .toList();
                List<Map<String, Object>> rows=new ArrayList<>();
                Object[] objects=new Object[tableInfoList.size()];
                if(rs.getMetaData().getColumnCount()==tableInfoList.size()){
                    while(rs.next()){
                        Map<String, Object> map=new HashMap<>();
                        for(TableInfo t:tableInfoList){
                            switch (t.getDataType()) {
                                case "varchar" -> map.put(t.getColumnName(), rs.getString(t.getColumnName()));
                                case "text" -> map.put(t.getColumnName(), rs.getString(t.getColumnName()));
                                case "datetime" ->map.put(t.getColumnName(), rs.getDate(t.getColumnName()));
                                case "date" -> map.put(t.getColumnName(), rs.getDate(t.getColumnName()));
                                case "bigint" -> map.put(t.getColumnName(), rs.getLong(t.getColumnName()));
                                case "tinyint" -> map.put(t.getColumnName(), rs.getInt(t.getColumnName()));
                                case "int" -> map.put(t.getColumnName(), rs.getInt(t.getColumnName()));
                                default -> {
                                }
                            }
                        }
                        rows.add(map);
                    }

                }else{
                    // #TODO Reload prepareDatabaseMetadata??
                }
                TableData tableData=new TableData();
                tableData.setData(rows.toArray());
                tableData.setPage(1);// #TODO For Now only
                tableData.setTotal(objects.length);
                tableData.setTotalCount(objects.length);// #TODO For Now only
                return tableData;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,Object> getTableRecord(String dbUniqueName, String tableName, Integer id){
        try {
            try (ResultSet rs = dbConnectionService.getConnection(dbUniqueName)
                    .prepareStatement("SELECT * FROM " + tableName+" WHERE id="+id).executeQuery()) {
                List<TableInfo> tableInfoList=connectionMap.get(dbUniqueName).getTableInfo().stream()
                        .filter(obj -> obj.getTableName().equals(tableName))
                        .toList();
                List<Map<String, Object>> rows=new ArrayList<>();
                Object[] objects=new Object[tableInfoList.size()];
                Map<String, Object> map=new HashMap<>();
                if(rs.getMetaData().getColumnCount()==tableInfoList.size()){
                    while(rs.next()){
                        for(TableInfo t:tableInfoList){
                            switch (t.getDataType()) {
                                case "varchar" -> map.put(t.getColumnName(), rs.getString(t.getColumnName()));
                                case "text" -> map.put(t.getColumnName(), rs.getString(t.getColumnName()));
                                case "datetime" ->map.put(t.getColumnName(), rs.getDate(t.getColumnName()));
                                case "date" -> map.put(t.getColumnName(), rs.getDate(t.getColumnName()));
                                case "bigint" -> map.put(t.getColumnName(), rs.getLong(t.getColumnName()));
                                case "tinyint" -> map.put(t.getColumnName(), rs.getInt(t.getColumnName()));
                                case "int" -> map.put(t.getColumnName(), rs.getInt(t.getColumnName()));
                                default -> {
                                }
                            }
                        }
                    }
                }else{
                    // #TODO Reload prepareDatabaseMetadata??
                }
                return map;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean createNewTableData(String dbUniqueName, String tableName, Map<String, Object> map) {
        List<TableInfo> tableInfoList=connectionMap.get(dbUniqueName).getTableInfo().stream()
                .filter(obj -> obj.getTableName().equals(tableName))
                .toList();

        // expected primary key is always 0
        // created_at/ updated_at always NOW(),
        String columns = "(";
        String values = "(";
        boolean first=true;
        for(TableInfo t:tableInfoList){
            if(!first){
                columns = columns.concat(",");
                values = values.concat(",");
            }
            first = false;
            columns = columns.concat(t.getColumnName());
            // #TODO These three if conditions below, can be converted to the config module
            if(t.getColumnName().equals("id")){
                values = values.concat("0");
                continue;
            }
            if(t.getColumnName().equals("created_at")){
                values = values.concat("NOW()");
                continue;
            }
            if(t.getColumnName().equals("updated_at")){
                values = values.concat("NOW()");
                continue;
            }
            switch (t.getDataType()) { //#TODO this will be converted to the preparedStatement
                case "varchar" -> values = values.concat("'"+map.get(t.getColumnName()).toString()+"'");
                case "text" -> values = values.concat("'"+map.get(t.getColumnName()).toString()+"'");
                case "datetime" -> values = values.concat("'"+map.get(t.getColumnName()).toString()+"'");//#TODO formatting required
                case "date" -> values = values.concat("'"+map.get(t.getColumnName()).toString()+"'");//#TODO formatting required
                case "bigint" -> values = values.concat(map.get(t.getColumnName()).toString());
                case "tinyint" -> values = values.concat(map.get(t.getColumnName()).toString());
                case "int" -> values = values.concat(map.get(t.getColumnName()).toString());
                default -> {
                }
            }
        }
        columns = columns.concat(")");
        values = values.concat(")");
        String sql="INSERT INTO "+tableName+" "+columns+" values "+values;
        System.out.println(" ---- ---- ---- "+sql);

        // Execute SQL query
        int res = 0;
        try {
            res = dbConnectionService.getConnection(dbUniqueName)
                    .prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the records inserted
        System.out.println(res + " records inserted");
        return  res>0;
    }

    public boolean editTableData(String dbUniqueName, String tableName, Map<String, Object> map){
        List<TableInfo> tableInfoList=connectionMap.get(dbUniqueName).getTableInfo().stream()
                .filter(obj -> obj.getTableName().equals(tableName))
                .toList();

        // expected primary key is always 0
        // created_at/ updated_at always NOW(),
        boolean first=true;
        String setString="";
        for(TableInfo t:tableInfoList){


            // #TODO These three if conditions below, can be converted to the config module
            if(t.getColumnName().equals("id")){
                continue;
            }
            if(t.getColumnName().equals("created_at")){
                continue;
            }
            if(!first){
                setString = setString.concat(",");
            }

            if(t.getColumnName().equals("updated_at")){
                setString = setString.concat("updated_at=").concat("NOW()");
                first = false;
                continue;
            }


            switch (t.getDataType()) { //#TODO this will be converted to the preparedStatement
                case "varchar" -> setString = setString.concat(t.getColumnName()+"=").concat("'"+map.get(t.getColumnName()).toString()+"'");
                case "text" -> setString = setString.concat(t.getColumnName()+"=").concat("'"+map.get(t.getColumnName()).toString()+"'");
                case "datetime" -> setString = setString.concat(t.getColumnName()+"=").concat("'"+map.get(t.getColumnName()).toString()+"'");//#TODO formatting required
                case "date" -> setString = setString.concat(t.getColumnName()+"=").concat("'"+map.get(t.getColumnName()).toString()+"'");//#TODO formatting required
                case "bigint" -> setString = setString.concat(t.getColumnName()+"=").concat(map.get(t.getColumnName()).toString());
                case "tinyint" -> setString = setString.concat(t.getColumnName()+"=").concat(map.get(t.getColumnName()).toString());
                case "int" -> setString = setString.concat(t.getColumnName()+"=").concat(map.get(t.getColumnName()).toString());
                default -> {
                }
            }
            first = false;
        }
        String sql="UPDATE "+tableName+" SET "+setString+" WHERE id="+map.get("id").toString();
        System.out.println(" ---- ---- ---- "+sql);

        // Execute SQL query
        int res = 0;
        try {
            res = dbConnectionService.getConnection(dbUniqueName)
                    .prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the records inserted
        System.out.println(res + " records inserted");
        return  res>0;
    }

    public boolean removeTableData(String dbUniqueName, String tableName, Integer id){
        //#TODO
        String sql="DELETE FROM "+tableName+" WHERE id="+id;
        System.out.println(" ---- ---- ---- "+sql);

        // Execute SQL query
        int res = 0;
        try {
            res = dbConnectionService.getConnection(dbUniqueName)
                    .prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the records inserted
        System.out.println(res + " records inserted");
        return  res>0;
    }

    public List<TableInfo> getTableInfo(String dbUniqueName, String tableName) {
        List<TableInfo> tableInfoList = connectionMap.get(dbUniqueName).getTableInfo().stream()
                .filter(obj -> obj.getTableName().equals(tableName))
                .toList();
        return  tableInfoList;
    }

    public List<TableInfo> prepareFormDetails(String dbUniqueName, String tableName) {
        List<TableInfo> tableInfoList = connectionMap.get(dbUniqueName).getTableInfo().stream()
                .filter(obj -> obj.getTableName().equals(tableName))
                .toList();
        List<TableInfo> formElementList=new ArrayList<>();
        for(TableInfo t:tableInfoList){
            if(!t.getColumnName().equals("id")
                    && !t.getColumnName().equals("created_at")
                    && !t.getColumnName().equals("updated_at")){
                formElementList.add(t);
            }
        }
        return  formElementList;
    }

    public DBMetadata getMetaData(String dbUniqueName){
        return connectionMap.getOrDefault(dbUniqueName, null);
    }
}
