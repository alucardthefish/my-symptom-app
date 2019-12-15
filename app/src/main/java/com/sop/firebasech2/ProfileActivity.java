package com.sop.firebasech2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button mButtonReports;
    private Button mButtonAddOccurence;
    private Button mButtonListOccurences;
    private TextView mTextViewName;
    private TextView mTextViewEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        mTextViewName = findViewById(R.id.textViewName);
        mTextViewEmail = findViewById(R.id.textViewEmail);

        mButtonReports = findViewById(R.id.btnReports);
        mButtonReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();*/
                Intent i = new Intent(ProfileActivity.this, GenerateReportsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        mButtonAddOccurence = findViewById(R.id.btnAddOccurenceView);
        mButtonAddOccurence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, addOccurenceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        mButtonListOccurences = findViewById(R.id.btnToOccurenceListView);
        mButtonListOccurences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ListSymptomsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        getUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();
    }

    private void getUserInfo(){
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        mTextViewName.setText(name);
        mTextViewEmail.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuAcerca:
                Toast.makeText(this, "Seleccionado información", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuSignOut:
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }
}
