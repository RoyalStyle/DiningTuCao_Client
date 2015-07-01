package com.example.xidian_dining;
/*
 * �û���½�ɹ���ĵ�һ�������Ҫ��ʾ���������ת�����⹦�ܰ���
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
        	new AlertDialog.Builder(this).setTitle("ȷ���˳���").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
        		   public void onClick(DialogInterface dialog, int which){
        			   new Thread(){
        	        		public void run(){		
        	        			try {
        	        	//			Connection.pan2 = false;						//�����߳���ֹѭ��
        	        	//			Connection.heartBeaten.interrupt();				//�ر������߳�
        	        				Connection.pan = false;
        	        				Connection.listenThread.interrupt();
        							Connection.writer.write("logout\n");		//ͨ��Э��[client��server��]
        							Connection.writer.flush();
        							Connection.br.close();
        							Connection.writer.close();
        							Connection.client.close();
        							System.exit(0);
        						} catch (IOException e) {
        							System.out.println("Exit Error");
        							e.printStackTrace();
        						}
        	        		}
        	        	}.start();
        		   }
        		  }).setNegativeButton("ȡ��",null).show();
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
 				Connection.writer.write("windowinformation\n");			//ͨ��Э��[client��server��]����ʾ����ĳ���Ƽ����ڵ�[����-����]��Ϣ
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
				if(time>20){			//���ݳ�ʱ��δ�����ͳ�����������
					find = false;
					Connection.link = false;
		//			Connection.pan2 = false;						//�����߳���ֹѭ��
		//			Connection.heartBeaten.interrupt();				//�ر������߳�
					Connection.pan = false;
					Connection.listenThread.interrupt();
					Connection.br.close();
					Connection.writer.close();
					Connection.client.close();
					new AlertDialog.Builder(this).setTitle("����������ӳ�ʱ�����ڳ�����������").setNegativeButton("ȷ��", null).show();
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
					sc = "��";
				}else{
					sc = (String)(sc.subSequence(0, 3));
				}
				dishitem[i] = Connection.dish2.get(i)+"		����:"+sc;
			}
			new AlertDialog.Builder(this).setTitle(tv.getText().toString()).setItems(
					dishitem, null).setNegativeButton("ȷ��", null).show();
		}else{					//��������
			try {
				new Thread(){
					public void run(){
						Connection.connect();
					}
				}.start();
				try{
					Thread.sleep(2000);			//������Ҫʱ��
				}catch(Exception e){
					System.out.println("sleep failed");
					e.printStackTrace();
				}
				if(Connection.link == false){
					new AlertDialog.Builder(this).setTitle("��������ʧ�ܣ���������").setNegativeButton("ȷ��", null).show();
				}else{
					new Thread(){
						public void run(){
							Connection.writer.write("login\n");							//�������͸�����Ϣ
							Connection.writer.write(Connection.user.getId()+"\n");
							Connection.writer.write(Connection.user.getPasswd()+"\n");
							Connection.writer.flush();
						}
					}.start();
					new AlertDialog.Builder(this).setTitle("�������ӳɹ�").setNegativeButton("ȷ��", null).show();
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
        		Connection.writer.write("tuijian\n");				//ͨ��Э��[client��server��]�������Ƽ���Ϣ(�Ƽ��Ĵ���-����)
        		Connection.writer.flush();
        	}
        }.start();
        final Button b1 = (Button)findViewById(R.id.pane_button1);
        b1.setOnClickListener(new OnClickListener(){
        		//�[activity]ת�ƣ���Search activity[��������]����Search.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* ָ��intentҪ�������� */
                    intent.setClass(Pane.this, Search.class);
                    /* �رյ�ǰ��Activity */
                    Pane.this.finish();
                    /* ����һ���µ�Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b2 = (Button)findViewById(R.id.pane_button2);
        b2.setOnClickListener(new OnClickListener(){
        	//�[activity]ת�ƣ���Remark activity[���۽���]����Remark.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* ָ��intentҪ�������� */
                    intent.setClass(Pane.this, Remark.class);
                    /* �رյ�ǰ��Activity */
                    Pane.this.finish();
                    /* ����һ���µ�Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b3 = (Button)findViewById(R.id.pane_button3);
        b3.setOnClickListener(new OnClickListener(){
        	//�[activity]ת�ƣ���Speak activity[�²۽���]����Speak.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* ָ��intentҪ�������� */
                    intent.setClass(Pane.this, Speak.class);
                    /* �رյ�ǰ��Activity */
                    Pane.this.finish();
                    /* ����һ���µ�Activity */
                    Pane.this.startService(intent);
                    startActivity(intent);
            	}
        });
        final Button b4 = (Button)findViewById(R.id.pane_button4);
        b4.setOnClickListener(new OnClickListener(){
        	//�[activity]ת�ƣ���Information activity[������Ϣ����]����Information.java
            	public void onClick(View v){
            		Intent intent = new Intent();   
                    /* ָ��intentҪ�������� */
                    intent.setClass(Pane.this, Information.class);
                    /* �رյ�ǰ��Activity */
                    Pane.this.finish();
                    /* ����һ���µ�Activity */
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
			//		Connection.pan2 = false;						//�����߳���ֹѭ��
			//		Connection.heartBeaten.interrupt();				//�ر������߳�
					Connection.pan = false;
					Connection.listenThread.interrupt();
					Connection.br.close();
					Connection.writer.close();
					Connection.client.close();
					new AlertDialog.Builder(this).setTitle("����������ӳ�ʱ�����ڳ�����������").setNegativeButton("ȷ��", null).show();
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
					Thread.sleep(2000);			//������Ҫʱ��
				}catch(Exception e){
					System.out.println("sleep failed");
					e.printStackTrace();
				}
				if(Connection.link == false){
					new AlertDialog.Builder(this).setTitle("��������ʧ�ܣ���������").setNegativeButton("ȷ��", null).show();
				}else{
					new Thread(){
						public void run(){
							Connection.writer.write("login\n");
							Connection.writer.write(Connection.user.getId()+"\n");
							Connection.writer.write(Connection.user.getPasswd()+"\n");
							Connection.writer.flush();
						}
					}.start();
					new AlertDialog.Builder(this).setTitle("�������ӳɹ�").setNegativeButton("ȷ��", null).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Retry Connection Failed!");
				e.printStackTrace();
			}
		}
    }
}
