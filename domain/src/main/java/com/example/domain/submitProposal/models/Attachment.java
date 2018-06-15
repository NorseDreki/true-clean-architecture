package com.example.domain.submitProposal.models;

import com.upwork.android.mvvmp.models.DisplayIntegerEntry;

public class Attachment {
    private String name;
    private String url;
    private DisplayIntegerEntry fileSize;
    private boolean isAuthenticationRequired;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DisplayIntegerEntry getFileSize() {
        return fileSize;
    }

    public void setFileSize(DisplayIntegerEntry fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isAuthenticationRequired() {
        return isAuthenticationRequired;
    }

    public void setIsAuthenticationRequired(boolean isAuthenticationRequired) {
        this.isAuthenticationRequired = isAuthenticationRequired;
    }
}
