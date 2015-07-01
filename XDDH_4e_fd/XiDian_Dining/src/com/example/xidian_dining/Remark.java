package com.example.xidian_dining;
/*
 * ���ۻ����ʾ�������µ� 50������
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Remark extends Activity{
	private String reid = "";
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
	public void mybuttonlistener1(View v){
 		reid = "";
 		final ListView lv = (ListView)findViewById(R.id.remark_listView1);
 		SimpleAdapter adapter = (SimpleAdapter)(lv.getAdapter());
 		final Button tbtn = (Button) v;
 		for (int i = 0; i < lv.getChildCount(); i++) {
 		     LinearLayout layout = (LinearLayout)lv.getChildAt(i);
 		     if(tbtn == (Button)layout.findViewById(R.id.simple_button1)){
 		    	 final TextView idText = (TextView)layout.findViewById(R.id.simple_textView4);
 		    	 reid = idText.getText().toString();
 		    	 break;
 		     }
 		}
 		new Thread(){
 			public void run(){
 				Connection.writer.write("zan\n");			//ͨ��Э��[client��server��]����ʾ�ͻ������˶�ĳ�����۵���
 				Connection.writer.write(reid+"\n");
 				Connection.writer.write(Connection.user.getId()+"\n");
 				Connection.writer.flush();
 			}
 		}.start();
 		boolean find = true;
		int time=0;
		while(Connection.waiting12 == true){
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
				System.out.println("Pane Message recv timeout error");
				e.printStackTrace();
			}
		}
        Connection.waiting12 = true;
        if(find){
        	if(Connection.result.equals("succeed")){
         		String content = tbtn.getText().toString().trim();
         		Pattern p = Pattern.compile("[0-9][0-9]*");
                Matcher m = p.matcher(content);
                m.find();
                int a = (new Integer(m.group())).intValue();
                a++;
                ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
                for(int i=0;i<Connection.remark.size();i++){
                	Map<String,Object> item = new HashMap<String,Object>();
                	if(reid.equals(Connection.remark.get(i).getTimeId())){
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+a);
                    	Connection.remark.get(i).setZanCount(a);
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}else{
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}
                }
                adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
                //tbtn.setText("�� "+a);
                lv.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }else if(Connection.result.equals("repeat")){
         		String content = tbtn.getText().toString().trim();
         		Pattern p = Pattern.compile("[0-9][0-9]*");
                Matcher m = p.matcher(content);
                m.find();
                int a = (new Integer(m.group())).intValue();
                a--;
                ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
                for(int i=0;i<Connection.remark.size();i++){
                	Map<String,Object> item = new HashMap<String,Object>();
                	if(reid.equals(Connection.remark.get(i).getTimeId())){
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	Connection.remark.get(i).setZanCount(a);
                    	item.put("l4", "�� "+a);
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}else{
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}
                }
                adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
                lv.setAdapter(adapter);
                //tbtn.setText("�� "+a);
                //adapter.notifyDataSetChanged();
            }
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
 	public void mybuttonlistener2(View v){
 		reid = "";
 		final ListView lv = (ListView)findViewById(R.id.remark_listView1);
 		SimpleAdapter adapter = (SimpleAdapter)(lv.getAdapter());
 		Button tbtn = (Button) v;
 		for (int i = 0; i < lv.getChildCount(); i++) {
 		     LinearLayout layout = (LinearLayout)lv.getChildAt(i);
 		     if(tbtn == (Button)layout.findViewById(R.id.simple_button2)){
 		    	 final TextView idText = (TextView)layout.findViewById(R.id.simple_textView4);
 		    	 reid = idText.getText().toString();
 		    	 break;
 		     }
 		}
 		new Thread(){
 			public void run(){
 				Connection.writer.write("cai\n");				//ͨ��Э��[client��server��]����ʾ�ͻ������˶�ĳ�����۵��
 				Connection.writer.write(reid+"\n");
 				Connection.writer.write(Connection.user.getId()+"\n");
 				Connection.writer.flush();
 			}
 		}.start();
 		boolean find = true;
		int time=0;
		while(Connection.waiting13 == true){
			try{
				Thread.sleep(100);
				time++;
				if(time>20){
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
        Connection.waiting13 = true;
        if(find){
        	if(Connection.result.equals("succeed")){
         		String content = tbtn.getText().toString().trim();
         		Pattern p = Pattern.compile("[0-9][0-9]*");
                Matcher m = p.matcher(content);
                m.find();
                int a = (new Integer(m.group())).intValue();
                a++;
                ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
                for(int i=0;i<Connection.remark.size();i++){
                	Map<String,Object> item = new HashMap<String,Object>();
                	if(reid.equals(Connection.remark.get(i).getTimeId())){
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	Connection.remark.get(i).setCaiCount(a);
                    	item.put("l5", "�� "+a);
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}else{
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}
                }
                adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
                //tbtn.setText("�� "+a);
                lv.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }else if(Connection.result.equals("repeat")){
         		String content = tbtn.getText().toString().trim();
         		Pattern p = Pattern.compile("[0-9][0-9]*");
                Matcher m = p.matcher(content);
                m.find();
                int a = (new Integer(m.group())).intValue();
                a--;
                ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
                for(int i=0;i<Connection.remark.size();i++){
                	Map<String,Object> item = new HashMap<String,Object>();
                	if(reid.equals(Connection.remark.get(i).getTimeId())){
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	Connection.remark.get(i).setCaiCount(a);
                    	item.put("l5", "�� "+a);
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}else{
                		item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
                    	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
                    	String sc = ""+(Connection.remark.get(i).getDishScore());
                    	if(sc.equals("0.0")){
                    		sc = "��";
                    	}
                    	item.put("l3", "����:"+sc);
                    	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
                    	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
                    	item.put("l6", Connection.remark.get(i).getTimeId());
                    	items.add(item);
                	}
                }
                adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
                //tbtn.setText("�� "+a);
                lv.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }
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
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remark);
        new Thread(){
        	public void run(){
        		Connection.writer.write("remark\n");
        		Connection.writer.flush();
        	}
        }.start();
        final Button exitButton = (Button)findViewById(R.id.remark_button1);
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		shift();
        	}
        });
        final Button upButton = (Button)findViewById(R.id.simple_button1);
        final Button downButton = (Button)findViewById(R.id.simple_button2);
        boolean find = true;
		int time=0;
		while(Connection.waiting4 == true){
			try{
				Thread.sleep(100);
				time++;
				if(time>20){
					find = false;
					Connection.link = false;
		//			Connection.pan2 = false;						//�����߳���ֹѭ��
		//			Connection.heartBeaten.interrupt();				//�ر������߳�
					Connection.pan = false;
					Connection.listenThread.interrupt();
					Connection.br.close();
					Connection.writer.close();
					Connection.client.close();
					new AlertDialog.Builder(this).setTitle("����������ӳ�ʱ�����Գ�����ת������������µ�¼").setNegativeButton("ȷ��", null).show();
					break;
				}
			}catch(Exception e){
				System.out.println("Pane Message recv timeout error");
				e.printStackTrace();
			}
		}
        Connection.waiting4 = true;
        if(find){
        	ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
            final ListView list = (ListView)findViewById(R.id.remark_listView1);
            for(int i=0;i<Connection.remark.size();i++){
            	Map<String,Object> item = new HashMap<String,Object>();
            	item.put("l1", (String)(Connection.remark.get(i).getBuildName()+" "+Connection.remark.get(i).getLayer()+"��"+" "+Connection.remark.get(i).getWindowName()+" "+Connection.remark.get(i).getDishName())+"\n");
            	item.put("l2", Connection.remark.get(i).getUserName()+":"+(String)(Connection.remark.get(i).getContent())+"\n");
            	String sc = ""+(Connection.remark.get(i).getDishScore());
            	if(sc.equals("0.0")){
            		sc = "��";
            	}
            	item.put("l3", "����:"+sc);
            	item.put("l4", "�� "+(Connection.remark.get(i).getZanCount()));
            	item.put("l5", "�� "+(Connection.remark.get(i).getCaiCount()));
            	item.put("l6", Connection.remark.get(i).getTimeId());
            	items.add(item);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.simple_item,new String[]{"l1","l2","l3","l4","l5","l6"},new int[]{R.id.simple_textView1,R.id.simple_textView2,R.id.simple_textView3,R.id.simple_button1,R.id.simple_button2,R.id.simple_textView4});
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
					/*new Thread(){
						public void run(){
							Connection.writer.write("login\n");
							Connection.writer.write(Connection.user.getId()+"\n");
							Connection.writer.write(Connection.user.getPasswd()+"\n");
							Connection.writer.flush();
						}
					}.start();*/
					new AlertDialog.Builder(this).setTitle("�������ӳɹ�").setNegativeButton("ȷ��", null).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Retry Connection Failed!");
				e.printStackTrace();
			}
		}
	}
	//�[activity]ת�ƣ���Pane activity[������]����Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Remark.this, Pane.class);
        /* �رյ�ǰ��Activity */
        Remark.this.finish();
        /* ����һ���µ�Activity */
        Remark.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
