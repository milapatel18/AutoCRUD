# Basic AutoCRUD
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
   - GET  provides Details of {uniqueName, dbName, dbServer, dbPort, dbUser, dbPassword, dbTitle} with List of tables and their links
   - Display all the tables in side panel 
  
6. Open table details from the side panel
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


