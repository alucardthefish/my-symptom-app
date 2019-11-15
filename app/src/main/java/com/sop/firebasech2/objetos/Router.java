package com.sop.firebasech2.objetos;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.sop.firebasech2.ProfileActivity;

public class Router {

    private Activity currentActivity;

    public Router() {
    }

    public Router(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void goto_profile(FirebaseUser user){
        // Method to send data and update view from whatever view
        Intent i = new Intent(this.currentActivity, ProfileActivity.class);
        i.putExtra("name", user.getDisplayName());
        i.putExtra("email", user.getEmail());
        this.currentActivity.startActivity(i);

        //Some discrimination
        String activityName = this.currentActivity.getClass().getName();
        Log.d("Mytag", activityName);
        if (activityName.endsWith(".MainActivity") || activityName.endsWith(".LoginActivity")){
            this.currentActivity.finish();
        }
    }
}
