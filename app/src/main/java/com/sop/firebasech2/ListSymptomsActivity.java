package com.sop.firebasech2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Collections;

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
    boolean isOrderedAscendingly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_symptoms);

        listView = findViewById(R.id.listView);
        isOrderedAscendingly = false;
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

        Toast.makeText(ListSymptomsActivity.this, R.string.message_load_symptoms, Toast.LENGTH_SHORT).show();

        final DatabaseReference occurencesRef = appRef.child(user.getUid()).child(FirebaseReferences.OCCURENCE_REFERENCE);

        //Old implementation
        keyList = new ArrayList<>();   //array to store occurence/symptom keys
        occurence = new Occurence();

        occurencesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Occurence> tmpOccurenceList = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    occurence = ds.getValue(Occurence.class);
                    tmpOccurenceList.add(occurence);
                    // Storing key to pass in the view to allow crud operations.
                    String key = ds.getKey();
                    keyList.add(key);
                }
                occurencesList = tmpOccurenceList;
                Collections.reverse(occurencesList);
                listView.setAdapter(new Adaptador(ListSymptomsActivity.this, occurencesList));
                if (occurencesList.isEmpty()) {
                    // Display message when symptom list is empty
                    ConstraintLayout myLayout = findViewById(R.id.list_symptom_layout_id);
                    TextView tvEmptySymps = new TextView(ListSymptomsActivity.this);
                    tvEmptySymps.setTextSize(16);
                    tvEmptySymps.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvEmptySymps.setText(getResources().getText(R.string.message_no_symptoms_in_bd));
                    tvEmptySymps.setLayoutParams(new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ));
                    myLayout.addView(tvEmptySymps);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListSymptomsActivity.this, R.string.message_connection_problem, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.symptoms_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.order_asc:
                if (!isOrderedAscendingly) {
                    Collections.reverse(occurencesList);
                    listView.setAdapter(new Adaptador(ListSymptomsActivity.this, occurencesList));
                    isOrderedAscendingly = true;
                }
                break;
            case R.id.order_des:
                if (isOrderedAscendingly) {
                    Collections.reverse(occurencesList);
                    listView.setAdapter(new Adaptador(ListSymptomsActivity.this, occurencesList));
                    isOrderedAscendingly = false;
                }
                break;
        }
        return true;
    }
}
