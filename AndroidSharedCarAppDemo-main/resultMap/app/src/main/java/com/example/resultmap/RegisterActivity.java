package com.example.resultmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.network.ErrorResult;
//import com.kakao.usermgmt.UserManagement;
//import com.kakao.usermgmt.callback.MeV2ResponseCallback;
//import com.kakao.usermgmt.response.MeV2Response;
//import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class RegisterActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText et_name, et_nickname, et_email, et_passwd, et_passwd_confirm, et_gender;
    private Button btn_submit;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;
    private String geneder;


    //<=====================================카카오========================================================================================>

//    private ISessionCallback mSessionCallback;

    public void intent(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        mSessionCallback = new ISessionCallback() {
//            @Override
//            public void onSessionOpened() {
//                //로그인 요청
//                UserManagement.getInstance().me(new MeV2ResponseCallback() {
//                    @Override
//                    public void onFailure(ErrorResult errorResult) {
//                        // 로그인 실패
//                        Toast.makeText(RegisterActivity.this, "로그인 도중에 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSessionClosed(ErrorResult errorResult) {
//                        //세션이 닫힘..
//                        Toast.makeText(RegisterActivity.this, "세션이 닫혔습니다.. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(MeV2Response result) {
//
//                        //로그인 성공
//                        Intent intent = new Intent(RegisterActivity.this, KakaoProfile.class);
//                        intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
//                        intent.putExtra("profileImg", result.getKakaoAccount().getProfile().getProfileImageUrl());
//                        intent.putExtra("email", result.getKakaoAccount().getEmail());
//                        startActivity(intent);
//
//                        Toast.makeText(RegisterActivity.this, "환영 합니다~", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onSessionOpenFailed(KakaoException exception) {
//                Toast.makeText(RegisterActivity.this, "onSessionOpenFailed", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        Session.getCurrentSession().addCallback(mSessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();

    //<===========카카오============================================================================================================>

//        getAppKeyHash();

        RadioGroup rg_gender;
        RadioButton rb_male, rb_female;


        et_name = (EditText)findViewById(R.id.et_name);
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        et_email = (EditText)findViewById(R.id.et_email);
        et_passwd = (EditText)findViewById(R.id.et_passwd);
        et_passwd_confirm = (EditText)findViewById(R.id.et_passwd_confirm);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);

        btn_submit = (Button)findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener((v) -> {
            String data = "{" +
                    "\"nickname\"" + "\"" + et_nickname.getText().toString() + "\","+
                    "\"email\"" + "\"" + et_email.getText().toString() + "\","+
                    "\"password\"" + "\"" + et_passwd.getText().toString() + "\","+
                    "}";
            Submit(data);



//            //email과 password 유효성 검사 하는 코드
            if(et_email.getText().toString().length() == 0){
                Toast.makeText(RegisterActivity.this, "Email을 입력하세요", Toast.LENGTH_SHORT).show();
                et_email.requestFocus();
                return;
            }

            //이메일형식체크
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches())
            {
                Toast.makeText(RegisterActivity.this,"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
                return;
            }

            if(et_passwd.getText().toString().length() == 0){
                Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                et_passwd.requestFocus();
                return;
            }

            if(et_passwd.getText().toString().length() < 3){
                Toast.makeText(RegisterActivity.this, "비밀번호는 4자리 이상 이어야 합니다.", Toast.LENGTH_SHORT).show();
                et_passwd.requestFocus();
                return;
            }


            if(!et_passwd_confirm.getText().toString().equals(et_passwd.getText().toString())){
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                et_passwd_confirm.requestFocus();
                return;
            }

            if(et_name.getText().toString().length() == 0){
                Toast.makeText(RegisterActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                et_name.requestFocus();
                return;
            }

            if(et_nickname.getText().toString().length() == 0){
                Toast.makeText(RegisterActivity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                et_nickname.requestFocus();
                return;
            }


            Intent intent = new Intent(this, Login.class);
            startActivity(intent);


        });




    }

     //카카오 로그인 시 필요한 해시키를 얻는 메소드 이다.
    private void getAppKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        }catch (Exception e){
            Log.e("name not found", e.toString());
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Session.getCurrentSession().removeCallback(mSessionCallback);
//    }

    private void Submit(String data)
    {
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", et_nickname.getText().toString());
            jsonBody.put("email", et_email.getText().toString());
            jsonBody.put("password", et_passwd.getText().toString());
            String gender = "";
//            if(rb_male.isSelected()) {
//                gender = "male";
//                System.out.println(gender);
//            }
//            if(rb_female.isSelected()){
//                gender = "female";
//                System.out.println(gender);
//            }
//            jsonBody.put("gender", gender);

            final String requestBody = jsonBody.toString();

            String URL = "https://tazoapp.site/auth/signup";

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);

                    intent();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "g.yju형식이 아닙니다", Toast.LENGTH_SHORT).show();



                    et_email.requestFocus();
                    System.out.println("최지성");
//                    Log.wtf("VOLLEY", error.toString());
//                    Log.e("Volley", error.toString());
                }
            })
            {
                @Override
                public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    }catch (UnsupportedEncodingException uee){
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };


            requestQueue.add(stringRequest);


        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}