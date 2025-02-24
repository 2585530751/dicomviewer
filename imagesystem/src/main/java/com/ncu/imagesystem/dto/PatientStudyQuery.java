package com.ncu.imagesystem.dto;

import java.time.LocalDate;
import java.util.List;

public class PatientStudyQuery {
    private Long pageSize;
    private Long current;
    private List<Integer> departmentIdList;
    private String patientName;
    private String patientGender;

    private String dateOfBirth;

    private String dateOfBirthBegin;

    private String dateOfBirthEnd;

    private  String phoneNumber;

    private String email;

    private String address;



    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirthBegin() {
        return dateOfBirthBegin;
    }

    public void setDateOfBirthBegin(String dateOfBirthBegin) {
        this.dateOfBirthBegin = dateOfBirthBegin;
    }

    public String getDateOfBirthEnd() {
        return dateOfBirthEnd;
    }

    public void setDateOfBirthEnd(String dateOfBirthEnd) {
        this.dateOfBirthEnd = dateOfBirthEnd;
    }
}
