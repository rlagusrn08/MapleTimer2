package com.ptu.mapletimer2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView etcTimer;
    TextView textView;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    InputMethodManager imm;
    EditText simText;
    EditText diceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        etcTimer = findViewById(R.id.etcTimer);
        listItem = new ArrayList<String>();
        textView = new TextView(this);
        textView.setTextSize(24);
        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추가버튼
                showDialog(view);
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listItem);
        etcTimer.setAdapter(adapter);

        etcTimer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(),listItem.get(i).toString() + " 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                listItem.remove(i);
                adapter.notifyDataSetChanged();
            }
        });




        final RadioButton fifteen = (RadioButton)findViewById(R.id.fifteenMin);
        final RadioButton thirty = (RadioButton)findViewById(R.id.thirtyMin);
        final RadioButton hour = (RadioButton)findViewById(R.id.oneHour);
        final CheckBox extreme = (CheckBox)findViewById(R.id.extreme);
        final CheckBox drink = (CheckBox)findViewById(R.id.drink);
        final CheckBox mvp = (CheckBox)findViewById(R.id.mvp);
        final CheckBox sim = (CheckBox)findViewById(R.id.sim);
        final CheckBox dice = (CheckBox)findViewById(R.id.dice);
        simText = (EditText)findViewById(R.id.simText);
        diceText = (EditText)findViewById(R.id.diceText);

        Button startBtn = (Button)findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<TimerClass> timerList = new ArrayList<TimerClass>();
                timerList.clear();
                if(fifteen.isChecked()){
                    timerList.add(new TimerClass("경험치 쿠폰",R.drawable.coupon,900));
                }
                if(thirty.isChecked()){
                    timerList.add(new TimerClass("경험치 쿠폰",R.drawable.coupon,1800));
                }
                if(hour.isChecked()){
                    timerList.add(new TimerClass("경험치 쿠폰",R.drawable.coupon,3600));
                }
                if(extreme.isChecked()){
                    timerList.add(new TimerClass("익스트림 물약",R.drawable.extreme,1800));
                }
                if(drink.isChecked()){
                    timerList.add(new TimerClass("비약",R.drawable.drink,7200));
                }
                if(mvp.isChecked()){

                    timerList.add(new TimerClass("MVP 쿠폰", R.drawable.mvp,1800));
                }
                try {
                    if (sim.isChecked()) {
                        timerList.add(new TimerClass("홀리 심볼", R.drawable.sim, Integer.parseInt(simText.getText().toString())));
                    }
                    if (dice.isChecked()) {
                        timerList.add(new TimerClass("럭키 다이스", R.drawable.luckydice, Integer.parseInt(diceText.getText().toString())));
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"지속시간을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra("timerList",timerList);
                intent.putExtra("etcList",listItem);
                startActivity(intent);
            }
        });
    }

    int userTime;
    EditText input;

    public void showDialog(final View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("기타 타이머 설정");
        builder.setMessage("원하는 시간을 초단위로 입력해주세요.");

        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.post(new Runnable() {
            @Override
            public void run() {
                input.setFocusable(true);
                input.requestFocus();
                imm.showSoftInput(input,0);
            }
        });

        builder.setView(input);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    hideKeyboard();
                    userTime = Integer.parseInt(input.getText().toString());
                    listItem.add(Integer.toString(userTime));
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(etcTimer);
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"숫자를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(simText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(diceText.getWindowToken(), 0);
    }




}
