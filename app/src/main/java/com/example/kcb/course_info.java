package com.example.kcb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class course_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        Button homework = (Button)findViewById(R.id.button2);
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(course_info.this,"test", Toast.LENGTH_LONG);
                Intent intent = new Intent(course_info.this, Add_homework.class);
                startActivity(intent);
            }
        });
    }
}
