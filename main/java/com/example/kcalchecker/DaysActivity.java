package com.example.kcalchecker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class DaysActivity extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    ListView listView;
    Button btnCreate, btnDelete;
    SQLiteDatabase sqlDB;
    ArrayList listarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        //전달 받아온 값
        Intent intent = getIntent();
        final int title = intent.getExtras().getInt("toDate");
        Log.d("받아온 값", title + "");
        //변경된 값
        final Spliter spliter = new Spliter();
        String day = spliter.daySpliter(title);
        Log.d("변경한 값", day + "");

        TextView tv1 = findViewById(R.id.date);
        tv1.setText(day);

        btnCreate = findViewById(R.id.btnCreate);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);
        //어탭터에 담을 리스트 생성
        listarray = new ArrayList();

        dataBaseHelper = new DataBaseHelper(this);
        sqlDB = dataBaseHelper.getWritableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from food join recode on fName = rFood where rDATE = '" + title + "'", null);
        int total = 0; //하루 총 칼로리량 누적할 변수
        if (cursor != null) {
            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append("NO "+cursor.getString(3) + "       "
                        //+cursor.getString(5)+"     "
                        + cursor.getString(4) + "       "
                        + cursor.getString(0) + "       "
                        + cursor.getString(1) + "Kcal   \n");
                listarray.add(sb);
                total = total + Integer.parseInt(cursor.getString(1));
            }
            Log.d("어레이리스트안에", listarray + "");
            Log.d("어레이리스트안에", total + "총 칼로리");
        } else {
            Log.d("데이터가", "없음");
        }

        TextView tv2 = findViewById(R.id.total);
        tv2.setText(total + "Kcal");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, listarray);
        listView.setAdapter(adapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaysActivity.this, RecordActivity.class);
                intent.putExtra("toDate", title);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DaysActivity.this);

                builder.setTitle("삭제확인");
                builder.setMessage("정말 삭제 하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int count, checked;
                        count = adapter.getCount() ;
                        if (count > 0) {
                            // 현재 선택된 아이템의 리스트안에 위치 저장
                            checked = listView.getCheckedItemPosition();

                            //app에서 minSdkVersion을 19 변경해줘야함
                            String str = Objects.toString(listarray.get(checked));
                            int no = spliter.noSpliter(str);

                            if (checked > -1 && checked < count) {
                                sqlDB = dataBaseHelper.getWritableDatabase();//DB에 연결
                                sqlDB.execSQL("DELETE FROM recode WHERE rNO = '"+no+"'");//SQL문 전달
                                sqlDB.close();//연결 종료

                                listarray.remove(checked);// 리스트에서 아이템 삭제
                                listView.clearChoices();// listview 선택 초기화.
                                adapter.notifyDataSetChanged();// listview 갱신

                                Intent intent = new Intent(DaysActivity.this, DaysActivity.class);
                                intent.putExtra("toDate", title);
                                startActivity(intent);
                                finish();


                            }else{
                                Toast.makeText(getApplicationContext(), "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "삭제했습니다!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                        // listview 선택 초기화.
                        listView.clearChoices();
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DaysActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
