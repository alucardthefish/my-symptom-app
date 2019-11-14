package com.sop.firebasech2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.sop.firebasech2.Logica.Manejador;
import com.sop.firebasech2.objetos.Occurence;
import com.google.android.gms.tasks.Task;

import static com.sop.firebasech2.objetos.Utils.validateSymptomDiff;
import static com.sop.firebasech2.objetos.Utils.validateSymptomTitleAndDesc;

public class editOccurenceActivity extends AppCompatActivity {
    private EditText tvEditSymptomTitle;
    private EditText etEditSymptomDesc;
    private Button btnConfirm;
    private Button btnCancel;

    private String symptomKey;
    private String title;
    private String description;
    private int intensity;
    private String symp_date;
    private int type = 1;
    private Occurence occurence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_occurence);

        getFromIntent();

        tvEditSymptomTitle = findViewById(R.id.tvEditTitle);
        etEditSymptomDesc = findViewById(R.id.etEditDescription);
        btnConfirm = findViewById(R.id.btnConfirmEdition);
        btnCancel = findViewById(R.id.btnEditCancel);

        tvEditSymptomTitle.setText(title);
        etEditSymptomDesc.setText(description);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEdition();
            }
        });
    }

    private void getFromIntent(){
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        intensity = Integer.parseInt(getIntent().getStringExtra("intensity"));
        symp_date = getIntent().getStringExtra("date");
        symptomKey = getIntent().getStringExtra("symptomKey");

        occurence = new Occurence();
        occurence.setTitle(title);
        occurence.setDescription(description);
        occurence.setIntensity(intensity);
        occurence.setTimeOfOccurence(symp_date);
        occurence.setType(1);
    }

    private void confirmEdition(){
        final String tvTitle = tvEditSymptomTitle.getText().toString();
        final String etDesc = etEditSymptomDesc.getText().toString();
        // Check if fields are filled
        if (validateSymptomTitleAndDesc(tvTitle, etDesc)) {
            // Check if data changed
            if (validateSymptomDiff(title, description, tvTitle, etDesc)){
                //Add logic of update
                Manejador manejador = new Manejador();
                occurence.setTitle(tvTitle);
                occurence.setDescription(etDesc);
                Task<Void> updateBD = manejador.fuun(symptomKey, occurence);
                updateBD.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(editOccurenceActivity.this, "Sintoma actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("title", tvTitle);
                            intent.putExtra("description", etDesc);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(editOccurenceActivity.this, "Ocurrio un error. Sintoma no pudo ser actualizado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Los datos no se modificaron por lo tanto no se guardan
                Toast.makeText(editOccurenceActivity.this, "No se registran cambios", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Los campos no pueden estar vacios
            Toast.makeText(editOccurenceActivity.this, "Los campos titulo y descripción no pueden estar vacios", Toast.LENGTH_SHORT).show();
        }
    }
}
