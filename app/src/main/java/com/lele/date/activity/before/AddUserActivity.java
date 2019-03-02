package com.lele.date.activity.before;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lele.date.R;
import com.lele.date.faker.Server;

public class AddUserActivity extends AppCompatActivity {

    EditText username,pwd,pwd_confirm;
    Button b_uploadface,b_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.pwd);
        pwd_confirm = findViewById(R.id.pwd_confirm);
        b_uploadface = findViewById(R.id.b_upload_face);
        b_sure = findViewById(R.id.b_sure);
        b_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.AddUser(username.getText().toString(),username.getText().toString());
                Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
