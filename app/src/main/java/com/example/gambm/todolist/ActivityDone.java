package com.example.gambm.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDone extends AppCompatActivity {

    @BindView(R.id.tvGoal)
    protected TextView tvGoal;

    @BindView(R.id.tvDescription)
    protected TextView tvDescription;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        ButterKnife.bind(this);

        dataOut();
    }

    private void dataOut() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        tvGoal.setText(intent.getStringExtra("goal"));
        tvDescription.setText(intent.getStringExtra("des"));
    }

    @OnClick({R.id.btnDone, R.id.btnCancel})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone://Обнуляем базу данных и передаем id списка, который нужно удалить в главном меню
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "Цель удалена", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
