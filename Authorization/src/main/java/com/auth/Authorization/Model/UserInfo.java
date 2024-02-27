package com.auth.Authorization.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "User_Info")
public class UserInfo {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "User_Name")
    private String userName;

    @Column(name = "Email_Id", nullable = false, unique = true)
    private String emailId;

    @Column(name = "Password",nullable = false)
    private String password;

    @Column(name = "Mobile_Number")
    private String mobileNumber;

    @Column(nullable = false, name = "Roles")
    private String roles;

}
