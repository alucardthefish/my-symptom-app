package com.sop.firebasech2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sop.firebasech2.objetos.Adaptador;
import com.sop.firebasech2.objetos.FirebaseReferences;
import com.sop.firebasech2.objetos.Occurence;

import java.util.ArrayList;

public class ListSymptomsActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference appRef;

    ArrayList<String> keyList;
    ArrayAdapter<String> adaptador;
    ArrayList<Occurence> occurencesList;
    Occurence occurence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_symptoms);
        Toast.makeText(ListSymptomsActivity.this, "listView created!", Toast.LENGTH_SHORT);
        Log.d("Estado", "onCreate de listSymptoms");

        listView = findViewById(R.id.listView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Estado", "onStart de listSymptoms");
        occurencesList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Estado", "onResume de listSymptoms");

        database = FirebaseDatabase.getInstance();
        appRef = database.getReference(FirebaseReferences.APP_REFERENCE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        final DatabaseReference occurencesRef = appRef.child(user.getUid()).child(FirebaseReferences.OCCURENCE_REFERENCE);

        //Old implementation
        keyList = new ArrayList<>();   //array to store occurence/symptom keys
        occurence = new Occurence();

        occurencesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Occurence> tmpOccurenceList = new ArrayList<>();
                Log.d("Estado", "Los datos cambiaron en la nube o aqui se llamo la ref");
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //Adapter
                    occurence = ds.getValue(Occurence.class);
                    tmpOccurenceList.add(occurence);
                    // Storing key to pass in the view and allow in the future to crud operations.
                    String key = ds.getKey();
                    keyList.add(key);
                }
                occurencesList = tmpOccurenceList;
                listView.setAdapter(new Adaptador(ListSymptomsActivity.this, occurencesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListSymptomsActivity.this, "Por favor revisa que tengas acceso a internet", Toast.LENGTH_LONG).show();
            }
        });

        //Adding listener for clicks on listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Occurence symptom = occurencesList.get(position);
                String symptomKey = keyList.get(position);
                Intent i = new Intent(ListSymptomsActivity.this, SeeSymptomActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("title", symptom.getTitle());
                i.putExtra("description", symptom.getDescription());
                i.putExtra("intensidad", ""+symptom.getIntensity());
                i.putExtra("fecha", symptom.getTimeOfOccurence());
                i.putExtra("symptomKey", symptomKey);
                startActivity(i);
            }
        });
    }
}
