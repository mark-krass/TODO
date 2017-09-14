package com.example.gambm.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityCreateGoal extends AppCompatActivity {

    @BindView(R.id.etGoal)
    protected EditText etGoal;

    @BindView(R.id.etDescription)
    protected EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnSave, R.id.btnBack})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave://Просто передаем описание и задачу в главное меню для сохранения в БД и добавления в пункт меню
                Intent intent = new Intent();
                intent.putExtra("goal", etGoal.getText().toString());
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
