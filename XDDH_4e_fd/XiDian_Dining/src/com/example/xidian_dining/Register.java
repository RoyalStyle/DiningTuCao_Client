package com.example.xidian_dining;
/*
 * ע������ʾǰ����ע����Ϣ�����û�д���������һ��ע�������ʣ�µ�ע����Ϣ
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
	private static final String[] gradeitem={"--","��һ","���","����","����","��һ","�ж�","����"};
	private String regNumber = "";
	private String regPasswd = "";
	private String regName = "";
	private String regSex = "��";
	private String regGrade = "";
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
        	new AlertDialog.Builder(this).setTitle("ȷ���˳���").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int which){
        			   new Thread(){
        	        		public void run(){		
        	        			try {
        	        	//			Connection.pan2 = false;						//�����߳���ֹѭ��
        	        	//			Connection.heartBeaten.interrupt();				//�ر������߳�
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
        		  }).setNegativeButton("ȡ��",null).show();
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
        			resText.setText("������Ϣδ����");
        		}else if(!(passwdText.getText().toString().trim().equals(passwdText2.getText().toString().trim()))){
        			resText.setText("�����������벻һ��");
        		}else{
        			resText.setText("OK");
        			shift2();
        		}
        	}
        });
        group.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		if(checkedId == R.id.register_radioMale){
        			regSex = "��";
        		}else if(checkedId == R.id.register_radioFemale){
        			regSex = "Ů";
        		}else{
        			regSex = "";
        		}
        	}
        });
	}
	//�[activity]ת�ƣ���MainActivity activity[��¼����]����MainActivity.java
	private void shift(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Register.this, MainActivity.class);
        /* �رյ�ǰ��Activity */
        Register.this.finish();
        /* ����һ���µ�Activity */
        Register.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
	//�[activity]ת�ƣ���Register2 activity[ע�����2]����Register2.java
	private void shift2(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Register.this, Register2.class);
        /* �رյ�ǰ��Activity */
        Register.this.finish();
        /* ����һ���µ�Activity */
        Register.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}