package com.sop.firebasech2.objetos;

public final class Utils {

    // Validation method for symptom entry
    public static boolean validateSymptomTitleAndDesc(String title, String description){
        return !title.isEmpty() && !description.isEmpty();
    }

    public static boolean validateSymptomDiff(String title, String desc, String titleFromView, String descFromView){
        return title != titleFromView || desc != descFromView;
    }
}
