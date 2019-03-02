package com.example.kcb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

public class add_thing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing);

        EditText miaoshu = (EditText)findViewById(R.id.miaoshu);
    }
}
