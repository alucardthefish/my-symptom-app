package com.sop.firebasech2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button mButtonSignOut;
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

        mButtonSignOut = findViewById(R.id.btnSignout);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
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
}
