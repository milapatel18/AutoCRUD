package com.basic.crud.autocrud.service;

import com.basic.crud.autocrud.entity.DBApplication;
import com.basic.crud.autocrud.entity.DBApplicationList;
import com.basic.crud.autocrud.entity.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class DatabaseService {


    @Value("${database.files.location}")
    private String dbFileLocation;

    @Autowired
    private DBConnectionService dbConnectionService;

    @Autowired
    private TableService tableService;

    public DBApplication createDbApplication(DBApplication dbApplication){
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(dbFileLocation+"/"+dbApplication.getUniqueName());
        } catch (FileNotFoundException e) {
            dbApplication=null;
            throw new RuntimeException(e);
        }
        Writer outputStreamWriter = new OutputStreamWriter(outputStream);

        try {
            outputStreamWriter.write("uniqueName="+dbApplication.getUniqueName());
            outputStreamWriter.write("\ndbName="+dbApplication.getDbName());
            outputStreamWriter.write("\ndbServer="+dbApplication.getDbServer());
            outputStreamWriter.write("\ndbPort="+dbApplication.getDbPort());
            outputStreamWriter.write("\ndbUser="+dbApplication.getDbUser());
            outputStreamWriter.write("\ndbPassword="+dbApplication.getDbPassword());
            outputStreamWriter.write("\ndbTitle="+dbApplication.getDbTitle());
            outputStreamWriter.close();

        } catch (IOException e) {
            dbApplication=null;
            throw new RuntimeException(e);
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dbApplication;
    }

    public DBApplication viewDbApplication(String dbApplicationName){
        DBApplication dbApplication=new DBApplication();
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dbFileLocation+"/"+dbApplicationName);
        } catch (FileNotFoundException e) {
            dbApplication=null;
            throw new RuntimeException(e);
        }

        try {
            prop.load(inputStream);
            dbApplication.setUniqueName(prop.getProperty("uniqueName"));
            dbApplication.setDbName(prop.getProperty("dbName"));
            dbApplication.setDbServer(prop.getProperty("dbServer"));
            dbApplication.setDbPort(prop.getProperty("dbPort"));
            dbApplication.setDbUser(prop.getProperty("dbUser"));
            dbApplication.setDbPassword(prop.getProperty("dbPassword"));
            dbApplication.setDbTitle(prop.getProperty("dbTitle"));
        } catch (IOException e) {
            dbApplication=null;
            throw new RuntimeException(e);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dbApplication;
    }

    public DBApplicationList getDbApplicationList(){
        File file=new File(dbFileLocation);
        DBApplicationList dbApplicationList=new DBApplicationList();
        dbApplicationList.setTotalCount(Objects.requireNonNull(file.listFiles()).length);
        if(dbApplicationList.getTotalCount()>0){
            List<DBApplication> dbApplicationList1=new ArrayList<>();
            for(File dbfile:file.listFiles()){
                DBApplication newDbApplication=new DBApplication();
                Properties prop = new Properties();
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(dbFileLocation+"/"+dbfile.getName());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    prop.load(inputStream);
                    newDbApplication.setUniqueName(prop.getProperty("uniqueName"));
                    newDbApplication.setDbName(prop.getProperty("dbName"));
                    newDbApplication.setDbServer(prop.getProperty("dbServer"));
                    newDbApplication.setDbPort(prop.getProperty("dbPort"));
                    newDbApplication.setDbUser(prop.getProperty("dbUser"));
                    newDbApplication.setDbPassword(prop.getProperty("dbPassword"));
                    newDbApplication.setDbTitle(prop.getProperty("dbTitle"));
                    dbApplicationList1.add(newDbApplication);
                } catch (IOException e) {
                    newDbApplication=null;
                    throw new RuntimeException(e);
                }finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            dbApplicationList.setEntities(dbApplicationList1);
        }
        return dbApplicationList;
    }

    public void prepareDBApplication(String dbApplicationName){
        DBApplication dbApplication=this.viewDbApplication(dbApplicationName);
        if(dbApplication!=null){
            dbConnectionService.createNewDatabaseConnection(dbApplication.getUniqueName(),
                    DataUtils.MYSQL,
                    dbApplication.getDbServer(),
                    dbApplication.getDbPort(),
                    dbApplication.getDbName(),
                    dbApplication.getDbUser(),
                    dbApplication.getDbPassword()
            );
            tableService.prepareDatabaseMetadata(dbApplication.getUniqueName(),dbApplication.getDbName());
        }
    }
}
