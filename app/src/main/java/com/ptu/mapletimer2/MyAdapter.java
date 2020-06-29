package com.ptu.mapletimer2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.ptu.mapletimer2.TimerActivity;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<MyItem> mItems = new ArrayList<>();
    private ArrayList<Boolean> running = new ArrayList<>();
    private ArrayList<String> myTime = new ArrayList<>();
    private ArrayList<CountDownTimer> countDownTimerArrayList = new ArrayList<>();
    private ArrayList<TextView> tv_name = new ArrayList<>();
    private ArrayList<TextView> tv_time = new ArrayList<>();
    MyItem myItem;
    CheckBox autoloop;
    CheckBox pushalarm;
    Context context;

    int size = 0;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }




    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_custom,viewGroup,false);
        }

        ImageView iv = (ImageView)view.findViewById(R.id.imageView);
        tv_name.add((TextView)view.findViewById(R.id.itemName));
        tv_time.add((TextView)view.findViewById(R.id.timeRemaining));
        tv_time.get(i).setTextColor(Color.BLACK);

        myItem = (MyItem) getItem(i);
        myTime.add(myItem.getTime());
        iv.setImageDrawable(myItem.getIcon());
        tv_name.get(i).setText(myItem.getName());

        Button startBtn = (Button)view.findViewById(R.id.startBtn);
        Button stopBtn = (Button)view.findViewById(R.id.stopBtn);


        running.add(true);


        autoloop = (CheckBox)((TimerActivity)TimerActivity.context).autoloop;
        pushalarm = (CheckBox)((TimerActivity)TimerActivity.context).pushalarm;

        int time = Integer.parseInt(myTime.get(i))*1000;

        CountDownTimer timer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long l) {
                tv_time.get(i).setText(
                        String.format("%02d:%02d:%02d",
                                (int) (l / (1000 * 60 * 60)) % 24,
                                (int) (l / (1000 * 60)) % 60,
                                (int) (l / 1000) % 60
                                )
                );
                if (l <= 10000) {
                    tv_time.get(i).setTextColor(Color.RED);
                }
            }
            @Override
            public void onFinish() {
                //종료
                tv_time.get(i).setText("버프 종료");
                running.set(i,false);

                if(autoloop.isChecked()){
                    ((TimerActivity)context).pushAlarm(tv_name.get(i).getText().toString()+"(이)가 종료되었습니다.", "자동 반복이 시작됩니다. 버프를 다시 사용하세요.");
                    tv_time.get(i).setTextColor(Color.BLACK);
                    if(!running.get(i)){
                        countDownTimerArrayList.get(i).start();
                        running.set(i,true);
                    }
                }
                else{
                    ((TimerActivity)context).pushAlarm(tv_name.get(i).getText().toString()+"(이)가 종료되었습니다.", "다시 사용 후 시작버튼을 눌러주세요.");
                }
            }
        };

        countDownTimerArrayList.add(timer);
        size = size+1;
        countDownTimerArrayList.get(i).start();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_time.get(i).setTextColor(Color.BLACK);
                if(!running.get(i)){
                    countDownTimerArrayList.get(i).start();
                    running.set(i,true);
                }

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(running.get(i)) {
                    countDownTimerArrayList.get(i).cancel();
                    tv_time.get(i).setTextColor(Color.BLACK);
                    tv_time.get(i).setText(
                            String.format("%02d:%02d:%02d",
                                    (int) (Integer.parseInt(myTime.get(i)) * 1000 / (1000 * 60 * 60)) % 24,
                                    (int) (Integer.parseInt(myTime.get(i)) * 1000 / (1000 * 60)) % 60,
                                    (int) (Integer.parseInt(myTime.get(i)) * 1000 / 1000) % 60
                            )
                    );
                    running.set(i,false);
                }
            }
        });



        return view;
    }

    public void addItem(Drawable img, String name, String time){
        MyItem mItem = new MyItem();

        mItem.setIcon(img);
        mItem.setName(name);
        mItem.setTime(time);

        mItems.add(mItem);
    }

    public void allStart(){
        for(int i = 0; i<size;i++){
            tv_time.get(i).setTextColor(Color.BLACK);
            if(!running.get(i)){
                countDownTimerArrayList.get(i).start();
                running.set(i,true);
            }
        }
    }

    public void allStop() {
        for (int i = 0; i < size; i++) {
            if(running.get(i)) {
                countDownTimerArrayList.get(i).cancel();
                tv_time.get(i).setTextColor(Color.BLACK);
                tv_time.get(i).setText(
                        String.format("%02d:%02d:%02d",
                                (int) (Integer.parseInt(myTime.get(i)) * 1000 / (1000 * 60 * 60)) % 24,
                                (int) (Integer.parseInt(myTime.get(i)) * 1000 / (1000 * 60)) % 60,
                                (int) (Integer.parseInt(myTime.get(i)) * 1000 / 1000) % 60
                        )
                );
                running.set(i,false);
            }
        }
    }
}
