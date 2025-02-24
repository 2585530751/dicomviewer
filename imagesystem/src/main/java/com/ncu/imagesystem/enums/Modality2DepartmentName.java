package com.ncu.imagesystem.enums;

public enum Modality2DepartmentName {
    CT("CT", "ctReader"),
    MRI("MRI", "mriReader"),
    ;


    private String modality;
    private String departmentName;

    Modality2DepartmentName(String modality, String departmentName) {
        this.modality = modality;
        this.departmentName = departmentName;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public static String getDepartmentByModality(String modality){
        for(Modality2DepartmentName entity : values()){
            if(entity.getModality().equals(modality)){
                return entity.getDepartmentName();
            }
        }
        return null;
    }
}
