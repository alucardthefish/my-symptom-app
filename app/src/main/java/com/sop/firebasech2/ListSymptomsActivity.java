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

    ArrayList<String> list;
    ArrayAdapter<String> adaptador;
    ArrayList<Occurence> occurencesList;
    Occurence occurence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_symptoms);
        Toast.makeText(ListSymptomsActivity.this, "listView created!", Toast.LENGTH_SHORT);

        database = FirebaseDatabase.getInstance();
        appRef = database.getReference(FirebaseReferences.APP_REFERENCE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        final DatabaseReference occurencesRef = appRef.child(user.getUid()).child(FirebaseReferences.OCCURENCE_REFERENCE);

        //Old implementation
        list = new ArrayList<>();
        occurencesList = new ArrayList<>();
        adaptador = new ArrayAdapter<String>(this, R.layout.occurence_info, R.id.symptomInfo, list);
        occurence = new Occurence();

        occurencesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //Adapter
                    occurence = ds.getValue(Occurence.class);
                    occurencesList.add(occurence);
                    // Storing key to pass in the view and allow in the future to crud operations.
                    String key = ds.getKey();
                    //Old implementation
                    //list.add("Zona: " + occurence.getTitle() + "\t Intensidad: " + String.valueOf(occurence.getIntensity()) + "\nDescripción: " + occurence.getDescription());
                }
                //Old implementation
                //listView.setAdapter(adaptador);
                listView.setAdapter(new Adaptador(ListSymptomsActivity.this, occurencesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = findViewById(R.id.listView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Adding listener for clicks on listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Occurence symptom = occurencesList.get(position);
                Intent i = new Intent(ListSymptomsActivity.this, SeeSymptomActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("title", symptom.getTitle());
                i.putExtra("description", symptom.getDescription());
                i.putExtra("intensidad", ""+symptom.getIntensity());
                i.putExtra("fecha", symptom.getTimeOfOccurence());
                startActivity(i);

            }
        });
    }
}
