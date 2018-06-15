package com.example.domain.submitProposal.models;


public class JobDetails {
    private Job job;
    private Client client;
    private CurrentUser currentUser;
    private ProposalInfo icProposalInfo;

    public Job getJob() {
        return job;
    }

    public Client getClient() {
        return client;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public ProposalInfo getIcProposalInfo() {
        return icProposalInfo;
    }

    public void setIcProposalInfo(ProposalInfo icProposalInfo) {
        this.icProposalInfo = icProposalInfo;
    }
}
