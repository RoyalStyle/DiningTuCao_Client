package com.example.xidian_dining;
/*
 * 个人信息活动，可以查看并修改个人信息
 */
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Information extends Activity{
	private String number = new String("");
	private String passwd = new String("");
	private String passwd2 = new String("");
	private String name = new String("");
	private String email = new String("");
	private String grade = new String("大一");
	private String sex = new String("男");
	private String phone = new String("");
	private String birth = new String("竹园");
	private final static int DATE_DIALOG = 0;
	private Button dateButton;
	private static final String[] gradeitem={"--","大一","大二","大三","大四","研一","研二","研三"};
	private ArrayAdapter<String> adapter = null;
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 * Android手机返回键事件 --- 应用程序退出
	 */
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
        							Connection.writer.write("logout\n");		//通信协议[client发server收]
        							Connection.writer.flush();
        				//			Connection.br.close();
        				//			Connection.writer.close();
        							Connection.client.close();
        							System.exit(0);
        						} catch (IOException e) {
        							System.out.println("Exit Error");
        							e.printStackTrace();
        						}
        	        		}
        	        	}.start();
        		   }
        		  }).setNegativeButton("取消",null).show();
        }
        return true;
    }
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        final TextView numberText = (TextView)findViewById(R.id.information_textView11);
        final EditText passwdText = (EditText)findViewById(R.id.information_editText2);
        final EditText passwdText2 = (EditText)findViewById(R.id.information_editText3);
        final EditText nameText = (EditText)findViewById(R.id.information_editText4);
        final RadioGroup group = (RadioGroup)this.findViewById(R.id.information_radioGroup1);
        final EditText emailText = (EditText)findViewById(R.id.information_editText6);
        final Spinner gradeSpinner = (Spinner)findViewById(R.id.information_spinner1);
        final EditText phoneText = (EditText)findViewById(R.id.information_editText8);
        final RadioButton rdb1 = (RadioButton)findViewById(R.id.information_radioMale);
        final RadioButton rdb2 = (RadioButton)findViewById(R.id.information_radioFemale);
        final RadioButton rdb3 = (RadioButton)findViewById(R.id.information_radioNone);
        dateButton = (Button)findViewById(R.id.information_date_button);
        final Button saveButton = (Button)findViewById(R.id.information_button1);
        final Button exitButton = (Button)findViewById(R.id.information_button2);
        final TextView result = (TextView)findViewById(R.id.information_textView10);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gradeitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);
        gradeSpinner.setVisibility(View.VISIBLE);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		if(checkedId == R.id.information_radioMale){
        			sex = "男";
        		}else if(checkedId == R.id.information_radioFemale){
        			sex = "女";
        		}else{
        			sex = "";
        		}
        	}
        });
        dateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		shift();
        	}
        });
        //接收自服务器
        saveButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		number = numberText.getText().toString().trim();
        		passwd = passwdText.getText().toString().trim();
        		passwd2 = passwdText2.getText().toString().trim();
        		name = nameText.getText().toString().trim();
        		email = emailText.getText().toString().trim();
        		grade = (gradeSpinner.getSelectedItem()).toString().trim();
        		phone = phoneText.getText().toString().trim();
        		birth = dateButton.getText().toString().trim();
        		if(number.equals("") || passwd.equals("") || passwd2.equals("") || name.equals("")){
        			result.setText("输入不能为空");
        		}else if(!passwd.equals(passwd2)){
        			result.setText("两次密码不一致");
        		}else{
        			if(birth.equals("未设置")){
        				birth = "";
        			}
        			if(grade.equals("--")){
        				grade = "";
        			}
        			new Thread(){
        				public void run(){
        					Connection.writer.write("modify\n");
        					Connection.writer.write(number+"\n");
        					Connection.writer.write(passwd+"\n");
        					Connection.writer.write(name+"\n");
        					Connection.writer.write(sex+"\n");
        					Connection.writer.write(email+"\n");
        					Connection.writer.write(phone+"\n");
        					Connection.writer.write(grade+"\n");
        					Connection.writer.write(birth+"\n");
        					Connection.writer.flush();
        				}
        			}.start();
        		/*	try{
        				Thread.sleep(1000);
        			}catch(Exception e){
        				System.out.println("Informaiton error1 -> Modify failed");
        				e.printStackTrace();
        			}*/
        			result.setText("修改中...");
        			boolean find = true;
        			int time=0;
        			while(Connection.waiting2 == true){
        				try{
        					Thread.sleep(100);
        					time++;
        					if(time>20){
        						find = false;
        						Connection.link = false;
            		//			Connection.pan2 = false;						//心跳线程终止循环
            		//			Connection.heartBeaten.interrupt();				//关闭心跳线程
            					Connection.pan = false;
            					Connection.listenThread.interrupt();
            		//			Connection.br.close();
            		//			Connection.writer.close();
            					Connection.client.close();
                				result.setText("与服务器连接超时，可以尝试跳转其他版面或重新登录");
        						break;
        					}
        				}catch(Exception e){
        					System.out.println("Pane Message recv timeout error");
        					e.printStackTrace();
        				}
        			}
        			Connection.waiting2 = true;
        			if(find){
        				if(Connection.result.equals("OK")){
            				result.setText("修改成功");
            			}else{
            				result.setText("修改失败");
            			}
        			}else{
        				try {
        					new Thread(){
        						public void run(){
        							Connection.connect();
        						}
        					}.start();
        					try{
        						Thread.sleep(1500);			//重连需要时间
        					}catch(Exception e){
        						System.out.println("sleep failed");
        						e.printStackTrace();
        					}
        					if(Connection.link == false){
        						result.setText("重新连接失败，请检查网络");
        					}else{
        						new Thread(){
        							public void run(){
        								Connection.writer.write("login\n");
        								Connection.writer.write(Connection.user.getId()+"\n");
        								Connection.writer.write(Connection.user.getPasswd()+"\n");
        								Connection.writer.flush();
        							}
        						}.start();
        						result.setText("重新连接成功");
        					}
        				} catch (Exception e) {
        					// TODO Auto-generated catch block
        					System.out.println("Retry Connection Failed!");
        					e.printStackTrace();
        				}
        			}
        		}
        	}
        });
        numberText.setText(Connection.user.getId());				//这些个人信息在Connection.user中都存有，一登录就会收到来自服务器的个人信息
        passwdText.setText(Connection.user.getPasswd());
        passwdText2.setText(Connection.user.getPasswd());
        nameText.setText(Connection.user.getName());
        emailText.setText(Connection.user.getEmail());
        phoneText.setText(Connection.user.getPhone());
        String temp = Connection.user.getBirth().equals("")?"未设置":Connection.user.getBirth();
        dateButton.setText(temp);
        for(int i=0;i<gradeitem.length;i++){
        	if(gradeitem[i].equals(Connection.user.getGrade())){
        		gradeSpinner.setSelection(i,true);
        		break;
        	}
        }
        if(Connection.user.getSex().equals("")){
        	rdb3.setChecked(true);
        }else if(Connection.user.getSex().equals("男")){
        	rdb1.setChecked(true);
        }else{
        	rdb2.setChecked(true);
        }
    } 
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 * 点击日期按键后会显示日期选择的Dialog，供选择生日实用
	 */
	protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        Calendar c = Calendar.getInstance();
            dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	String mmm = month+1+"";
                    	String dd = dayOfMonth+"";
                    	if(month+1<10){
                    		int mm = month+1;
                    		mmm = "0"+mm;
                    	}
                    	if(dayOfMonth<10){
                    		dd = "0"+dayOfMonth; 
                    	}
                        dateButton.setText(""+year+mmm+dd);
                    }
                }, 
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
            );
        return dialog;
    }
	//活动[activity]转移，至Pane activity[主界面]，见Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Information.this, Pane.class);
        /* 关闭当前的Activity */
        Information.this.finish();
        /* 启动一个新的Activity */
        Information.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
