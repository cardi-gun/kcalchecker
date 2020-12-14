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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity {
    Button btnMeal, btnSnack, btnSave;
    LinearLayout layoutType;
    GridLayout recode_grid;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqlDB;
    String recode_name, recode_classify;
    boolean recode_flag;
    Intent intent;
    int title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        intent = getIntent();
        title = intent.getExtras().getInt("toDate");
        Log.d("받아온 값", title + "");
        //썰어낸 값
        Spliter ds = new Spliter();
        String day = ds.daySpliter(title);
        Log.d("썰어낸 값", day + "");

        TextView recode_date = findViewById(R.id.recode_date);
        recode_date.setText("날짜 : " + day);
        btnMeal = findViewById(R.id.btnMeal);
        btnSnack = findViewById(R.id.btnSnack);
        layoutType = findViewById(R.id.layoutType);
        recode_grid = findViewById(R.id.recode_grid);

        dataBaseHelper = new DataBaseHelper(this);

        //저장할때 담을 변수 저장장소, 다른거 선택했을때 확인하기 위해
        recode_name = null;
        recode_classify = null;
        recode_flag = true;

        final String[] list = {"한식", "양식", "일,중식", "분식", "음료", "제빵"};
        btnMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutType.removeAllViews();
                recode_classify = "식사";
                for (int i = 0; i < 3; i++) {
                    Button button = new Button(getApplicationContext());
                    button.setText(list[i]);
                    button.setId(i);
                    layoutType.addView(button);
                    Log.d("생성된 아이디", button.getId() + "");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//1.카테고리를 누르면 그에 해당하는 DB  while무안에서 버튼으로가져오기
                            recode_grid.removeAllViews();
                            int btnid = v.getId();
                            Log.d("클릭된 아이디", btnid + "");
                            sqlDB = dataBaseHelper.getWritableDatabase();
                            Cursor cursor;//2.SQL 보내서 자료 가져오기
                            cursor = sqlDB.rawQuery("SELECT fName FROM food WHERE fType ='" + btnid + "'", null);
                            String fName = null;
                            while (cursor.moveToNext()) {//3. 반복해서 버튼 출력
                                fName = cursor.getString(0);
                                final Button foodBtn = new Button(getApplicationContext());
                                foodBtn.setWidth(450);
                                foodBtn.setHeight(300);
                                foodBtn.setTextSize(14);
                                foodBtn.setBackgroundColor(Color.LTGRAY);
                                foodBtn.setText(fName);
                                recode_grid.addView(foodBtn);
                                //4. while문으로 만들어진 버튼 클릭했을때 동작구현
                                foodBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (recode_flag == true && recode_name == null) {
                                            recode_name = foodBtn.getText().toString();
                                            Toast.makeText(getApplicationContext(), recode_name + "을 클릭함", Toast.LENGTH_SHORT).show();
                                            foodBtn.setBackgroundColor(Color.DKGRAY);
                                            Log.d("저장된 이름", recode_name + "");
                                            recode_flag = false;
                                        } else if (recode_flag == false && recode_name.equals(foodBtn.getText().toString())) {
                                            recode_name = null;
                                            Toast.makeText(getApplicationContext(), "해제되었습니다.", Toast.LENGTH_SHORT).show();
                                            foodBtn.setBackgroundColor(Color.LTGRAY);
                                            Log.d("저장된 이름", recode_name + "");
                                            recode_flag = true;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "다시선택해주세요.", Toast.LENGTH_SHORT).show();
                                            Log.d("저장된 이름", recode_name + "");
                                        }
                                    }
                                });
                            }
                            cursor.close();
                            sqlDB.close();
                        }
                    });
                }
            }
        });


        btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutType.removeAllViews();
                recode_classify = "간식";


                for (int i = 3; i < 6; i++) {
                    Button button = new Button(getApplicationContext());
                    button.setText(list[i]);
                    button.setId(i);

                    layoutType.addView(button);
                    Log.d("생성된 아이디", button.getId() + "");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//1.카테고리를 누르면 그에 해당하는 DB  while무안에서 버튼으로가져오기
                            recode_grid.removeAllViews();
                            int btnid = v.getId();
                            Log.d("클릭된 아이디", btnid + "");
                            sqlDB = dataBaseHelper.getWritableDatabase();
                            Cursor cursor;
                            cursor = sqlDB.rawQuery("SELECT fName FROM food WHERE fType ='" + btnid + "'", null);//2.SQL 보내서 자료 가져오기
                            String fName = null;
                            while (cursor.moveToNext()) {//3. 반복해서 버튼 출력
                                fName = cursor.getString(0);
                                final Button foodBtn = new Button(getApplicationContext());
                                foodBtn.setWidth(450);
                                foodBtn.setHeight(300);
                                foodBtn.setTextSize(14);
                                foodBtn.setBackgroundColor(Color.LTGRAY);
                                foodBtn.setText(fName);
                                //foodBtn.setId(fName);
                                recode_grid.addView(foodBtn);
                                //4. while문으로 만들어진 버튼 클릭했을때 동작구현
                                foodBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (recode_flag == true && recode_name == null) {
                                            recode_name = foodBtn.getText().toString();
                                            Toast.makeText(getApplicationContext(), recode_name + "을 클릭함", Toast.LENGTH_SHORT).show();
                                            foodBtn.setBackgroundColor(Color.DKGRAY);
                                            Log.d("저장된 이름", recode_name + "");
                                            recode_flag = false;
                                        } else if (recode_flag == false && recode_name.equals(foodBtn.getText().toString())) {
                                            recode_name = null;
                                            Toast.makeText(getApplicationContext(), "해제되었습니다.", Toast.LENGTH_SHORT).show();
                                            foodBtn.setBackgroundColor(Color.LTGRAY);
                                            Log.d("저장된 이름", recode_name + "");
                                            recode_flag = true;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "다시선택해주세요.", Toast.LENGTH_SHORT).show();
                                            Log.d("저장된 이름", recode_name + "");

                                        }

                                    }
                                });
                            }
                            cursor.close();
                            sqlDB.close();


                        }
                    });
                }
//                layoutMeal.setVisibility(View.GONE);
//                layoutSnack.setVisibility(View.VISIBLE);
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recode_name != null) {
                    Toast.makeText(getApplicationContext(), "저장되었습니다!.", Toast.LENGTH_SHORT).show();
                    sqlDB = dataBaseHelper.getWritableDatabase();
                    Log.d("insert db연결", "성공");
                    sqlDB.execSQL("INSERT INTO recode VALUES( NULL ,'"
                            + recode_classify + "','"
                            + title + "','"
                            + recode_name + "');");
                    Log.d("insert 입력", "성공");
                    sqlDB.close();
                    Log.d("insert db연결", "종료");

                    Intent intent1 = new Intent(RecordActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "음식을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RecordActivity.this, DaysActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
