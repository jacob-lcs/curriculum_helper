package com.example.kcb;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class daka extends AppCompatActivity {

    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    private List<thing> thingList = new ArrayList<>();

    /**
     * 保存数据到数据库
     */
    private void saveData(thing thing) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into thing(thing_name, thing_day, thing_info, thing_icon, date) " + "values(?, ?, ?, ?,?)",
                        new String[]{thing.getName() + "",
                                thing.getDay() + "",
                                thing.getMiaoshu() + "",
                                thing.getThing_icon() + "",
                                thing.getDate()}
                );
    }

    private void deleteData(String name) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("delete from thing where thing_name=?",
                        new String[]{name}
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daka);
        final android.content.Context that = this;
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_thing);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(that);
                LinearLayout ThingDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_add_thing, null);
                builder.setCancelable(true);
                builder.setView(ThingDialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                String icons[] = {"@drawable/alarm", "@drawable/breakfast", "@drawable/english", "@drawable/exercise", "@drawable/fruit", "@drawable/rainbow", "@drawable/reading", "@drawable/sleep", "@drawable/smile", "@drawable/snacks", "@drawable/water"};
                final EditText name = dialog.getWindow().findViewById(R.id.name);
                final EditText miaoshu = dialog.getWindow().findViewById(R.id.miaoshu);

                final int thing_day = 0;
                Random random = new Random();
                int n = random.nextInt(10);
                final String thing_icon = icons[n];

                Button submit = (Button) dialog.getWindow().findViewById(R.id.button5);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String thing_name = name.getText().toString();
                        final String thing_info = miaoshu.getText().toString();

                        thing new_thing = new thing(thing_name, thing_day, thing_info, thing_icon, "");
                        saveData(new_thing);
                        Log.d("test", thing_name);
                        initThing();
                        DakaAdapter adapter = new DakaAdapter(daka.this, R.layout.list_item, thingList);
                        ListView listView = (ListView) findViewById(R.id.daka_list);

                        listView.setAdapter(adapter);
                        dialog.hide();

                    }
                });

            }
        });

        initThing();
        DakaAdapter adapter = new DakaAdapter(daka.this, R.layout.list_item, thingList);
        ListView listView = (ListView) findViewById(R.id.daka_list);

        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog dialog = new AlertDialog.Builder(that)
                        .setTitle("是否删除")
                        //可以直接设置这三种button
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                thing thing = thingList.get(i);
                                deleteData(thing.getName());
                                initThing();
                                DakaAdapter adapter = new DakaAdapter(daka.this, R.layout.list_item, thingList);
                                ListView listView = (ListView) findViewById(R.id.daka_list);

                                listView.setAdapter(adapter);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                thing thing1 = thingList.get(i);
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from thing", null);
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = sdf.format(calendar.getTime());
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex("thing_name")).equals(thing1.getName())) {
                            Log.d("数据库日期为", cursor.getString(cursor.getColumnIndex("date")));
                            Log.d("系统日期为", dateStr);
                            if (cursor.getString(cursor.getColumnIndex("date")).equals(dateStr)) {
                                Toast.makeText(daka.this, "今日已打卡", Toast.LENGTH_LONG).show();
                            } else {
                                sqLiteDatabase.execSQL
                                        ("update thing set date=?,thing_day=? where thing_name=?",
                                                new String[]{dateStr, String.valueOf(thing1.getDay() + 1), thing1.getName()}
                                        );
                                Toast.makeText(daka.this, "打卡成功", Toast.LENGTH_LONG).show();
                                initThing();
                                DakaAdapter adapter = new DakaAdapter(daka.this, R.layout.list_item, thingList);
                                ListView listView = (ListView) findViewById(R.id.daka_list);

                                listView.setAdapter(adapter);
                            }
                        }
                    } while (cursor.moveToNext());


                }
            }
        });

    }

    private void initThing() {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from thing", null);
        thingList.clear();
        if (cursor.moveToFirst()) {
            do {
                thingList.add(new thing(
                        cursor.getString(cursor.getColumnIndex("thing_name")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("thing_day"))),
                        cursor.getString(cursor.getColumnIndex("thing_info")),
                        cursor.getString(cursor.getColumnIndex("thing_icon")),
                        cursor.getString(cursor.getColumnIndex("date"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
