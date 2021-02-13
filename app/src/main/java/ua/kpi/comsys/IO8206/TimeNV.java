package ua.kpi.comsys.IO8206;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class TimeNV extends AppCompatActivity {
    int HH, MM, SS;
    private TextView main_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        main_text = findViewById(R.id.main_text);
        main_text.setMovementMethod(new ScrollingMovementMethod());
    }

    public void changeText() {
        main_text.setText("It`s WORK!");
    }

    void defaultInit(){
        HH = 0;
        MM = 0;
        SS = 0;
    }

    void userInit(int hour, int minute, int sec){
        HH = hour;
        MM = minute;
        SS = sec;
    }

    void userInitTime(Date date){
        HH = date.getHours();
        MM = date.getMinutes();
        SS = date.getSeconds();
    }

    String returnTimeStr(){
        String result, XM;

        if(HH>=12) {XM = "AM"; HH -= 12;}
        else XM = "PM";

        result = HH + ":" + MM + ":" + SS + " " + XM;
        return result;
    }

    TimeNV sumTime(TimeNV first, TimeNV second){
        TimeNV result = new TimeNV();

        result.HH = first.HH + second.HH;
        result.MM = first.MM + second.MM;
        result.SS = first.SS + second.SS;

        if(result.SS>59) {result.MM += 1; result.SS -= 60;}
        if(result.MM>59) {result.HH += 1; result.MM -= 60;}
        if(result.HH>23) {result.HH -= 24;}

        return result;
    }

    TimeNV subTime(TimeNV first, TimeNV second){
        TimeNV result = new TimeNV();

        result.HH = first.HH - second.HH;
        result.MM = first.MM - second.MM;
        result.SS = first.SS - second.SS;

        if(result.HH<0) {result.HH += 24;}
        if(result.MM<0) {result.HH -= 1; result.MM += 60;}
        if(result.SS<0) {result.MM -= 1; result.SS += 60;}

        return result;
    }

    TimeNV subCurrentTime(TimeNV time) {
        TimeNV temp = new TimeNV();
        temp.HH = HH;
        temp.MM = MM;
        temp.SS = SS;

        return subTime(temp, time);
    }

    TimeNV sumCurrentTime(TimeNV time) {
        TimeNV temp = new TimeNV();
        temp.HH = HH;
        temp.MM = MM;
        temp.SS = SS;

        return sumTime(temp, time);
    }

    public void part1(View view){
        Intent intent = new Intent(TimeNV.this, MainActivity.class);
        startActivity(intent);
    }

    public void runPart2(View view){
        String result = "";
        TimeNV one = new TimeNV();
        TimeNV two = new TimeNV();
        TimeNV three = new TimeNV();

        TimeNV res1 = new TimeNV();
        TimeNV res2 = new TimeNV();

        one.defaultInit();
        two.userInit(10, 50, 34);
        three.userInitTime(new Date());

        res1 = res1.subTime(three, two);
        res2 = res2.sumTime(three, two);

        result += three.returnTimeStr() + " -+ " + two.returnTimeStr() + "\n";

        result += res1.returnTimeStr() + "\n";
        result += res2.returnTimeStr();

        main_text.setText(result);
    }

}
