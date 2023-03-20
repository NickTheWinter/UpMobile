package com.example.upmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Получаем объект
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Берём данные о пользователе
        String password = sharedPreferences.getString("password", "");
        String email = sharedPreferences.getString("email", "");

        getUser(email,password);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void getUser(String email,String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject postData = new JSONObject();
                    postData.put("email", email);
                    postData.put("password", password);

                    // создаем соединение
                    URL url = new URL("http://mskko2021.mad.hakta.pro/api/user/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    // отправляем данные на сервер
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();

                    // получаем ответ от сервера
                    StringBuilder sb = new StringBuilder();
                    int HttpResult = conn.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        JSONObject response = new JSONObject(sb.toString());

                        // Получаем объект SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                        // Получаем объект Editor для редактирования SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // Сохраняем данные о пользователе
                        editor.putString("password", String.valueOf(password));
                        editor.putString("email", String.valueOf(email));
                        // Применяем изменения
                        editor.apply();

                        UserInfo.Id = response.getString("id");
                        UserInfo.avatar = response.getString("avatar");
                        UserInfo.nickName = response.getString("nickName");
                        UserInfo.email = response.getString("email");
                        UserInfo.token = response.getString("token");
                        Log.d("response",response.toString());

                        Intent main = new Intent(MainActivity.this,MainMenuActivity.class);
                        startActivity(main);
                    } else {
                        Toast.makeText(MainActivity.this, "invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception error){
                    Log.d("Connection error: ",error.toString());
                }
            }
        }).start();
    }

    public void LogIn(View v){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void Reg(View v){
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

}