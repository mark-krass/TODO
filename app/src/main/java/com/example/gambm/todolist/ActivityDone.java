package com.example.gambm.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDone extends AppCompatActivity implements View.OnClickListener{

    Button btnDone, btnCancel;
    TextView tvGoal, tvDescription;
    private int id;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        tvGoal = (TextView) findViewById(R.id.tvGoal);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);//Получаем id, который удалить или посмотреть

        dbHelper = new DBHelper(this);
        //datadel();
    }

    private void datadel(){//Вывод на экран задачи и описания
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        c.moveToPosition(id);//По полученному id смещаемся к строке и отображем человеку задачу с описанием
        int goalColIndex = c.getColumnIndex("goal");
        int desColIndex = c.getColumnIndex("des");
        tvGoal.setText(c.getString(goalColIndex));
        tvDescription.setText(c.getString(desColIndex));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
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
