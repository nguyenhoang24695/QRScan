package com.usol.gettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signInHandle(View view){
        EditText phone = (EditText)findViewById(R.id.editTextPhone);
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("phone",phone.toString());
        startActivity(intent);
        finish();
    }
}