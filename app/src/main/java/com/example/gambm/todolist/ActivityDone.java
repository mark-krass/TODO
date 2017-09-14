package com.example.gambm.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDone extends AppCompatActivity {

    private int id;

    private DB DataBase;

    @BindView(R.id.tvGoal)
    protected TextView tvGoal;

    @BindView(R.id.tvDescription)
    protected TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);//Получаем id, который удалить или посмотреть

        DataBase = new DB(this);
        //datadel();
    }

    private void datadel() {//Вывод на экран задачи и описания
        SQLiteDatabase db = DataBase.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        c.moveToPosition(id);//По полученному id смещаемся к строке и отображем человеку задачу с описанием
        int goalColIndex = c.getColumnIndex("goal");
        int desColIndex = c.getColumnIndex("des");
        tvGoal.setText(c.getString(goalColIndex));
        tvDescription.setText(c.getString(desColIndex));
    }

    @OnClick({R.id.btnDone, R.id.btnCancel})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone://Обнуляем базу данных и передаем id списка, который нужно удалить в главном меню
                Intent intent = new Intent();
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                /*SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("mytable", "id = " + id, null);*/
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
