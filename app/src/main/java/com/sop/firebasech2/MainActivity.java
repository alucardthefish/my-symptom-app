package com.sop.firebasech2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sop.firebasech2.objetos.FirebaseReferences;
import com.sop.firebasech2.objetos.Router;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegister;
    private Button mButtonToLogin;
    private ProgressBar mProgressBar;

    // Variables de los datos que vamos a registrar
    private String name = "";
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(getResources().getString(R.string.label_user_register));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mProgressBar = findViewById(R.id.progressBarRegister);
        mProgressBar.setVisibility(ProgressBar.GONE);

        mEditTextName = findViewById(R.id.editTextName);
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mButtonRegister = findViewById(R.id.btnRegister);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEditTextName.getText().toString();
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if (password.length() >= 6){
                        registerUser();
                    }
                    else {
                        Toast.makeText(MainActivity.this, R.string.toast_password_validation, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,R.string.validate_fields_login, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonToLogin = findViewById(R.id.btn_tologin);
        mButtonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //updateUI(currentUser);
            Router router = new Router(MainActivity.this);
            router.goto_profile(currentUser);
        }
    }

    private void registerUser(){
        activateProgressBar(true);
        Toast.makeText(MainActivity.this, R.string.toast_register_user_transaction, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()){
                     Map<String, Object> map = new HashMap<>();
                     map.put("name", name);
                     map.put("email", email);
                     map.put("password", password);

                     FirebaseUser user = mAuth.getCurrentUser();

                     //Setting display name as name on db
                     UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                             .setDisplayName(name).build();
                     user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task1) {
                             if (task1.isSuccessful()){
                                 Log.d("Display name: ", "set Correctly");
                             } else {
                                 Log.d("Display name: ", "not set Correctly");
                             }
                         }
                     });

                     String id = user.getUid();

                     mDatabase.child(FirebaseReferences.APP_REFERENCE).child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task2) {
                             if (task2.isSuccessful()){
                                 Toast.makeText(MainActivity.this, R.string.toast_success_user_register, Toast.LENGTH_LONG).show();
                                 FirebaseUser currentUser = mAuth.getCurrentUser();

                                 Router router = new Router(MainActivity.this);
                                 router.goto_profile(currentUser);
                             } else {
                                 activateProgressBar(false);
                                 Toast.makeText(MainActivity.this, R.string.toast_fail_user_register, Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 } else {
                     activateProgressBar(false);
                     Toast.makeText(MainActivity.this, getResources().getText(R.string.toast_fail_user_register_two) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    private void activateProgressBar(boolean active){
        mEditTextName.setEnabled(!active);
        mEditTextEmail.setEnabled(!active);
        mEditTextPassword.setEnabled(!active);
        mButtonRegister.setEnabled(!active);
        mButtonToLogin.setEnabled(!active);
        if (active){
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }
}
