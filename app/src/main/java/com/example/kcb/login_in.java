package com.example.kcb;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.wang.avi.AVLoadingIndicatorView;

public class login_in extends AppCompatActivity {
    private AVLoadingIndicatorView avi;

    private static Handler handler = new Handler();

    /**
     * SQLite Helper类
     */
    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    private LoadingView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        final Context that = this;
        final Button login_btn = (Button) findViewById(R.id.btn_login);
        final EditText input_ID = (EditText) findViewById(R.id.input_ID);
        final EditText input_password = (EditText) findViewById(R.id.input_password);
        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loading = new LoadingView(that,R.style.CustomDialog);
                final String ID = input_ID.getText().toString();
                final String password = input_password.getText().toString();
                if (ID.equals("") || password.equals("")) {
                    Toast.makeText(login_in.this, "请输入账号和密码哦~", Toast.LENGTH_LONG).show();
                } else {
                    loading.show();
                    new Thread() {
                        public void run() {
                            getCourse(ID, password);
                        }
                    }.start();
                }
            }
        });


    }

    public void getCourse(String number, String psd) {
        Log.d("debug001", "lallallalal");
        List<String> course = new ArrayList<>();
        String username = number;
        String password = psd;
        try {
            Connection connect = Jsoup.connect("https://oauth.shu.edu.cn/oauth/authorize?response_type=code&client_id=yRQLJfUsx326fSeKNUCtooKw&redirect_uri=http%3a%2f%2fxk.autoisp.shu.edu.cn%2fpassport%2freturn");
            Document doc = connect.get();

            Elements ele = doc.body().select("input[name]");

            Connection.Response res = Jsoup.connect("https://sso.shu.edu.cn/idp/profile/SAML2/POST/SSO")
                    .data("SAMLRequest", ele.get(0).attr("value"), "RelayState", ele.get(1).attr("value"))
                    .method(Connection.Method.POST).timeout(10000).execute();
            //.followRedirects(false)
            Map<String, String> user_password = new HashMap<String, String>();
            user_password.put("j_username", username);
            user_password.put("j_password", password);

            doc = Jsoup.connect("https://sso.shu.edu.cn/idp/Authn/UserPassword")
                    .data(user_password)
                    .cookies(res.cookies())
                    .post();
            ele = doc.body().select("input[name]");

            Connection.Response res2 = Jsoup.connect("http://oauth.shu.edu.cn/oauth/Shibboleth.sso/SAML2/POST")
                    .data("SAMLResponse", ele.get(1).attr("value"), "RelayState", ele.get(0).attr("value"))
                    .method(Connection.Method.POST).timeout(10000).execute();

            doc = Jsoup.connect("http://xk.autoisp.shu.edu.cn/StudentQuery/CtrlViewQueryCourseTable")
                    .data("studentNo", username)
                    .cookies(res2.cookies())
                    .post();
            Elements ele2 = doc.body().select("tr");
            for (int i = 3; i < ele2.size(); i++) {
                if (ele2.get(i).getElementsByTag("td").text().equals("")) {
                    break;
                }
                course.add(ele2.get(i).getElementsByTag("td").text());
            }
        } catch (Exception e) {
            Log.d("debug001", e.toString());
            e.printStackTrace();
        }

//        List<List<String>> courseTimeList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from courses");
        for (int i = 0; i < course.size() - 1; i++) {
            Log.d("debug001", course.get(i));
            List<String> xingqi = new ArrayList<>(Arrays.asList("一", "二", "三", "四", "五"));

            List<String> list = new ArrayList<>();
            list = getInformations(course.get(i));
            for (int j = 4; j < list.size(); j++) {
                int day = 0;
                switch (list.get(j).charAt(0)) {
                    case '一':
                        day = 1;
                        break;
                    case '二':
                        day = 2;
                        break;
                    case '三':
                        day = 3;
                        break;
                    case '四':
                        day = 4;
                        break;
                    case '五':
                        day = 5;
                        break;
                    default:
                        day = 0;
                        break;
                }

                Log.d("debug001", day + "");
                String time_start = list.get(j).substring(1, list.get(j).indexOf("-"));
                String time_end = list.get(j).substring(list.get(j).indexOf("-") + 1);
                String d = "全周";
                Course course_1 = new Course(list.get(1), list.get(2), list.get(3),
                        Integer.valueOf(day), Integer.valueOf(time_start), Integer.valueOf(time_end), d, "无");
                saveData(course_1);

            }


        }
        Intent intent = new Intent(login_in.this, MainActivity.class);
        startActivity(intent);
//        for(List<String> i : courseTimeList) {
//            for(String s : i) {
//                Log.d("debug001",s);
//            }
//        }
    }

    public List<String> getInformations(String info) {
        List<String> strList = java.util.Arrays.asList(info.split(" "));
        List<String> list = new ArrayList<>();
        list.add(strList.get(1));
        list.add(strList.get(2));
        list.add(strList.get(4));
        list.add(strList.get(strList.size() - 4));
        String regex = "[一二三四五][0-9]+-[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(info);
        List<String> courseTime = new ArrayList<>();
        while (matcher.find()) {
            courseTime.add(matcher.group());
        }
        courseTime = courseTime.subList(0, courseTime.size() - 1);
        list.addAll(courseTime);
        return list;
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


}

