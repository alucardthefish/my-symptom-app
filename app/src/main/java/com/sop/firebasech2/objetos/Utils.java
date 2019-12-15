package com.sop.firebasech2.objetos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    // Validation method for symptom entry
    public static boolean validateSymptomTitleAndDesc(String title, String description){
        return !title.isEmpty() && !description.isEmpty();
    }

    public static boolean validateSymptomDiff(String title, String desc, String titleFromView, String descFromView){
        return title != titleFromView || desc != descFromView;
    }

    // Validation for date interval
    public static boolean validateDateInterval(String initialDate, String finalDate) {
        boolean output = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date initDate = sdf.parse(initialDate);
            Date finlDate = sdf.parse(finalDate);
            output = finlDate.after(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }

    // Decorator for date numbers < 10
    public static String dateNumberDecorator(int num) {
        String output = "";
        if (num < 10) {
            output += "0";
        }
        return output+num;
    }
}
