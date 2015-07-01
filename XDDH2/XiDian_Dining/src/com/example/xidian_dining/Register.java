package com.example.xidian_dining;
/*
 * 注册活动，显示前几项注册信息，如果没有错误会进行下一个注册活动，完成剩下的注册信息
 */
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity{
	private static final String[] gradeitem={"--","大一","大二","大三","大四","研一","研二","研三"};
	private String regNumber = "";
	private String regPasswd = "";
	private String regName = "";
	private String regSex = "男";
	private String regGrade = "";
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
        	new AlertDialog.Builder(this).setTitle("确认退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int which){
        			   new Thread(){
        	        		public void run(){		
        	        			try {
        	        	//			Connection.pan2 = false;						//心跳线程终止循环
        	        	//			Connection.heartBeaten.interrupt();				//关闭心跳线程
        	        				Connection.pan = false;
        	        				Connection.listenThread.interrupt();
        							Connection.br.close();
        							Connection.writer.close();
        							Connection.client.close();
        						} catch (IOException e) {
        							System.out.println("Exit Error");
        							e.printStackTrace();
        						}
        	        		}
        	        	}.start();
        	        	System.exit(0);
        		   }
        		  }).setNegativeButton("取消",null).show();
        }
        return true;
    }
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final Button nextButton = (Button)findViewById(R.id.register_button1);
        final Button exitButton = (Button)findViewById(R.id.register_button2);
        final Spinner gradeSpinner = (Spinner)findViewById(R.id.register_spinner1);
        final EditText numberText = (EditText)findViewById(R.id.register_editText1);
        final EditText passwdText = (EditText)findViewById(R.id.register_editText2);
        final EditText passwdText2 = (EditText)findViewById(R.id.register_editText3);
        final EditText nameText = (EditText)findViewById(R.id.register_editText4);
        final RadioGroup group = (RadioGroup)this.findViewById(R.id.register_radioGroup1);
        final TextView resText = (TextView)findViewById(R.id.register_textView7);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gradeitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);
        gradeSpinner.setVisibility(View.VISIBLE);
        numberText.setText(Connection.number);
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Connection.number = "";
        		Connection.passwd = "";
        		Connection. name = "";
        		Connection.sex = "";
        		Connection.email = "";
        		Connection.grade = "";
        		Connection.phone = "";
        		Connection.birth = "";
        		Connection.registertime = "";
        		shift();
        	}
        });
        nextButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Connection.number = regNumber = numberText.getText().toString().trim();
        		Connection.passwd = regPasswd = passwdText.getText().toString().trim();
        		Connection.name = regName = nameText.getText().toString().trim();
        		Connection.sex = regSex;
        		Connection.grade = regGrade = (gradeSpinner.getSelectedItem()).toString().trim();
        		if(regGrade.equals("--")){
        			Connection.grade = regGrade = "";
        		}
        		if(regNumber.equals("") || regPasswd.equals("") || regName.equals("")){
        			resText.setText("必填信息未完善");
        		}else if(!(passwdText.getText().toString().trim().equals(passwdText2.getText().toString().trim()))){
        			resText.setText("两次输入密码不一致");
        		}else{
        			resText.setText("OK");
        			shift2();
        		}
        	}
        });
        group.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		if(checkedId == R.id.register_radioMale){
        			regSex = "男";
        		}else if(checkedId == R.id.register_radioFemale){
        			regSex = "女";
        		}else{
        			regSex = "";
        		}
        	}
        });
	}
	//活动[activity]转移，至MainActivity activity[登录界面]，见MainActivity.java
	private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Register.this, MainActivity.class);
        /* 关闭当前的Activity */
        Register.this.finish();
        /* 启动一个新的Activity */
        Register.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
	//活动[activity]转移，至Register2 activity[注册界面2]，见Register2.java
	private void shift2(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Register.this, Register2.class);
        /* 关闭当前的Activity */
        Register.this.finish();
        /* 启动一个新的Activity */
        Register.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}