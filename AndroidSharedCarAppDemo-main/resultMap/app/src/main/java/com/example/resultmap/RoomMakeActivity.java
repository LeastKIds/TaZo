package com.example.resultmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RoomMakeActivity extends AppCompatActivity {
    Message msg;
    String nickname,gender,start,result;
    EditText roomName;
    String gerderRimit="none";
    String userLimit="2";
    Spinner spinner,spinner2;
    TextView orderName,startPlace,resultPlace,startTime;
    ImageButton makeStart;

    TimePicker timePicker;
    private Context  context;

    String cookie="connect.sid=s%3ArmRzL7Afm6Y41zHsuXqU-RLSZd7kPs0v.qoAgHKNN0tkGLUUrnFxt4ikvWOLzEfWaLpMGHefFbds; Domain=.tazoapp.site; Path=/; HttpOnly; Secure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_make);

        context = this;

        Thread checkLogin=new CheckLogin();
        checkLogin.start();


        orderName=findViewById(R.id.orderName);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody =new FormBody.Builder()
                .build();
        System.out.println(formBody.toString());
        Request request = new Request.Builder()
                .addHeader("cookie",cookie)
                .url("https://tazoapp.site/rooms/2")
                .delete(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.code());
                System.out.println(response.message());
                System.out.println(response.body().string());
            }
        });



        startPlace=findViewById(R.id.startPlace);
        resultPlace=findViewById(R.id.resultPlace);

        Intent intent=getIntent();
        start=(String)intent.getExtras().get("start");
        result=(String)intent.getExtras().get("result");

        startPlace.setText(start);
        resultPlace.setText(result);

        new myGetThead().start();

        Date date=new Date(System.currentTimeMillis());
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

        StringTokenizer st=new StringTokenizer(sdf.format(date),"-");

        startTime=findViewById(R.id.startTime);
        startTime.append(st.nextToken()+"월 "+st.nextToken()+"일 (오늘)");

        timePicker=findViewById(R.id.timePicker);

        roomName=findViewById(R.id.roomName);


        spinner2=findViewById(R.id.spinner2);
        String[] items2={"2명","3명","4명"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items2
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너에 어댑터 설정
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userLimit=String.valueOf(position+2);
                System.out.println("클릭");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("클릭");
            }
        });


        makeStart=findViewById(R.id.makeStart);
        makeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(start+" "+result);

                int hourchech=timePicker.getCurrentHour();
                int mintuecheck=timePicker.getCurrentMinute();

                Date date=new Date(System.currentTimeMillis());

                if (hourchech<date.getHours()){
                    Toast.makeText(getApplicationContext(),"현재시간 후로 설정 해주세요",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    if (hourchech==date.getHours()&&mintuecheck<date.getMinutes()){
                        Toast.makeText(getApplicationContext(),"현재시간 후로 설정 해주세요",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR,timePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        System.out.println(simpleDateFormat.format(calendar.getTime())+"포맷 완성");
                        String timeresult=simpleDateFormat.format(calendar.getTime());

                            OkHttpClient client = new OkHttpClient();
                            RequestBody formBody =new FormBody.Builder()
                                    .add("name", roomName.getText().toString())
                                    .add("userLimit",userLimit)
                                    .add("gender",gerderRimit)
                                    .add("originName",start)
                                    .add("destinationName",result)
                                    .add("startAt",timeresult)
                                    .build();
                            System.out.println(formBody.toString());
                            Request request = new Request.Builder()
                                    .addHeader("cookie",cookie)
                                    .url("https://tazoapp.site/rooms")
                                    .post(formBody)
                                    .build();


                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.getStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    System.out.println(response.code());
                                    System.out.println(response.message());
                                    System.out.println(response.body().string());
                                }
                            });


                    }
                }



            }

        });

    }
    class myGetThead extends Thread{
        StringBuilder sb;
        public void run() {
            try {
                URL url = new URL("https://tazoapp.site/users/test");
//                URL url = new URL(user);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); //전송방식
//                    connection.setDoOutput(true);       //데이터를 쓸 지 설정
                connection.setDoInput(true);        //데이터를 읽어올지 설정

                InputStream is = connection.getInputStream();
                sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result;
                while((result = br.readLine())!=null){
                    sb.append(result+"\n");
                }
                try {
                    JSONObject temp=new JSONObject(sb.toString());
                }
                catch (Exception e){

                }
                System.out.println(sb.toString());
                msg=myhand.obtainMessage();
                msg.what=1;//구분
                msg.obj= sb.toString();
                myhand.sendMessage(msg);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    Handler myhand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject json2=new JSONObject((msg.obj.toString()));

                nickname=json2.getString("nickname");
                orderName.setText(nickname);

                gender=json2.getString("gender");

                spinner=findViewById(R.id.spinner);
                String hangul="";
                if (gender.equals("male")){
                    hangul="남자만";
                }else{
                    hangul="여자만";
                }

                String[] items={"상관없음",hangul};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        RoomMakeActivity.this, android.R.layout.simple_spinner_item, items
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // 스피너에 어댑터 설정
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println(position);
                        if (position==1){
                            gerderRimit=gender;
                        }
                        else {
                            gerderRimit="none";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println("클릭");
                    }
                });


            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    };

    public class CheckLogin extends Thread
    {
        @Override
        public void run()
        {

            String url = "https://tazoapp.site/auth";
            String shard="file";
            try {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sharedPreferences = getSharedPreferences(shard,0);
                String setCookie = sharedPreferences.getString("cookie","");
                Log.d("세션",setCookie);

                Request request = new Request.Builder()
                        .addHeader("cookie", "123123"+setCookie)
                        .url(url)
                        .build();
                Response response = client.newCall(request)
                        .execute();

                String result = response.body().string();
                System.out.println("result : " + result);
                if(result.equals("null")){
                    System.out.println("adsfasdfasdfafdsfasdf");
                    Intent intent = new Intent(context,Login.class);
                    startActivity(intent);
                    finish();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }




        }

    }



}