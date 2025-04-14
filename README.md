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
5. Delete Databae Details
   - /api/v1/database/delete/{uniqueName}
   - DELETE

