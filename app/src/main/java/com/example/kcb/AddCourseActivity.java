package com.example.kcb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(true);

        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.week);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);
        final EditText dsz = (EditText) findViewById(R.id.dsz);

        Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
                String dan = dsz.getText().toString();
                String d = "";
                Log.d("dan到底是多少",dan);
                if(dan.equals("0"))
                    d = "单周";
                else if (dan.equals("1"))
                    d = "双周";
                else if (dan.equals("2"))
                    d = "全周";
                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {
                    Course course = new Course(courseName, teacher, classRoom,
                            Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end), d);
                    Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                    intent.putExtra("course", course);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
