package com.example.fiveyuanstore.myProfiles.myData;

import java.sql.Date;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Locale;

import com.example.fiveyuanstore.R;

import android.os.Bundle;  
import android.app.Activity;  
import android.app.DatePickerDialog;  
import android.app.TimePickerDialog;  
import android.util.Log;  
import android.view.Menu;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.DatePicker;  
import android.widget.TextView;  
import android.widget.TimePicker;  
  
public class TimeActivity extends Activity {  
  
    private final static String TAG="TimeDate";  
       //获取日期格式器对象  
    DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");  
        
      
    //定义一个TextView控件对象  
    TextView txtDate = null;    
    //获取一个日历对象  
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);  
      
      
    //当点击DatePickerDialog控件的设置按钮时，调用该方法  
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()  
    {  
        @Override  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            //修改日历控件的年，月，日  
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致  
            dateAndTime.set(Calendar.YEAR, year);  
            dateAndTime.set(Calendar.MONTH, monthOfYear);  
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);      
            //将页面TextView的显示更新为最新时间  
            upDateDate();     
              
        }          
    };     
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
          
        Log.d(TAG,"onCreate");  
       // txtDate =(TextView)findViewById(R.id.txtDate);  
        txtDate.setClickable(true);    
        txtDate.setFocusable(true);  
         
          
        txtDate.setOnClickListener(new OnClickListener(){    
                @Override    
                public void onClick(View v){    
                    Log.d(TAG,"txtDate click start");    
                    DatePickerDialog  dateDlg = new DatePickerDialog(TimeActivity.this,  
                            d,  
                            dateAndTime.get(Calendar.YEAR),  
                            dateAndTime.get(Calendar.MONTH),  
                            dateAndTime.get(Calendar.DAY_OF_MONTH));  
                   
                    dateDlg.show();  
                      
                    Log.d(TAG,"Date show");  
             }  
           });          
          
        upDateDate();  
 
    }  
      
    private void upDateDate() {  
        txtDate.setText(fmtDate.format(dateAndTime.getTime()));  
        }  
  
//    @Override  
//    public boolean onCreateOptionsMenu(Menu menu) {  
//        // Inflate the menu; this adds items to the action bar if it is present.  
//        getMenuInflater().inflate(R.menu.main, menu);  
//        return true;  
//    }  
  
}  