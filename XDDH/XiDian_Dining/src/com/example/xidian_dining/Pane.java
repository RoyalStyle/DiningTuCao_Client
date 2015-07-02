package com.example.xidian_dining;
/*
 * 用户登陆成功后的第一个活动，主要显示活动，可以跳转到任意功能版面
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.xidian_dining.MainActivity.PlaceholderFragment;

public class Pane extends Activity{
	private String[] arr = {"Top1","Top2","Top3","Top4","Top5","Top6","Top7","Top8","Top9","Top10","Top11","Top12","Top13","Top14","Top15"};
	private String[] arr2 = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
	private String windowid = "";
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
	public void mytextlistener(View v){
		windowid = "";
		TextView tv = (TextView) v;
		tv.getText().toString();
		final ListView lv = (ListView)findViewById(R.id.pane_listView1);
		for (int i = 0; i < lv.getChildCount(); i++) {
		     LinearLayout layout = (LinearLayout)lv.getChildAt(i);
		     if(tv == (TextView)layout.findViewById(R.id.simple_item2_textView11) || 
		    		 tv == (TextView)layout.findViewById(R.id.simple_item2_textView1) ||
		    		 tv == (TextView)layout.findViewById(R.id.simple_item2_textView2)){
		    	 final TextView idText = (TextView)layout.findViewById(R.id.simple_item2_textView2);
		    	 windowid = idText.getText().toString();
		    	 break;
		     }
		}
		new Thread(){
			public void run(){
 				Connection.writer.write("windowinformation\n");			//通信协议[client发server收]，表示请求某个推荐窗口的[菜名-评论]信息
 				Connection.writer.write(windowid+"\n");
 				Connection.writer.flush();
 			}
		}.start();
		boolean find = true;
		int time=0;
		while(Connection.waiting14 == true){
			try{
				Thread.sleep(100);
				time++;
				if(time>20){			//数据超时还未到来就尝试重新连接
					find = false;
					Connection.link = false;
		//			Connection.pan2 = false;						//心跳线程终止循环
		//			Connection.heartBeaten.interrupt();				//关闭心跳线程
					Connection.pan = false;
					Connection.listenThread.interrupt();
		//			Connection.br.close();
		//			Connection.writer.close();
					Connection.client.close();
					new AlertDialog.Builder(this).setTitle("与服务器连接超时，正在尝试重新连接").setNegativeButton("确定", null).show();
					break;
				}
			}catch(Exception e){
				System.out.println("Pane Message recv timeout error");
				e.printStackTrace();
			}
		}
		Connection.waiting14 = true;
		if(find){
			String[] dishitem = new String[Connection.dish2.size()];
			for(int i=0;i<Connection.dish2.size();i++){
				String sc = Connection.score2.get(i);
				if(Connection.score2.get(i).equals("0.0")){
					sc = "无";
				}else{
					sc = (String)(sc.subSequence(0, 3));
				}
				dishitem[i] = Connection.dish2.get(i)+"		评分:"+sc;
			}
			new AlertDialog.Builder(this).setTitle(tv.getText().toString()).setItems(
					dishitem, null).setNegativeButton("确定", null).show();
		}else{					//尝试重连
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
					new AlertDialog.Builder(this).setTitle("重新连接失败，请检查网络").setNegativeButton("确定", null).show();
				}else{
					new Thread(){
						public void run(){
							Connection.writer.write("login\n");							//重连后发送个人信息
							Connection.writer.write(Connection.user.getId()+"\n");
							Connection.writer.write(Connection.user.getPasswd()+"\n");
							Connection.writer.flush();
						}
					}.start();
					new AlertDialog.Builder(this).setTitle("重新连接成功").setNegativeButton("确定", null).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Retry Connection Failed!");
				e.printStackTrace();
			}
		}
	}
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pane);
        new Thread(){
        	public void run(){
        		Connection.writer.write("tuijian\n");				//通信协议[client发server收]，请求推荐信息(推荐的窗口-评分)
        		Connection.writer.flush();
        	}
        }.start();
        final Button b1 = (Button)findViewById(R.id.pane_button1);
        b1.setOnClickListener(new OnClickListener(){
        		//活动[activity]转移，至Search activity[搜索界面]，见Search.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* 指定intent要启动的类 */
                    intent.setClass(Pane.this, Search.class);
                    /* 关闭当前的Activity */
                    Pane.this.finish();
                    /* 启动一个新的Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b2 = (Button)findViewById(R.id.pane_button2);
        b2.setOnClickListener(new OnClickListener(){
        	//活动[activity]转移，至Remark activity[评论界面]，见Remark.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* 指定intent要启动的类 */
                    intent.setClass(Pane.this, Remark.class);
                    /* 关闭当前的Activity */
                    Pane.this.finish();
                    /* 启动一个新的Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b3 = (Button)findViewById(R.id.pane_button3);
        b3.setOnClickListener(new OnClickListener(){
        	//活动[activity]转移，至Speak activity[吐槽界面]，见Speak.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* 指定intent要启动的类 */
                    intent.setClass(Pane.this, Speak.class);
                    /* 关闭当前的Activity */
                    Pane.this.finish();
                    /* 启动一个新的Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b4 = (Button)findViewById(R.id.pane_button4);
        b4.setOnClickListener(new OnClickListener(){
        	//活动[activity]转移，至Information activity[个人信息界面]，见Information.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* 指定intent要启动的类 */
                    intent.setClass(Pane.this, Information.class);
                    /* 关闭当前的Activity */
                    Pane.this.finish();
                    /* 启动一个新的Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        boolean find = true;
		int time=0;
		while(Connection.waiting15 == true){
			try{
				Thread.sleep(100);
				time++;
				if(time>20){
					find = false;
					Connection.link = false;
			//		Connection.pan2 = false;						//心跳线程终止循环
			//		Connection.heartBeaten.interrupt();				//关闭心跳线程
					Connection.pan = false;
					Connection.listenThread.interrupt();
			//		Connection.br.close();
			//		Connection.writer.close();
					Connection.client.close();
					new AlertDialog.Builder(this).setTitle("与服务器连接超时，正在尝试重新连接").setNegativeButton("确定", null).show();
					break;
				}
			}catch(Exception e){
				System.out.println("Pane Message recv timeout error2");
				e.printStackTrace();
			}
		}
        Connection.waiting15 = true;
        if(find){
        	ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            final ListView list = (ListView)findViewById(R.id.pane_listView1);
            for(int i=0;i<arr.length;i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	item.put("l1", arr[i]);
            	item.put("l2", Connection.tuijianwindowname.get(i));
            	item.put("l3", Connection.tuijianwindowid.get(i));
            	items.add(item);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.simple_item2,new String[]{"l1","l2","l3"},new int[]{R.id.simple_item2_textView1,R.id.simple_item2_textView11,R.id.simple_item2_textView2});
        	list.setAdapter(adapter);
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
					new AlertDialog.Builder(this).setTitle("重新连接失败，请检查网络").setNegativeButton("确定", null).show();
				}else{
					new Thread(){
						public void run(){
							Connection.writer.write("login\n");
							Connection.writer.write(Connection.user.getId()+"\n");
							Connection.writer.write(Connection.user.getPasswd()+"\n");
							Connection.writer.flush();
						}
					}.start();
					new AlertDialog.Builder(this).setTitle("重新连接成功").setNegativeButton("确定", null).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Retry Connection Failed!");
				e.printStackTrace();
			}
		}
    }
}
