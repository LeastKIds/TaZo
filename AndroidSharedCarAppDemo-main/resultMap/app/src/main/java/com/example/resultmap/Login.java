package com.example.resultmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity {

    private EditText et_email, et_passwd;
    private Button btn_register, login_button;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_passwd = findViewById(R.id.et_passwd);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_passwd.getText().toString();
                Response.ErrorListener err = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                };
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //여기서 로그인 성공 코드 작성
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(jsonObject);
                            //dfdfd
//                            boolean success = jsonObject.getBoolean("success");

//                            if(true) {  //로그인 성공시
//                                String email = jsonObject.getString("email");
//                                String password = jsonObject.getString("password");
//                                String password = "1234";
                                String nickname = jsonObject.getString("nickname");

                                Toast.makeText(getApplicationContext(), String.format("%s님 환영합니다.", nickname), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, com.example.resultmap.MainActivity.class);

                                intent.putExtra("email", email);
//                                intent.putExtra("password", password);
                                intent.putExtra("nickname", nickname);

                                startActivity(intent);


//                                startActivity(intent);
//                            }else{  //로그인 실패시
//                                Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(email, password, responseListener, err);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }
        });

    }
}