package com.lele.date.activity.before;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lele.date.R;
import com.lele.date.activity.home.HomeActivity;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Button b_register,b_get_back_pwd,b_login;
    EditText edit_user,edit_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new Server();
        new Client();
        b_register = findViewById(R.id.register);
        b_get_back_pwd = findViewById(R.id.getbackpwd);
        edit_user = findViewById(R.id.edit_user);
        edit_pwd = findViewById(R.id.edit_pwd);
        b_login = findViewById(R.id.login);
        //默认用户名和密码，仅供测试用
        edit_pwd.setText("hello");
        edit_user.setText("hello");
        b_register.setOnClickListener(new View.OnClickListener() {
            /*
             * 进入注册新用户页面，暂时还没写
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AddUserActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        b_get_back_pwd.setOnClickListener(new View.OnClickListener() {
            /*
             * 进入找回密码页面，暂时还没写
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, GetBackPwdActivity.class));
                overridePendingTransition(0, 0);
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            /*
             * 登录，若密码验证成功则登录成功，否则则登录失败
             */
            @Override
            public void onClick(View view) {

                switch (Check(edit_user.getText().toString(),edit_pwd.getText().toString()))
                {
                    case 0:break;
                    case 1:startActivity(new Intent(getApplicationContext(), HomeActivity.class));overridePendingTransition(0, 0);break;
                    case 2:startActivity(new Intent(getApplicationContext(), HomeDefaultActivity.class));overridePendingTransition(0, 0);break;
                    case -1:break;
                    default:break;
                }
            }
        });
    }
    /**
     * 核对用户名和密码
     * @param username 用户名
     * @param pwd 密码
     * @return 1表示存在User和Userinfo，2表示仅存在User，-1表示不存在
     */
    public int Check(String username,String pwd)
    {
        User user = Server.LoginCheck(username,pwd);
        if(user!=null)
        {
            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
            Log.d("userinfosize",""+Server.getUserInfos().size());
            UserInfo userInfo = Server.getUserInfoByUserId(user.getId());
            if(userInfo!=null) {
                Client.setUserInfo(userInfo);
                Client.setUser(user);
                return 1;
            }else{
                Client.setUser(user);
                return 2;
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"请核对您的用户名和密码",Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public void sendRequestWithHttpURLConnection(final String request,final String url) {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    String s = url + request;
                    URL url = new URL(s);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
