package com.sop.firebasech2.Logica;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sop.firebasech2.objetos.FirebaseReferences;
import com.sop.firebasech2.objetos.Occurence;

public class Manejador {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public Manejador(){
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public Task<Void> updateSymptom(String key_node, Occurence occurence){
        final DatabaseReference appRef = database.getReference(FirebaseReferences.APP_REFERENCE);
        //Accessing the database ref
        return appRef.child(currentUser.getUid())
                .child(FirebaseReferences.OCCURENCE_REFERENCE)
                .child(key_node).setValue(occurence);
    }

    public Query getSymptomReportByDates(String initialDate, String finalDate) {
        Query queryReport = database.getReference(FirebaseReferences.APP_REFERENCE)
                .child(currentUser.getUid())
                .child(FirebaseReferences.OCCURENCE_REFERENCE);
        return queryReport.orderByChild("timeOfOccurence")
                .startAt(initialDate)
                .endAt(finalDate);
    }
}
