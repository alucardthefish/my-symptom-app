package com.sop.firebasech2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sop.firebasech2.objetos.FirebaseReferences;

public class SeeSymptomActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionEditText;
    private TextView intensityEditText;
    private Button deleteBtn;
    private Button updateBtn;

    private String symptomKey;
    private String title;
    private String description;
    private String intensidad;
    private String fecha;

    private static final int EDIT_SYMPTOM_REQUEST_CODE = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_symptom);

        titleTextView = findViewById(R.id.textViewForTitle);
        descriptionEditText = findViewById(R.id.textViewForDesc);
        descriptionEditText.setMovementMethod(new ScrollingMovementMethod());
        intensityEditText = findViewById(R.id.textViewForIntensity);

        getFromIntent();

        deleteBtn = findViewById(R.id.buttonDeleteSymptom);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SeeSymptomActivity.this);
                builder.setTitle("Advertencia");
                builder.setMessage("Esta acción es irreversible ¿Está seguro de eliminar este síntoma?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSymptom();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //add update button
        updateBtn = findViewById(R.id.buttonUpdateSymptom);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSymptomBtnAction();
            }
        });

    }

    private void getFromIntent(){
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        intensidad = getIntent().getStringExtra("intensidad");
        fecha = getIntent().getStringExtra("fecha");
        symptomKey = getIntent().getStringExtra("symptomKey");

        titleTextView.setText(title);
        descriptionEditText.setText(description);
        intensityEditText.setText(intensidad);
    }

    private void deleteSymptom(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference appRef = database.getReference(FirebaseReferences.APP_REFERENCE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String userId = user.getUid();

        appRef.child(userId).child(FirebaseReferences.OCCURENCE_REFERENCE).child(symptomKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SeeSymptomActivity.this, "Síntoma eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    SeeSymptomActivity.this.finish();
                } else {
                    Toast.makeText(SeeSymptomActivity.this, "Hubo un error. No se pudo eliminar el síntoma", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateSymptomBtnAction(){

        Intent i = new Intent(SeeSymptomActivity.this, editOccurenceActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("title", title);
        i.putExtra("description", description);
        i.putExtra("intensity", ""+intensidad);
        i.putExtra("date", fecha);
        i.putExtra("symptomKey", symptomKey);
        //startActivity(i);
        startActivityForResult(i, EDIT_SYMPTOM_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if editOccurenceActivity response is ok
        if (requestCode == EDIT_SYMPTOM_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                titleTextView.setText(data.getStringExtra("title"));
                descriptionEditText.setText(data.getStringExtra("description"));
            }
        }
    }
}
