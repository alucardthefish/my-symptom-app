package com.sop.firebasech2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sop.firebasech2.objetos.Router;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private ProgressBar mProgressBarLogin;

    private String email = "";
    private String password = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEditTextEmail = findViewById(R.id.editTextEmailLogin);
        mEditTextPassword = findViewById(R.id.editTextPasswordLogin);
        mProgressBarLogin = findViewById(R.id.progressBarLogin);
        mProgressBarLogin.setVisibility(ProgressBar.GONE);

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser();
                } else {
                    Toast.makeText(LoginActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        activateProgressBar(true);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(LoginActivity.this, "Acceso autorizado. Cargando datos...", Toast.LENGTH_LONG).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    Router router = new Router(LoginActivity.this);
                    router.goto_profile(currentUser);

                } else {
                    activateProgressBar(false);
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesión, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void activateProgressBar(boolean active){
        mEditTextEmail.setEnabled(!active);
        mEditTextPassword.setEnabled(!active);
        mButtonLogin.setEnabled(!active);
        if (active){
            mProgressBarLogin.setVisibility(ProgressBar.VISIBLE);
        } else {
            mProgressBarLogin.setVisibility(ProgressBar.GONE);
        }
    }
}
