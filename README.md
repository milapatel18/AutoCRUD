<img width="1079" alt="image" src="https://github.com/user-attachments/assets/80c04a98-507a-4347-bd00-fd935d5c1db9" /># Basic AutoCRUD
Completely Dynamic AUTO CRUD Development with core concept of Spring Boot API, Database, ORM/JDBC, REST.
Design Patterns : 

Requirement : 
- Connect and configure MySQL database in UI - 
- Select Database in UI and show all the tables
- Diaplay all the tables in side panel and manage crud operations for each table
- Manage 1-M relationship in dropdown menu with single select options ( Display ID with 4 columns value in tabular Mode )
- Manage all the form Components based on metadata of the table columns e.g. Numbers, Text, TextArea, 

Backend : 
- Use MySQL/PostgreSQL
- Use different packages for managing metadata services and connections
- Use seamless API design for CRUD
- Save Database details to the filesystem only

Technology Stack : 
- Spring Boot 3.*, Java 17.*, MySQL 8.*,
- ReactJS 19.*


API Details 
1. Create Databae Conection
   - /api/v1/database/create
   - POST {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle}
2. List Database Connection
   - /api/v1/database/list
   - GET List of {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle}
3. Edit Databae Details
   - /api/v1/database/edit/{uniqueName}
   - PATCH Details of {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle}
4. Delete Databae Details
   - /api/v1/database/delete/{uniqueName}
   - DELETE
  
5. Open database Details
   - /api/v1/database/view/{uniqueName}
   - GET  provides Details of {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle}

7. Open database Table Details
   - /api/v1/database/tables/{uniqueName}
   - GET  provides Details of {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle} with List of tables and their links
   - Display all the tables in side panel

7. Open table details from the side panel
   - /api/v1/table/view/{uniqueName}/{tableName}
   - GET list of table details and display with edit and Delete option for each raw
   - show Create Record Option on top
  
7. Edit Table data option will open form in Model with simple form UI in onecolumn
   - /api/v1/table/edit/{uniqueName}/{tableName}/{recordId)
  
8. Create Table data option will open form in Model with simple form UI in onecolumn
   - /api/v1/table/create/{uniqueName}/{tableName}
  
9. Delete Table data option will open form in Model with simple form UI in onecolumn
   - /api/v1/table/delete/{uniqueName}/{tableName}/{recordId)
  



UI Form Options and Validation
- Text      Max Limit, Not Null
- TextArea  Max Limit, Not Null
- Number    Min Max Limit, Not Null
- DropDown  Default first one is empty, Not Null, 
- Date      Not Null
- DateTime  Not Null

#MYSQL
Important Database Queries for the Metadata
- Fetch full details of the table
SELECT * FROM information_schema.columns    
WHERE (table_schema='DBNAME' and table_name = 'TABLENAME') 
order by ordinal_position;
- 
SELECT * FROM information_schema.key_column_usage
WHERE constraint_schema="DBNAME";
select * from information_schema.key_column_usage where table_schema="springtodo";

-- date time must be formatted to the DB relevant from UI request
-- log everywhere
-- db default for jwt, google, etc..

1. Index Page First View

<img width="1079" alt="image" src="https://github.com/user-attachments/assets/5070e49f-5609-4a76-a138-98c0b5a84215" />

2. Select available databases
<img width="1079" alt="image" src="https://github.com/user-attachments/assets/b3167dea-9cce-43fa-88be-c2f97108df35" />

3. Select Tables
<img width="1079" alt="image" src="https://github.com/user-attachments/assets/a59f1174-80ac-4879-8386-3df1bd301353" />

4. View CRUD Operations

<img width="1710" alt="image" src="https://github.com/user-attachments/assets/dc60b71d-ff90-410c-bb2e-bdad8b374c2a" />









