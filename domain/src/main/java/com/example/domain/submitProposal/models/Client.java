package com.example.domain.submitProposal.models;

import com.upwork.android.mvvmp.models.DisplayIntegerEntry;

import java.util.List;

public class Client {
    private String country;
    private String profilePictureUrl;
    private String city;
    private TimeZone timeZone;
    private DisplayIntegerEntry memberSince;
    private float feedbackRate;
    private int feedbackTotal;
    private boolean isPaymentVerified;
    private DisplayIntegerEntry lastViewed;
    private List<ClientProperty> properties;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DisplayIntegerEntry getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(DisplayIntegerEntry memberSince) {
        this.memberSince = memberSince;
    }

    public float getFeedbackRate() {
        return feedbackRate;
    }

    public void setFeedbackRate(float feedbackRate) {
        this.feedbackRate = feedbackRate;
    }

    public int getFeedbackTotal() {
        return feedbackTotal;
    }

    public void setFeedbackTotal(int feedbackTotal) {
        this.feedbackTotal = feedbackTotal;
    }

    public boolean isPaymentVerified() {
        return isPaymentVerified;
    }

    public void setIsPaymentVerified(boolean isPaymentVerified) {
        this.isPaymentVerified = isPaymentVerified;
    }

    public DisplayIntegerEntry getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(DisplayIntegerEntry lastViewed) {
        this.lastViewed = lastViewed;
    }

    public List<ClientProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ClientProperty> properties) {
        this.properties = properties;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
