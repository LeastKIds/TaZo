package com.example.resultmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private long backKeyPressedTime = 0;
    private Toast toast;

    String str;

    private Boolean check=true;

    int myId;

    private Button button,profile_btn,makeroom;
    private ImageButton cahage_Button,menubutton,myLocation;
    private EditText text;

    private  static int permission_code=100;
    private  static String[] permissions={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

    };
    private FusedLocationSource mLoction;
    private NaverMap mNaverMap;
    Marker marker;

    private DrawerLayout drawerLayout;
    private  View menubar;

    TextView spinner;
    ArrayAdapter<String> arrayAdapter;

    LinearLayout mLlinearLayout;
    Message msg;
    OkHttpClient client=new OkHttpClient();

    private Context context;
    private Button logout;



    private Button roomSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Thread checkLogin = new CheckLogin();
        checkLogin.start();

        new myGetThead().start();

        roomSelect = (Button) findViewById(R.id.roomSelect);
        roomSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, chattingRoom.class);
                startActivity(intent);
            }
        });



        Button speed=findViewById(R.id.speed);
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client=new OkHttpClient();
//        RequestBody Body=new FormBody.Builder().add("nickname",str).build();
                Request request=new Request.Builder().url("https://tazoapp.site/rooms")
                        .get().build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("????????????");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("????????????");
                        try {
                            JSONArray json=new JSONArray(response.body().string());
                            msg=handler2.obtainMessage();
                            msg.what=1;//??????
                            msg.obj= json;
                            handler2.sendMessage(msg);
                            System.out.println(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        mLoction=new FusedLocationSource(this,permission_code);


        button=findViewById(R.id.button);
        text=findViewById(R.id.text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=text.getText().toString();
                System.out.println(str);
                new searchlocation(str).start();
            }

        });
        myLocation=findViewById(R.id.myLocation);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng2 = new LatLng(mLoction.getLastLocation().getLatitude(),mLoction.getLastLocation().getLongitude());
                CameraUpdate cameraUpdate2=CameraUpdate.scrollTo(latLng2);
                cameraUpdate2.zoomTo(15.0);
                mNaverMap.moveCamera(cameraUpdate2);
            }
        });

        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        menubar=(View) findViewById(R.id.menu);
        drawerLayout.setDrawerListener(listener);
        Button close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        String[] array={" ????????? ??????","????????? ??????"};
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                array);

        spinner=findViewById(R.id.spinner);

        menubutton=findViewById(R.id.menubutton);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(menubar);
            }
        });


        mLlinearLayout = findViewById(R.id.mLlinearLayout);

        cahage_Button=findViewById(R.id.cahage_Button);
        cahage_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                marker.setMap(null);
                str=null;
                if(check){
                    mLlinearLayout.removeAllViews();
                    mLlinearLayout.addView(spinner);
                    mLlinearLayout.addView(text);
                    text.setText(null);
                    text.setHint("      ?????????");
                    Toast.makeText(getApplicationContext(),"???????????? ????????? ?????????",
                            Toast.LENGTH_SHORT).show();
                    check=false;
                }
                else{
                    mLlinearLayout.removeAllViews();
                    mLlinearLayout.addView(text);
                    mLlinearLayout.addView(spinner);
                    text.setText(null);
                    text.setHint("      ?????????");
                    Toast.makeText(getApplicationContext(),"???????????? ????????? ?????????",
                            Toast.LENGTH_SHORT).show();
                    check=true;
                }
            }
        });
        profile_btn=findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        makeroom=findViewById(R.id.makeroom);
        makeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(text.getText().length()+"ssssssssssss");
                if(str==null){
                    Toast.makeText(getApplicationContext(),"?????????????????? ??????????????? ????????????",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(MainActivity.this,RoomMakeActivity.class);
                    if (check){
                        intent.putExtra("start",str);
                        intent.putExtra("result","?????????");
                    }
                    else{
                        intent.putExtra("result",str);
                        intent.putExtra("start","?????????");
                    }
                    startActivity(intent);
                }
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread logout = new Logout();
                logout.start();
            }
        });
    }

    DrawerLayout.DrawerListener listener =new DrawerLayout.DrawerListener(){

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };



    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        ActivityCompat.requestPermissions(this,permissions,permission_code);

        mNaverMap=naverMap;
        mNaverMap.setLocationSource(mLoction);

//        mNaverMap.setOnMapClickListener((point, coord) ->{
//                    marker.setPosition(new LatLng(coord.latitude,coord.longitude));
//                    marker.setMap(mNaverMap);
//                }
//                Toast.makeText(this, coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show()


//        );


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        Toast.makeText(this,"request?????? ?????????",Toast.LENGTH_LONG).show();
        if(requestCode==permission_code){
            if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }

    }
    class searchlocation extends Thread{
        String str;
        int index=0;
        public searchlocation(String str){
            this.str=str;
        }
        @Override
        public void run() {
            super.run();
            List<Address> addresses ;
            String temp=str;

            try {
                Geocoder geocoder=new Geocoder(getBaseContext());
                System.out.println(str);
                addresses = geocoder.getFromLocationName(str, 10);
                Address address = addresses.get(0);
                System.out.println(address);
                System.out.println(address.getAdminArea());
                if (!address.getAdminArea().equals("???????????????")&&!address.getLocality().equals(" ???????????????")){
                    throw new Exception();
                }
                if (address != null && !address.equals(" ")) {
                    msg=handler.obtainMessage();
                    msg.what=1;//??????
                    msg.obj= address.getLatitude()+" "+address.getLongitude();
                    handler.sendMessage(msg);
                }
            } catch(Exception e) {
                if ((index==0))str="?????? ????????? "+str;
                if (index==1)str="?????? "+temp;
                if (index<2)run();
                index++;
            }
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try{
                String[] a=msg.obj.toString().split(" ");
                LatLng latLng = new LatLng(Double.parseDouble(a[0]), Double.parseDouble(a[1]));

//                System.out.println(Double.parseDouble(a[0])+" "+Double.parseDouble(a[1]));
                CameraUpdate cameraUpdate=CameraUpdate.scrollTo(latLng);
                cameraUpdate.zoomTo(15.0);

                mNaverMap.moveCamera(cameraUpdate);
                marker=new Marker();
                marker.setPosition(latLng);
                marker.setMap(mNaverMap);

            }catch (Exception e){
                e.getStackTrace();
                System.out.println(e.getMessage());
            }
        }
    };
    Handler handler2=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            JSONArray array= null;
            JSONObject object=null;
            JSONArray array2= null;
            try {
                array = new JSONArray(msg.obj.toString());
                object=(JSONObject)array.get(0);
                array2=object.getJSONArray("Members");
                for (int i=0;i<array2.length();i++){
                    JSONObject temp=(JSONObject) array2.get(i);
                    if(myId==temp.getInt("id"))
                        System.out.println(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    class myGetThead extends Thread{
        StringBuilder sb;
        StringBuilder sb2;
        public void run() {
            try {
                URL url = new URL("https://tazoapp.site/users/test");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                InputStream is = connection.getInputStream();
                sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result;
                while((result = br.readLine())!=null){
                    sb.append(result+"\n");
                }
                try {
                    JSONObject temp=new JSONObject(sb.toString());
                    myId=temp.getInt("id");
                    System.out.println("dddddd"+temp.getInt("id"));
                }catch (Exception e){

                }
                URL url2 = new URL("https://tazoapp.site/rooms");

                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                connection2.setRequestMethod("GET");
                connection2.setDoInput(true);

                InputStream is2 = connection2.getInputStream();
                sb2 = new StringBuilder();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2,"UTF-8"));
                String result2;
                while((result2 = br2.readLine())!=null){
                    sb2.append(result2+"\n");
                }
                System.out.println(sb2);
                try {
                    JSONArray array=new JSONArray(sb.toString());
                    JSONObject object=(JSONObject)array.get(0);
                    JSONArray array2=object.getJSONArray("Members");
                    for (int i=0;i<array2.length();i++){
                        JSONObject temp=(JSONObject) array2.get(i);
                        if(myId==temp.getInt("id"))
                            System.out.println(temp);
                    }

                }catch (Exception e){

                }


//                msg=myhand.obtainMessage();
//                msg.what=1;//??????
//                msg.obj= sb.toString();
//                myhand.sendMessage(msg);
//                System.out.println(sb);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // ?????? ???????????? ????????? ????????? ???????????? ???????????? ?????? ??????
        // super.onBackPressed();

        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ?????? ??????????????? ?????? ???
        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ???????????? Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ?????? ??????????????? ?????? ???
        // ??????????????? ???????????? ????????? ????????? ????????? 2?????? ????????? ???????????? ??????
        // ?????? ????????? Toast ??????
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();
        }
    }

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
                Log.d("??????",setCookie);

                Request request = new Request.Builder()
                        .addHeader("cookie", setCookie)
                        .url(url)
                        .build();
                Response response = client.newCall(request)
                        .execute();

                String result = response.body().string();
                System.out.println("result : " + result);
                if(result.equals("null")){
                    Intent intent = new Intent(context,Login.class);
                    startActivity(intent);
                    finish();
                }

            } catch(Exception e) {
                e.printStackTrace();
            }




        }

    }
    public class Logout extends Thread
    {
        @Override
        public void run()
        {

            String url = "https://tazoapp.site/auth/logout";
            String shard="file";
            try {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sharedPreferences = getSharedPreferences(shard,0);
                String setCookie = sharedPreferences.getString("cookie","");
                Log.d("??????",setCookie);

                Request request = new Request.Builder()
                        .addHeader("cookie", setCookie)
                        .url(url)
                        .build();
                Response response = client.newCall(request)
                        .execute();

                String result = response.body().string();

                System.out.println(result);

                Intent intent = new Intent(context, Login.class);
                startActivity(intent);

            } catch(Exception e) {
                e.printStackTrace();
            }




        }

    }

}