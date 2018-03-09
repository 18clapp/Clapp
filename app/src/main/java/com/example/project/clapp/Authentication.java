package com.example.project.clapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentication extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private EditText emailUser;
    private EditText passUser;
    private TextView errorMessage;
    private Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        emailUser = findViewById(R.id.inputEmail);
        passUser = findViewById(R.id.inputPass);
        errorMessage = findViewById(R.id.errorMessage);
        logIn = findViewById(R.id.buttonLogin);

        Log.d(TAG, "abri");
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailUser.getText().toString();
                String password = passUser.getText().toString();
                Log.d(TAG, "ola" + email);
                Log.d(TAG, "ola" + password);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Authentication.this, HomePageActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    errorMessage.setText("User does not exist");
                                }
                            }
                        });
            }
        });

    }


}
