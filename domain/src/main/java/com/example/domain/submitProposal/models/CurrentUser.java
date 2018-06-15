package com.example.domain.submitProposal.models;

public class CurrentUser {
    private boolean isSaved;
    private String applicationId;
    private MembershipType membership;
    private String contractId;
    private String offerId;

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsJobSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public MembershipType getMembership() {
        return membership;
    }

    public void setMembership(MembershipType membership) {
        this.membership = membership;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
