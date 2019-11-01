package com.sop.firebasech2.objetos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Occurence {
    String title;
    String description;
    int intensity;          // Could be a scale 1 up to 5
    int type;               // 1 for symptom and 2 for poop diary.
    String timeOfOccurence; // to check what time it was added

    public Occurence(String title, String description, int intensity, int type) {
        this.title = title;
        this.description = description;
        this.intensity = intensity;
        this.type = type;
        //Create the date and time formatter to be used in dates
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeOfOccurence = formatter.format(new Date());
    }

    public Occurence() {
    }

    public String getTimeOfOccurence() {
        return timeOfOccurence;
    }

    public void setTimeOfOccurence(String timeOfOccurence) {
        this.timeOfOccurence = timeOfOccurence;
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

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
