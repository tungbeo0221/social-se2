package org.se2.authentication.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserDto {
    private Long id;
    private String email;

    private String password;

    private String role;

    private String fullname;
    private String phoneNo;

    private Integer gender;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Specify the date format expected in the form
    private Date dob;

    public UserDto(String email, String password, String role, String fullname, String phoneNo, Integer gender, Date dob) {
        super();
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Long getId() {
        return this.id;
    }
}
