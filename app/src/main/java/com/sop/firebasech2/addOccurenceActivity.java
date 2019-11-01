package com.sop.firebasech2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sop.firebasech2.objetos.FirebaseReferences;
import com.sop.firebasech2.objetos.Occurence;
import com.sop.firebasech2.objetos.Router;

public class addOccurenceActivity extends AppCompatActivity {

    private Button mButtonProfile;
    private Button mButtonAddOccurence;
    private Spinner mSpinnerIntensity;
    private EditText et_occurence_title;
    private EditText et_description;
    private Spinner spinner_intensities;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_occurence);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appRef = database.getReference(FirebaseReferences.APP_REFERENCE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        et_occurence_title = findViewById(R.id.et_occurence_title);
        et_description = findViewById(R.id.et_description);
        spinner_intensities = findViewById(R.id.spinner);

        // Button for going back to profile activity
        mButtonProfile = findViewById(R.id.btn_goto_profile);
        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router router = new Router(addOccurenceActivity.this);
                router.goto_profile(user);
            }
        });

        // Button for add a new occurence in the database
        mButtonAddOccurence = findViewById(R.id.btn_add);
        mButtonAddOccurence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUid();
                //Add here some input validation
                if (validateOccurence()) {
                    String title = et_occurence_title.getText().toString();
                    String desc = et_description.getText().toString();
                    int intensity = Integer.parseInt(spinner_intensities.getSelectedItem().toString());
                    if (desc.isEmpty()){
                        desc = "No Description";
                    }
                    int type = 1;   // Symptom type quemado
                    Occurence occurence = new Occurence(title, desc, intensity, type);
                    appRef.child(userId).child(FirebaseReferences.OCCURENCE_REFERENCE).push().setValue(occurence).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(addOccurenceActivity.this,"Ocurrencia agregada", Toast.LENGTH_SHORT).show();
                                Router router = new Router(addOccurenceActivity.this);
                                router.goto_profile(user);
                            } else {
                                Toast.makeText(addOccurenceActivity.this, "Hubo un problema", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(addOccurenceActivity.this, "El campo titulo no puede ser vacio", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Spinner of intensity options
        mSpinnerIntensity = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intensities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter <Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, intensities);
        mSpinnerIntensity.setAdapter(adapter);

    }

    public boolean validateOccurence(){
        String title = et_occurence_title.getText().toString();
        return !title.isEmpty();
    }
}
