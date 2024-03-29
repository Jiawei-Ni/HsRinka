package irdc.ex06_10; /* import相关class */

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EX06_10 extends Activity
{ /* 宣告对象变量 */
  TextView setTime1;
  TextView setTime2;
  Button mButton1;
  Button mButton2;
  Button mButton3;
  Button mButton4;
  Calendar c = Calendar.getInstance();
  private static int timerId=0;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); /* 载入main.xml Layout */
    setContentView(R.layout.main); /* 以下为只响一次的闹钟的设定 */
    setTime1 = (TextView) findViewById(R.id.setTime1); /* 只响一次的闹钟的设定Button */
    mButton1 = (Button) findViewById(R.id.mButton1);
    mButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      { /* 取得按下按钮时的时间做为TimePickerDialog的默认值 */
        c.setTimeInMillis(System.currentTimeMillis());
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE); /* 跳出TimePickerDialog来设定时间 */
        new TimePickerDialog(EX06_10.this,
            new TimePickerDialog.OnTimeSetListener()
            {
              public void onTimeSet(TimePicker view, int hourOfDay,
                  int minute)
              { /* 取得设定后的时间，秒跟毫秒设为0 */
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0); /* 指定闹钟设定时间到时要执行CallAlarm.class */
                Intent intent = new Intent(EX06_10.this,
                    CallAlarm.class); /* 建立PendingIntent */
                PendingIntent sender = PendingIntent.getBroadcast(
                    EX06_10.this, timerId++, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT); /*
                                                         * AlarmManager.RTC_WAKEUP设定服务在系统休眠时同样会执行
                                                         * *以set()
                                                         * 设定的PendingIntent只会执行一次
                                                         * *
                                                         */
                AlarmManager am;
                am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                    sender); /* 更新显示的设定闹钟时间 */
                String tmpS = format(hourOfDay) + "：" + format(minute);
                setTime1.setText(tmpS); /* 以Toast提示设定已完成 */
                Toast.makeText(EX06_10.this, "设定闹钟时间为" + tmpS,
                    Toast.LENGTH_SHORT).show();
              }
            }, mHour, mMinute, true).show();
      }
    }); /* 只响一次的闹钟的移除Button */
    mButton2 = (Button) findViewById(R.id.mButton2);
    mButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent intent = new Intent(EX06_10.this, CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(EX06_10.this,
            0, intent, 0); /* 由AlarmManager中移除 */
        AlarmManager am;
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender); /* 以Toast提示已删除设定，并更新显示的闹钟时间 */
        Toast.makeText(EX06_10.this, "闹钟时间解除", Toast.LENGTH_SHORT)
            .show();
        setTime1.setText("目前无设定");
      }
    }); /* 以下为重复响起的闹钟的设定 */
    setTime2 = (TextView) findViewById(R.id.setTime2); /* create重复响起的闹钟的设定画面 *//*
                                                                                * 引用timeset.
                                                                                * xml为Layout
                                                                                */
    LayoutInflater factory = LayoutInflater.from(this);
    final View setView = factory.inflate(R.layout.timeset, null);
    final TimePicker tPicker = (TimePicker) setView
        .findViewById(R.id.tPicker);
    tPicker.setIs24HourView(true); /* create重复响起闹钟的设定Dialog */
    final AlertDialog di = new AlertDialog.Builder(EX06_10.this)
        .setIcon(R.drawable.clock).setTitle("设定").setView(setView)
        .setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          { /* 取得设定的间隔秒数 */
            EditText ed = (EditText) setView.findViewById(R.id.mEdit);
            int times = Integer.parseInt(ed.getText().toString()) * 1000; /*
                                                                           * 取得设定的开始时间，
                                                                           * 秒及毫秒设为0
                                                                           */
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, tPicker.getCurrentHour());
            c.set(Calendar.MINUTE, tPicker.getCurrentMinute());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0); /* 指定闹钟设定时间到时要执行CallAlarm.class */
            Intent intent = new Intent(EX06_10.this, CallAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(
                EX06_10.this, 1, intent, 0); /* setRepeating()可让闹钟重复执行 */
            AlarmManager am;
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, c
                .getTimeInMillis(), times, sender); /* 更新显示的设定闹钟时间 */
            String tmpS = format(tPicker.getCurrentHour()) + "："
                + format(tPicker.getCurrentMinute());
            setTime2.setText("设定闹钟时间为" + tmpS + "开始，重复间隔为" + times
                / 1000 + "秒"); /* 以Toast提示设定已完成 */
            Toast.makeText(EX06_10.this,
                "设定闹钟时间为" + tmpS + "开始，重复间隔为" + times / 1000 + "秒",
                Toast.LENGTH_SHORT).show();
          }
        }).setNegativeButton("取消",
            new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {
              }
            }).create(); /* 重复响起的闹钟的设定Button */
    mButton3 = (Button) findViewById(R.id.mButton3);
    mButton3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      { /* 取得按下按钮时的时间做为tPicker的默认值 */
        c.setTimeInMillis(System.currentTimeMillis());
        tPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        tPicker.setCurrentMinute(c.get(Calendar.MINUTE)); /* 跳出设定画面di */
        di.show();
      }
    }); /* 重复响起的闹钟的移除Button */
    mButton4 = (Button) findViewById(R.id.mButton4);
    mButton4.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent intent = new Intent(EX06_10.this, CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(EX06_10.this,
            1, intent, 0); /* 由AlarmManager中移除 */
        AlarmManager am;
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender); /* 以Toast提示已删除设定，并更新显示的闹钟时间 */
        Toast.makeText(EX06_10.this, "闹钟时间解除", Toast.LENGTH_SHORT)
            .show();
        setTime2.setText("目前无设定");
      }
    });
  } /* 日期时间显示两位数的method */

  private String format(int x)
  {
    String s = "" + x;
    if (s.length() == 1)
      s = "0" + s;
    return s;
  }
}