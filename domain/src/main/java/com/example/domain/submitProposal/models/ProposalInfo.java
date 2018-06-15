package com.example.domain.submitProposal.models;

import com.upwork.android.mvvmp.models.DisplayDoubleEntry;
import com.upwork.android.mvvmp.models.DisplayMoneyEntry;

public class ProposalInfo {
    private Connects connects;
    private boolean isAvailable;
    private double minAmount;
    private double maxAmount;
    private DisplayMoneyEntry rate;
    private DisplayDoubleEntry serviceFee;
    /*private JsonObject fees;
    private List<UnavailabilityReason> unavailabilityReasons;
    private SuggestedRate suggestedRate;*/

    public Connects getConnects() {
        return connects;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public DisplayMoneyEntry getRate() {
        return rate;
    }

    public DisplayDoubleEntry getServiceFee() {
        return serviceFee;
    }

    /*public JsonObject getFees() {
        return fees;
    }

    public List<UnavailabilityReason> getUnavailabilityReasons() {
        return unavailabilityReasons;
    }

    public void setUnavailabilityReasons(List<UnavailabilityReason> unavailabilityReasons) {
        this.unavailabilityReasons = unavailabilityReasons;
    }

    public SuggestedRate getSuggestedRate() {
        return suggestedRate;
    }*/
}
