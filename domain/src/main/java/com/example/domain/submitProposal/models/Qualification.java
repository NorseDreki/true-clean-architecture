package com.example.domain.submitProposal.models;

public class Qualification {
    private String qualification;
    private String selection;
    private boolean isQualificationMet;

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public boolean isQualificationMet() {
        return isQualificationMet;
    }

    public void setIsQualificationMet(boolean isQualificationMet) {
        this.isQualificationMet = isQualificationMet;
    }
}
