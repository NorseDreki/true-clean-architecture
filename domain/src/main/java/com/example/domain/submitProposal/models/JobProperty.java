package com.example.domain.submitProposal.models;

import java.util.List;

public class JobProperty {
    private String id;
    private String name;
    private String type;
    private List<JobPropertyValue> values;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<JobPropertyValue> getValues() {
        return values;
    }

    public void setValues(List<JobPropertyValue> values) {
        this.values = values;
    }
}
