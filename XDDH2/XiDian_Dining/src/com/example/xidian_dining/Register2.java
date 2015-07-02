package com.example.xidian_dining;
/*
 * 注册活动，退出后会进入登录界面，成功后会直接进入主界面
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

public class Register2 extends Activity{
	private String regEmail = "";
	private String regPhone = "";
	private String regBirth = "";
	private final static int DATE_DIALOG = 0;
	private Button dateButton;
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
        setContentView(R.layout.register2);
        final Button nextButton = (Button)findViewById(R.id.register_button3);
        final Button exitButton = (Button)findViewById(R.id.register_button4);
        final EditText emailText = (EditText)findViewById(R.id.register_editText9);
        final EditText phoneText = (EditText)findViewById(R.id.register_editText10);
        final TextView resText = (TextView)findViewById(R.id.register_textView13);
        dateButton = (Button)findViewById(R.id.register_date_button);
        dateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		resText.setText("跳转...");
        		shift();
        	}
        });
        nextButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Connection.email = emailText.getText().toString().trim();
        		Connection.phone = phoneText.getText().toString().trim();
        		Connection.birth = dateButton.getText().toString().trim();
        		if(regBirth.equals("Date")){
        			regBirth = "";
        		}
        		new Thread(){
        			public void run(){
        				Connection.writer.write("register\n");				//通信协议[client发server收]，发送注册信息给服务器，服务器进行数据库操作
        				Connection.writer.write(Connection.number+"\n");
        				Connection.writer.write(Connection.passwd+"\n");
        				Connection.writer.write(Connection.name+"\n");
        				Connection.writer.write(Connection.grade+"\n");
        				Connection.writer.write(Connection.sex+"\n");
        				Connection.writer.write(Connection.email+"\n");
        				Connection.writer.write(Connection.phone+"\n");
        				Connection.writer.write(Connection.birth+"\n");
        				Connection.writer.flush();
        			}
        		}.start();
        		resText.setText("正在注册...");
        	/*	try{
        			Thread.sleep(1000);
        		}catch(Exception e){
        			System.out.println("Register2 error2 -> register failed");
        			e.printStackTrace();
        		}*/
        		boolean find = true;
    			int time=0;
    			while(Connection.waiting3 == true){
    				try{
    					Thread.sleep(100);
    					time++;
    					if(time>20){
    						find = false;
    						Connection.link = false;
        	//				Connection.pan2 = false;						//心跳线程终止循环
        	//				Connection.heartBeaten.interrupt();				//关闭心跳线程
        					Connection.pan = false;
        					Connection.listenThread.interrupt();
        	//				Connection.br.close();
        	//				Connection.writer.close();
        					Connection.client.close();
            				resText.setText("与服务器连接超时，正在尝试重新连接");
    						break;
    					}
    				}catch(Exception e){
    					System.out.println("Pane Message recv timeout error");
    					e.printStackTrace();
    				}
    			}
    			Connection.waiting3 = true;
    			if(find){
    				if(Connection.result.equals("OK")){
            			resText.setText("注册成功");
            			shift2();
            		}else if(Connection.result.equals("alreadyused")){
            			resText.setText("账号已存在");
            		}else{
            			resText.setText("服务器操作失败，请重试");
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
    						resText.setText("重新连接失败，请检查网络");
    					}else{
    						new Thread(){
    							public void run(){
    								Connection.writer.write("login\n");
    								Connection.writer.write(Connection.user.getId()+"\n");
    								Connection.writer.write(Connection.user.getPasswd()+"\n");
    								Connection.writer.flush();
    							}
    						}.start();
    						resText.setText("重新连接成功");
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					System.out.println("Retry Connection Failed!");
    					e.printStackTrace();
    				}
    			}
        		Connection.number = "";
        		Connection.passwd = "";
        		Connection. name = "";
        		Connection.sex = "";
        		Connection.email = "";
        		Connection.grade = "";
        		Connection.phone = "";
        		Connection.birth = "";
        		Connection.registertime = "";
        	}
        });
	}
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
	//活动[activity]转移，至 MainActivity activity[登录界面]，见MainActivity.java
	private void shift(){
		Connection.number = "";
		Connection.passwd = "";
		Connection.name = "";
		Connection.sex = "";
		Connection.email = "";
		Connection.grade = "";
		Connection.phone = "";
		Connection.birth = "";
		Connection.registertime = "";
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Register2.this, MainActivity.class);
        /* 关闭当前的Activity */
        Register2.this.finish();
        /* 启动一个新的Activity */
        Register2.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
	//活动[activity]转移，至 Pane activity[主界面]，见Pane.java
	private void shift2(){
		try{
			Connection.writer.write("login\n");
			Connection.writer.write(Connection.number+"\n");
			Connection.writer.write(Connection.passwd+"\n");
			Connection.writer.flush();
		}catch(Exception e){
			System.out.println("Register2 Error -> shift2 error");
			e.printStackTrace();
		}
		Connection.number = "";
		Connection.passwd = "";
		Connection.name = "";
		Connection.sex = "";
		Connection.email = "";
		Connection.grade = "";
		Connection.phone = "";
		Connection.birth = "";
		Connection.registertime = "";
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Register2.this, Pane.class);
        /* 关闭当前的Activity */
        Register2.this.finish();
        /* 启动一个新的Activity */
        Register2.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
