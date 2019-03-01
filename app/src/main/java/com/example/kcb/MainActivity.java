package com.example.kcb;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    /**
     * 星期几
     */
    private RelativeLayout day;


    /**
     * SQLite Helper类
     */
    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //从数据库读取数据
        loadData();

        createLeftView(13);
    }

    /**
     * 从数据库加载数据
     */
    private void loadData() {
        ArrayList<Course> coursesList = new ArrayList<>();
        //课程列表
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end")),
                        cursor.getString(cursor.getColumnIndex("dsz")),
                        cursor.getString(cursor.getColumnIndex("homework"))));
                Log.d("数据库信息：", cursor.getString(cursor.getColumnIndex("homework")));
                Log.d("测试信息", "ddd");
            } while (cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {

            createItemCourseView(course);
        }
    }

    /**
     * 保存数据到数据库
     */
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end, dsz, homework) " + "values(?, ?, ?, ?, ?, ?, ?, ?)",
                        new String[]{course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay() + "",
                                course.getStart() + "",
                                course.getEnd() + "",
                                course.getDsz() + "",
                                course.getHomework()}
                        //单双周
                );
    }

    //创建左视图
    private void createLeftView(int n) {
        int endNumber = n;
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber - maxCoursesNumber; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
//                view.findViewById(R.id.left).setBackgroundColor(Color.parseColor("#00000000"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110, 180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    /**
     * 创建单个课程视图
     */
    private void createItemCourseView(final Course course) {
        final android.content.Context that = this;
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd()) {
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        } else {
            int dayId = 0;
            switch (getDay) {
                case 1:
                    dayId = R.id.monday;
                    break;
                case 2:
                    dayId = R.id.tuesday;
                    break;
                case 3:
                    dayId = R.id.wednesday;
                    break;
                case 4:
                    dayId = R.id.thursday;
                    break;
                case 5:
                    dayId = R.id.friday;
                    break;
                case 6:
                    dayId = R.id.saturday;
                    break;
                case 7:
                    dayId = R.id.weekday;
                    break;
            }
            day = findViewById(dayId);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null);
            //加载单个课程布局
            v.setY(height * (course.getStart() - 1));
            //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, (course.getEnd() - course.getStart() + 1) * height - 8);
            //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom());
            //显示课程名
            String [] a = {"#f17c67","#9966CC","#BDB76A","#008573","#FE4C40","#DE3163"};
            Random random = new Random();
            int n = random.nextInt(6);
            v.findViewById(R.id.CardView1).setBackgroundColor(Color.parseColor(a[n]));
            v.findViewById(R.id.CardView1).getBackground().setAlpha(200);
            day.addView(v);


            //单击查看课程详情
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(that);
                    LinearLayout CourseDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_course_info, null);
                    TextView tv_course_name = (TextView) CourseDialog.findViewById(R.id.tv_course_name);
                    TextView tv_teacher = (TextView) CourseDialog.findViewById(R.id.tv_teacher);
                    TextView tv_room = (TextView) CourseDialog.findViewById(R.id.tv_room);
                    TextView tv_time = (TextView) CourseDialog.findViewById(R.id.tv_time);
                    TextView tv_time_week = (TextView) CourseDialog.findViewById(R.id.tv_time_week);
                    TextView tv_dsz = (TextView) CourseDialog.findViewById(R.id.tv_dsz);
                    final TextView tv_homework = (TextView) CourseDialog.findViewById(R.id.homework);
                    tv_course_name.setText(course.getCourseName());
                    tv_teacher.setText(course.getTeacher());
                    tv_room.setText(course.getClassRoom());
                    tv_time.setText("第 " + course.getStart() + " - " + course.getEnd() + " 节");
                    tv_homework.setMovementMethod(ScrollingMovementMethod.getInstance());
                    tv_homework.setText(course.getHomework());
                    Log.d("获取到的课程作业为：", course.getHomework());
                    String day = String.valueOf(course.getDay());
                    if (day.equals("1")) {
                        day = "星期一";
                    } else if (day.equals("2")) {
                        day = "星期二";
                    } else if (day.equals("3")) {
                        day = "星期三";
                    } else if (day.equals("4")) {
                        day = "星期四";
                    } else if (day.equals("5")) {
                        day = "星期五";
                    } else if (day.equals("6")) {
                        day = "星期六";
                    } else if (day.equals("7")) {
                        day = "星期日";
                    }
                    tv_time_week.setText(day);
                    tv_dsz.setText(course.getDsz());
                    builder.setView(CourseDialog);

                    builder.setCancelable(true);
                    final AlertDialog dialog = builder.create();

                    dialog.show();
                    dialog.getWindow().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String work_before = tv_homework.getText().toString();
                            dialog.getWindow().findViewById(R.id.button2).setVisibility(View.GONE);
                            tv_homework.setVisibility(View.GONE);
                            dialog.getWindow().findViewById(R.id.button3).setVisibility(View.VISIBLE);

                            final String name = course.getCourseName();
                            dialog.getWindow().findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    EditText homework = (EditText) dialog.getWindow().findViewById(R.id.editText2);
                                    String work = homework.getText().toString();
                                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                                    sqLiteDatabase.execSQL("update courses set homework=? where course_name=?", new String[]{work, name});
                                    dialog.hide();
                                    loadData();
                                }
                            });
                        }
                    });
                }
            });
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog dialog = new AlertDialog.Builder(that)
                            .setTitle("是否删除")
                            //可以直接设置这三种button
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    v.setVisibility(View.GONE);
                                    //先隐藏
                                    day.removeView(v);
                                    //再移除课程视图
                                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                                    sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[]{course.getCourseName()});
                                    Log.v("testDialog", "click button ok");
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v("testDialog", "click button cancel");
                                }
                            })
                            .show();

                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final android.content.Context that = this;
        switch (item.getItemId()) {
            case R.id.add_courses:

//                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
//                startActivityForResult(intent, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(that);
                LinearLayout CourseDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_add_course, null);
                builder.setView(CourseDialog);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText inputCourseName = (EditText) dialog.getWindow().findViewById(R.id.course_name);
                        final EditText inputTeacher = (EditText) dialog.getWindow().findViewById(R.id.teacher_name);
                        final EditText inputClassRoom = (EditText) dialog.getWindow().findViewById(R.id.class_room);
                        final EditText inputDay = (EditText) dialog.getWindow().findViewById(R.id.week);
                        final EditText inputStart = (EditText) dialog.getWindow().findViewById(R.id.classes_begin);
                        final EditText inputEnd = (EditText) dialog.getWindow().findViewById(R.id.classes_ends);
                        final EditText dsz = (EditText) dialog.getWindow().findViewById(R.id.dsz);

                        Button okButton = (Button) dialog.getWindow().findViewById(R.id.button);
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
                                Log.d("dan到底是多少", dan);
                                if (dan.equals("0"))
                                    d = "单周";
                                else if (dan.equals("1")){
                                    d = "双周";
                                }
                                else if (dan.equals("2")){
                                    d = "全周";
                                }
                                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                                    Toast.makeText(MainActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                                } else {
                                    Course course = new Course(courseName, teacher, classRoom,
                                            Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end), d, "无");
                                    saveData(course);
                                    loadData();
                                    Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
                                    dialog.hide();
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.menu_about:
                Intent intent1 = new Intent(this, Settings.class);
                startActivity(intent1);
                break;
            case R.id.daka:
                Intent intent2 = new Intent(MainActivity.this, daka.class);
                startActivity(intent2);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表左边视图(节数)
            createLeftView(13);
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            saveData(course);
        }
    }



}