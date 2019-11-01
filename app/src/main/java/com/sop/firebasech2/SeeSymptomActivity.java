package com.sop.firebasech2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SeeSymptomActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionEditText;
    private TextView intensityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_symptom);

        titleTextView = findViewById(R.id.textViewForTitle);
        descriptionEditText = findViewById(R.id.textViewForDesc);
        intensityEditText = findViewById(R.id.textViewForIntensity);

        getFromIntent();

    }

    private void getFromIntent(){
        String title = getIntent().getStringExtra("title");
        String descripcion = getIntent().getStringExtra("description");
        String intensidad = getIntent().getStringExtra("intensidad");
        String fecha = getIntent().getStringExtra("fecha");
        titleTextView.setText(title);
        descriptionEditText.setText(descripcion);
        intensityEditText.setText(intensidad);
    }
}
