package com.basic.crud.autocrud.entity;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DBApplication {
    private String uniqueName;
    private String dbName;
    private String dbServer;
    private String dbPort;
    private String dbUser;
    private String dbPassword;
    private String dbTitle;
}
