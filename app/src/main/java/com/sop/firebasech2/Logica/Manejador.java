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

    public void updateSymptom(Occurence occurence, String key_node, final Context context){
        final DatabaseReference appRef = database.getReference(FirebaseReferences.APP_REFERENCE);
        //Accessing the database location
        appRef.child(currentUser.getUid())
                .child(FirebaseReferences.OCCURENCE_REFERENCE)
                .child(key_node).setValue(occurence)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Sintoma actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Hubo un problema al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Task<Void> fuun(String key_node, Occurence occurence){
        final DatabaseReference appRef = database.getReference(FirebaseReferences.APP_REFERENCE);
        //Accessing the database location
        return appRef.child(currentUser.getUid())
                .child(FirebaseReferences.OCCURENCE_REFERENCE)
                .child(key_node).setValue(occurence);
    }
}
