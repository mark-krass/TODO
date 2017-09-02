package com.example.gambm.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnGoal;
    final String LOG_TAG = "myLogs";

    DBHelper dbHelper;

    ListView lvSimple;
    SimpleAdapter sAdapter;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    final String ATTRIBUTE_NAME_GOAL = "GOAL";
    final int REQUEST_CODE_ADD = 1;
    final int REQUEST_CODE_DONE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoal = (Button) findViewById(R.id.btnGoal);
        btnGoal.setOnClickListener(this);

        lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {//Когда нажимают на пункт меню чтобы посмотреть описание или его удалить
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("android.intent.action.done");//Открываем задачу
                intent.putExtra("id", position);//Передаем id для отображения текста и задачи
                startActivityForResult(intent, REQUEST_CODE_DONE);
            }
        });
        dbHelper = new DBHelper(this);
        data = new ArrayList<Map<String, Object>>();
        start();//Вывод всех данных
    }

    private void start(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.moveToFirst()) {//Если база данных не пуста, заполняем все названиями задач
            int goalColIndex = c.getColumnIndex("goal");
            m = new HashMap<String, Object>();
            String[] from = {ATTRIBUTE_NAME_GOAL};
            int[] to = {R.id.tvText};
            do {
                m.put(ATTRIBUTE_NAME_GOAL, c.getString(goalColIndex));
                data.add(m);
                sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
                lvSimple.setAdapter(sAdapter);
            } while (c.moveToNext());
        }
    }

    @Override
    public void onClick(View v)//Создаем новую задачу
    {
        Intent intent = new Intent(this, ActivityTwo.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD://БД и список  складируем
                    Datawrite(data1);
                    Adapterwrite(data1);
                    break;
                case REQUEST_CODE_DONE:
                    data.remove(data1.getIntExtra("id", -1));//Удалили пункт меню
                    sAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void Datawrite(Intent data1){//Записываем в БД
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("goal", data1.getStringExtra("goal"));
        cv.put("des", data1.getStringExtra("des"));
        long rowID = db.insert("mytable", null, cv);//Новую задачу поместили в БД
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    private void Adapterwrite(Intent data1){//Записываем в адаптер
        m = new HashMap<String, Object>();
        m.put(ATTRIBUTE_NAME_GOAL, data1.getStringExtra("goal"));
        data.add(m);
        String[] from = {ATTRIBUTE_NAME_GOAL};
        int[] to = {R.id.tvText};
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        lvSimple.setAdapter(sAdapter);//Новый пункт добавили в список
    }
}
