package com.example.domain.submitProposal.models;

import com.upwork.android.mvvmp.models.DisplayMoneyEntry;
import com.upwork.android.mvvmp.models.DisplayStringEntry;

import java.util.List;

public class Job {
    private String id;
    private long createdTimestamp;
    private String title;
    private String description;
    private DisplayStringEntry jobType;
    private DisplayMoneyEntry fixedPriceBudget;
    private Bids bids;
    private DisplayStringEntry engagementType; //"less than 30 hrs"
    private DisplayStringEntry estimatedDuration; //for hourly jobs
    private DisplayStringEntry experienceLevel;
    private List<JobProperty> properties;
    private List<Question> questions;
    private List<Qualification> qualifications;
    private List<Attachment> attachments;
    private String lookingToHire;
    private String hired;
    private String proposals;
    private String interviewing;
    private boolean isAvailable;
    private boolean isCoverLetterRequired;
    private boolean isTieredPricing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DisplayStringEntry getJobType() {
        return jobType;
    }

    public void setJobType(DisplayStringEntry jobType) {
        this.jobType = jobType;
    }

    public DisplayMoneyEntry getFixedPriceBudget() {
        return fixedPriceBudget;
    }

    public void setFixedPriceBudget(DisplayMoneyEntry fixedPriceBudget) {
        this.fixedPriceBudget = fixedPriceBudget;
    }

    public Bids getBids() {
        return bids;
    }

    public void setBids(Bids bids) {
        this.bids = bids;
    }

    public DisplayStringEntry getEngagementType() {
        return engagementType;
    }

    public void setEngagementType(DisplayStringEntry engagementType) {
        this.engagementType = engagementType;
    }

    public DisplayStringEntry getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(DisplayStringEntry estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public DisplayStringEntry getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(DisplayStringEntry experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public List<JobProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<JobProperty> properties) {
        this.properties = properties;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getLookingToHire() {
        return lookingToHire;
    }

    public void setLookingToHire(String lookingToHire) {
        this.lookingToHire = lookingToHire;
    }

    public String getHired() {
        return hired;
    }

    public void setHired(String hired) {
        this.hired = hired;
    }

    public String getProposals() {
        return proposals;
    }

    public void setProposals(String proposals) {
        this.proposals = proposals;
    }

    public String getInterviewing() {
        return interviewing;
    }

    public void setInterviewing(String interviewing) {
        this.interviewing = interviewing;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isCoverLetterRequired() {
        return isCoverLetterRequired;
    }

    public void setIsCoverLetterRequired(boolean isCoverLetterRequired) {
        this.isCoverLetterRequired = isCoverLetterRequired;
    }

    public boolean isTieredPricing() {
        return isTieredPricing;
    }
}
