package com.ptu.mapletimer2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class TimerActivity extends Activity {
    public static Context context;
    private ListView mListView;
    MyAdapter mMyAdapter = new MyAdapter();
    ArrayList<TimerClass> timerList = null;
    ArrayList<String> etcList;
    public CheckBox autoloop;
    public CheckBox pushalarm;
    Button allStart;
    Button allStop;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_list);

        autoloop = (CheckBox)findViewById(R.id.autoloop);
        pushalarm = (CheckBox)findViewById(R.id.pushalarm);

        Intent intent = getIntent();
        etcList = (ArrayList<String>)intent.getSerializableExtra("etcList");
        timerList = (ArrayList<TimerClass>)intent.getSerializableExtra("timerList");
        mListView = (ListView)findViewById(R.id.listView);
        dataSetting();


        allStart = (Button)findViewById(R.id.allStartBtn);
        allStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyAdapter.allStart();
            }
        });

        allStop = (Button)findViewById(R.id.allStopBtn);
        allStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyAdapter.allStop();
            }
        });


        context = this;
    }

    public void dataSetting(){
        for(int i = 0; i<timerList.size();i++){
            TimerClass tc = timerList.get(i);
            mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), tc.icon),tc.name, Integer.toString(tc.time));
        }
        for(int i = 0; i<etcList.size();i++){
            mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank), String.format("기타 타이머 %d", i+1), etcList.get(i).toString());
        }
        mListView.setAdapter(mMyAdapter);
    }

    int id =1;
    public void pushAlarm(String title,String text){
        if(pushalarm.isChecked()) {
//            Intent intent = new Intent(TimerActivity.this.getApplicationContext(),TimerActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent pendnoti = PendingIntent.getActivity(TimerActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
//            builder.setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pendnoti);
            builder.setDefaults(Notification.DEFAULT_SOUND).setContentIntent(PendingIntent.getActivity(this,0,new Intent(),0)).setAutoCancel(true);
            builder.setSmallIcon(R.drawable.blank);
            builder.setContentTitle(title);
            builder.setContentText(text);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setAutoCancel(true);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            notificationManager.notify(id, builder.build());
            id++;
        }
    }

    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 타이머가 종료되고 타이머 설정 창으로 돌아갑니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            mMyAdapter.allStop();
            finish();
        }
    }

}
