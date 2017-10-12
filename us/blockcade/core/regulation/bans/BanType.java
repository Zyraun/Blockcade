package us.blockcade.core.regulation.bans;

import us.blockcade.core.utility.TimeUtil;

public enum BanType {

    PLACEHOLDER("Default Ban Message", 100);

    public String reason;
    public int duration;

    private BanType(String reason, int duration) {
        this.reason = reason;
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public int getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        return TimeUtil.getFormatted(getDuration());
    }

}
