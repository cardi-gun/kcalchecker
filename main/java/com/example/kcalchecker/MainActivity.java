package com.example.kcalchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqlDB;

    GridLayout calendar_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateCalender();

    }

    public void updateCalender() {
        Calendar calendar = Calendar.getInstance();//원본
        Calendar cloneCal = (Calendar) calendar.clone();//복사본

        cloneCal.set(Calendar.DAY_OF_MONTH, 1); // 달의 일자를 1로 변경
        final int Start = cloneCal.get(calendar.DAY_OF_WEEK) - 1;// 주의 일자 -1(index 0부터)

        cloneCal.add(Calendar.DAY_OF_MONTH, -Start);//시작 일자를 1로 변경
        Date nowDate = cloneCal.getTime(); //오늘 날짜 저장

        calendar_grid = findViewById(R.id.calendar_grid);//그리드 불러오기

        for (int i = 0; i < 42; i++) {
            Date monthDate = cloneCal.getTime();

            SimpleDateFormat dataformat2 = new SimpleDateFormat("dd");
            SimpleDateFormat dataformat3 = new SimpleDateFormat("yyyyMMdd");
            String calDay = dataformat2.format(monthDate);
            String toDate = dataformat3.format(monthDate);

            SimpleDateFormat dataformat4 = new SimpleDateFormat("MM");
            String month = dataformat4.format(nowDate);
            int monthcheck = cloneCal.get(Calendar.MONTH);

            Log.d("화면 첫 일자가 될부분", month + "와" + monthcheck);
            //TextView 생성, 규격 규정
            TextView tv = new TextView(getApplicationContext());
            tv.setWidth(205);
            tv.setPadding(10, 0, 0, 0);
            tv.setHeight(325);
            tv.setTextSize(16);

            //색상변경
            int color = cloneCal.get(calendar.DAY_OF_WEEK);
            if (Integer.parseInt(month) != monthcheck) {
                tv.setTextColor(Color.GRAY);
            } else {
                if (color == 1) {
                    tv.setTextColor(Color.RED);
                } else if (color == 7) {
                    tv.setTextColor(Color.BLUE);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
            }
            //TextView ID,
            tv.setId(Integer.parseInt(toDate));

            //db갔다와서 하루 총량 칼로리 가져올것
            dataBaseHelper = new DataBaseHelper(this);
            sqlDB = dataBaseHelper.getWritableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("select fKcal from food join recode on fName = rFood where rDATE = '" + toDate + "'", null);
            int total = 0;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    total = total + Integer.parseInt(cursor.getString(0));
                }
                Log.d("결과는", total + "");

                tv.setText(calDay + "\n\n" + total + "\nKcal");
                calendar_grid.addView(tv);
                Log.d("아이디", toDate + "");
                Log.d("색상조정", color + "");

                //반복문 도는동안 1씩 증가.
                cloneCal.add(Calendar.DAY_OF_MONTH, +1);
                //각 동작에 반복해서 들어갈 것.
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DaysActivity.class);
                        intent.putExtra("toDate", v.getId());
                        Log.d("아이디", v.getId() + "");
                        startActivity(intent);
                        finish();
                    }
                });
            }


        }

    }
}
