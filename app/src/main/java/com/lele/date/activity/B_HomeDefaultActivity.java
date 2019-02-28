package com.lele.date.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lele.date.R;

public class B_HomeDefaultActivity extends AppCompatActivity {

    Button b_registerfirm,b_joininfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_default);
        init();
        addListener();
    }
    public void init(){
        b_registerfirm= findViewById(R.id.register_firm);
        b_joininfirm = findViewById(R.id.joinin_firm);
    }
    public void addListener()
    {
        b_registerfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(B_HomeDefaultActivity.this, B_AddFirmActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        b_joininfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(B_HomeDefaultActivity.this, B_BindFirmActivity.class));
                overridePendingTransition(0, 0);
            }
        });

    }
}
