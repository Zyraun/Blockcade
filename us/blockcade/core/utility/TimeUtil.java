package us.blockcade.core.utility;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TimeUtil {

    private static int getIndexContainingString(String[] array, String search) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].contains(search)) return i;
        } return -1;
    }

    public static int getSeconds(String time) {
        String[] split = time.split(" ");
        int tally = 0;
        if (time.contains("forever") || time.contains("never")) {
            return -1;
        }

        List<String> accounted = new ArrayList<>();

        String[] day = new String[] { "d", "dys", "day", "days" };
        String[] hour = new String[] { "h", "hour", "hours", "hr", "hrs" };
        String[] minute = new String[] { "m", "min", "mins", "minute", "minutes" };
        String[] second = new String[] { "s", "sec", "secs", "sex", "second", "seconds" };

        for (String d : day) {
            if (time.contains(d)) {
                if (!accounted.contains("d")) {
                    String portion = split[getIndexContainingString(split, d)];
                    String amount = portion.split(d)[0];
                    try {
                        int amt = Integer.valueOf(amount);
                        tally = tally + (amt * 86400);
                        accounted.add("d");
                    } catch (Exception e) {
                        System.out.println("Error in configuration file. " + time + ". Days is not an integer.");
                    }
                }
            }
        }
        for (String h : hour) {
            if (time.contains(h)) {
                if (!accounted.contains("h")) {
                    String portion = split[getIndexContainingString(split, h)];
                    String amount = portion.split(h)[0];
                    try {
                        int amt = Integer.valueOf(amount);
                        tally = tally + (amt * 3600);
                        accounted.add("h");
                    } catch (Exception e) {
                        System.out.println("Error in configuration file. " + time + ". Hours is not an integer.");
                    }
                }
            }
        }
        for (String m : minute) {
            if (time.contains(m)) {
                if (!accounted.contains("m")) {
                    String portion = split[getIndexContainingString(split, m)];
                    String amount = portion.split(m)[0];
                    try {
                        int amt = Integer.valueOf(amount);
                        tally = tally + (amt * 60);
                        accounted.add("m");
                    } catch (Exception e) {
                        System.out.println("Error in configuration file. " + time + ". Minutes is not an integer.");
                    }
                }
            }
        }
        for (String s : second) {
            if (time.contains(s)) {
                if (!accounted.contains("s")) {
                    String portion = split[getIndexContainingString(split, s)];
                    String amount = portion.split(s)[0];
                    try {
                        int amt = Integer.valueOf(amount);
                        tally = tally + (amt * 1);
                        accounted.add("s");
                    } catch (Exception e) {
                        System.out.println("Error in configuration file. " + time + ". Seconds is not an integer.");
                    }
                }
            }
        }

        return tally;
    }

    public static String getFormatted(int seconds) {
        int days = (seconds - (seconds % 86400)) / 86400;

        double hoursTemp = Math.floor((seconds - (days * 86400)) / 3600);
        int hours = Integer.valueOf((hoursTemp + "").split("\\.")[0]);

        int secondLeft = seconds - ((days * 86400) + (hours * 3600));
        double minsTemp = Math.floor(secondLeft / 60);
        int mins = Integer.valueOf((minsTemp + "").split("\\.")[0]);

        int secs = secondLeft - (mins * 60);

        return days + "d " + hours + "h " + mins + "m " + secs + "s";
    }

}
