package com.example.yantu.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yantu.androidproject.Entity.Hobby;

public class EditHobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hobby_activity);
        Hobby hobby = (Hobby)getIntent().getSerializableExtra("Hobby");
    }
}
