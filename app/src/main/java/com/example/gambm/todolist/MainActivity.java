package com.example.gambm.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.example.gambm.todolist.R.id.tvText;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "myLogs";

    private SimpleAdapter sAdapter;
    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> m;

    private final String ATTRIBUTE_NAME_GOAL = "GOAL";

    private final int REQUEST_CODE_ADD = 1;
    private final int REQUEST_CODE_DONE = 2;

    private SharedPreferences sPref;

    @BindView(R.id.lvSimple)
    protected ListView lvSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        data = new ArrayList<Map<String, Object>>();

        dataOut();//Вывод всех данных
    }

    private void dataOut() {
        Log.d(LOG_TAG, "0");
        Map<String, ?> keys = sPref.getAll();
        Log.d(LOG_TAG, "1");
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d(LOG_TAG, "2");
            Log.d(LOG_TAG, entry.getKey() + ": " + entry.getValue().toString());
            Log.d(LOG_TAG, "3");
        }
    }

    @OnItemClick(R.id.lvSimple)
    protected void onItemClick(int position, View v) {
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
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(data1.getStringExtra("goal"), data1.getStringExtra("goal"));
        ed.putString(data1.getStringExtra("goal") + "1", data1.getStringExtra("des"));
    }

    private void Adapterwrite(Intent data1) {//Записываем в адаптер
        m = new HashMap<String, Object>();
        m.put(ATTRIBUTE_NAME_GOAL, data1.getStringExtra("goal"));
        data.add(m);
        String[] from = {ATTRIBUTE_NAME_GOAL};
        int[] to = {tvText};
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        lvSimple.setAdapter(sAdapter);//Новый пункт добавили в список
    }
}
