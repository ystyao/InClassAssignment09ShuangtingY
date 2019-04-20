package com.example.inclassassignment09_shuangtingy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userReference;
    private FirebaseAuth msAuth;
    private FirebaseAuth.AuthStateListener authListener;

    EditText questionField;
    EditText answerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionField = (EditText) findViewById(R.id.question_field);
        answerField = (EditText) findViewById(R.id.answer_field);

        userReference = database.getReference("HardCodedUser");

        msAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    userReference = database.getReference(user.getUid());
                } else {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
            }
        };

    }

    public void sendToFirebase(View view) {
        String q = questionField.getText().toString();
        String a = answerField.getText().toString();
        TestItem testItem = new TestItem(q, a);
        userReference.push().setValue(testItem);
    }

    protected void onStart() {
        super.onStart();
        msAuth.addAuthStateListener(authListener);
    }

    @Override

    public void onStop() {
        super.onStop();
        msAuth.removeAuthStateListener(authListener);
    }


    public void logOut(View view) {
        msAuth.signOut();
    }
}
