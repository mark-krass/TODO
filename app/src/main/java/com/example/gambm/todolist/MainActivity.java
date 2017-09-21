package com.example.gambm.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lvSimple)
    protected ListView lvSimple;

    private final int REQUEST_CODE_ADD = 1;
    private final int REQUEST_CODE_DONE = 2;
    private final String SAVED_LIST = "saved_list";

    private SharedPreferences sPref;
    private ArrayList<UserInfo> list = new ArrayList<>();
    //private UserInfo obj = new UserInfo();
    private BoxAdapter boxAdapter;

    private final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dataOut();
    }

    private void dataOut() {
        sPref = getPreferences(MODE_PRIVATE);
        String saved_list = sPref.getString(SAVED_LIST, null);
        if (saved_list != null) {
            Log.d(LOG_TAG, "0");

            Type listType = new TypeToken<ArrayList<UserInfo>>(){}.getType();
            List<UserInfo> newList = new Gson().fromJson(saved_list, listType);
            list.addAll(newList);

            Log.d(LOG_TAG, "1");
            Adapterwrite();
        }
    }

    @OnItemClick(R.id.lvSimple)
    protected void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ActivityDone.class);
        intent.putExtra("position", position);
        /*obj = list.get(position);
        intent.putExtra("goal", obj.getGoal());
        intent.putExtra("des", obj.getDes());*/
        UserInfo obj = list.get(position);
        intent.putExtra("goal", obj.getGoal());
        intent.putExtra("des", obj.getDes());
        startActivityForResult(intent, REQUEST_CODE_DONE);
    }

    @OnClick(R.id.btnGoal)
    protected void onClick() {
        startActivityForResult(new Intent(MainActivity.this, ActivityCreateGoal.class), REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD://Pref и список  складируем
                    DatawriteAdd(data);
                    break;
                case REQUEST_CODE_DONE:
                    DatawriteDone(data);
            }
        }
        Adapterwrite();
    }

    private void DatawriteAdd(Intent data) {//Записываем в Pref
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = sPref.edit();
        /*obj.setGoal(data.getStringExtra("goal"));
        obj.setDes(data.getStringExtra("des"));
        list.add(obj);Вот тут все проблемы. В строчке ниже присутствует модификатор new, который решает проблему. Сюда его поставить не получилось*/
        list.add(new UserInfo(data.getStringExtra("goal"), data.getStringExtra("des")));
        Gson gson = new Gson();
        String json = gson.toJson(list);
        edit.putString(SAVED_LIST, json);
        edit.commit();
    }

    private void DatawriteDone(Intent data) {
        list.remove(data.getIntExtra("position", 0));
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        edit.putString(SAVED_LIST, json);
        edit.commit();
    }

    private void Adapterwrite() {//Записываем в адаптер
        Log.d(LOG_TAG, "2");
        boxAdapter = new BoxAdapter(this, list);
        Log.d(LOG_TAG, "3");
        lvSimple.setAdapter(boxAdapter);
        Log.d(LOG_TAG, "4");
    }
}
