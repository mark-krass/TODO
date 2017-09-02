package com.example.gambm.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityTwo extends AppCompatActivity implements View.OnClickListener{

    Button btnSave, btnBack;
    EditText etGoal, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        etGoal = (EditText) findViewById(R.id.etGoal);

        etDescription = (EditText) findViewById(R.id.etDescription);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnSave://Просто передаем описание и задачу в главное меню для сохранения в БД и добавления в пункт меню
                Intent intent = new Intent();
                intent.putExtra("goal",etGoal.getText().toString());
                intent.putExtra("des", etDescription.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btnBack:
                Toast.makeText(this, "Данные не сохранены", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}
