package com.example.gambm.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "myLogs";

    private DB DataBase;

    private SimpleAdapter sAdapter;
    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> m;

    private final String ATTRIBUTE_NAME_GOAL = "GOAL";
    private final int REQUEST_CODE_ADD = 1;
    private final int REQUEST_CODE_DONE = 2;

    @BindView(R.id.lvSimple)
    protected ListView lvSimple;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dataOut();//Вывод всех данных
    }

    private void dataOut() {
        DataBase = new DB(this);
        data = new ArrayList<Map<String, Object>>();
        SQLiteDatabase db = DataBase.getWritableDatabase();
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

    @OnItemClick(R.id.lvSimple)
    protected void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ActivityDone.class);//"android.intent.action.done");//Открываем задачу
        intent.putExtra("id", position);//Передаем id для отображения текста и задачи
        startActivityForResult(intent, REQUEST_CODE_DONE);
    }

    @OnClick(R.id.btnGoal)
    protected void onClick() {
        startActivityForResult(new Intent(MainActivity.this, ActivityCreateGoal.class), REQUEST_CODE_ADD);
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

    private void Datawrite(Intent data1) {//Записываем в БД
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = DataBase.getWritableDatabase();
        cv.put("goal", data1.getStringExtra("goal"));
        cv.put("des", data1.getStringExtra("des"));
        long rowID = db.insert("mytable", null, cv);//Новую задачу поместили в БД
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    }

    private void Adapterwrite(Intent data1) {//Записываем в адаптер
        m = new HashMap<String, Object>();
        m.put(ATTRIBUTE_NAME_GOAL, data1.getStringExtra("goal"));
        data.add(m);
        String[] from = {ATTRIBUTE_NAME_GOAL};
        int[] to = {R.id.tvText};
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        lvSimple.setAdapter(sAdapter);//Новый пункт добавили в список
    }
}
