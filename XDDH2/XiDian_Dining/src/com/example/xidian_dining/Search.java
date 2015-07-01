package com.example.xidian_dining;
/*
 * ����������԰����������õ���Ӧ����������������Ϣ��Ҳ���԰����������õ���Ӧ���ڵ�����[����-����]��Ϣ
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
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Search extends Activity{
	private ArrayAdapter<String> adapter;
	private Spinner layerSpinner;
    private Spinner windowSpinner;
    private Spinner dishSpinner;
    private Spinner buildSpinner;
	private String build;
	private String layer;
	private String window;
	private String dish;
	private String doWhat;
	private ListView list;
	private String reid = "";
	private static final String[] temp = {"--"};
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
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        final Button exitButton = (Button)findViewById(R.id.search_button1);
        final Button searchButton = (Button)findViewById(R.id.search_button2);
        buildSpinner = (Spinner)findViewById(R.id.search_spinner1);
        layerSpinner = (Spinner)findViewById(R.id.search_spinner2);
        windowSpinner = (Spinner)findViewById(R.id.search_spinner3);
        dishSpinner = (Spinner)findViewById(R.id.search_spinner4);
        final TextView result = (TextView)findViewById(R.id.search_textView5);
        list = (ListView)findViewById(R.id.search_listView1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layerSpinner.setAdapter(adapter);
        layerSpinner.setVisibility(View.VISIBLE);
        buildSpinner.setAdapter(adapter);
        buildSpinner.setVisibility(View.VISIBLE);
        windowSpinner.setAdapter(adapter);
        windowSpinner.setVisibility(View.VISIBLE);
        dishSpinner.setAdapter(adapter);
        dishSpinner.setVisibility(View.VISIBLE);
        new Thread(){
        	public void run(){
				Connection.writer.write("build"+"\n");			//ͨ��Э��[client��server��]�������������
				Connection.writer.flush();
			}
        }.start();
        boolean find = true;
		int time=0;
		while(Connection.waiting5 == true){
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
					result.setText("����������ӳ�ʱ�����ڳ�������������");
					break;
				}
			}catch(Exception e){
				System.out.println("Pane Message recv timeout error");
				e.printStackTrace();
			}
		}
        Connection.waiting5 = true;
        if(find){
        	String[] builditem = new String[Connection.build.size()];
            for(int i=0;i<Connection.build.size();i++){
            	builditem[i] = (String)(Connection.build.get(i));
            }
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,builditem);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        	buildSpinner.setAdapter(adapter);
        	buildSpinner.setVisibility(View.VISIBLE);
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
        buildSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
					public void run(){
						Connection.writer.write("layer"+"\n");		//ͨ��Э��[client��server��]������ĳ������¥����Ϣ
						Connection.writer.write(buildSpinner.getSelectedItem().toString().trim()+"\n");
						Connection.writer.flush();
					}
				}.start();
				boolean find = true;
				int time=0;
				while(Connection.waiting6 == true){
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
							result.setText("����������ӳ�ʱ�����ڳ�������������");
							break;
						}
					}catch(Exception e){
						System.out.println("Pane Message recv timeout error");
						e.printStackTrace();
					}
				}
    	        Connection.waiting6 = true;
    	        if(find){
    	        	layer();
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
    						result.setText("��������ʧ�ܣ���������");
    					}else{
    						new Thread(){
    							public void run(){
    								Connection.writer.write("login\n");
    								Connection.writer.write(Connection.user.getId()+"\n");
    								Connection.writer.write(Connection.user.getPasswd()+"\n");
    								Connection.writer.flush();
    							}
    						}.start();
    						result.setText("�������ӳɹ�");
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					System.out.println("Retry Connection Failed!");
    					e.printStackTrace();
    				}
    			}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        layerSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
					public void run(){
						Connection.writer.write("window"+"\n");				//ͨ��Э��[client��server��]������ĳ������ĳ��¥�����д�����Ϣ
						Connection.writer.write(buildSpinner.getSelectedItem().toString().trim()+"\n");
						Connection.writer.write(layerSpinner.getSelectedItem().toString().trim()+"\n");
						Connection.writer.flush();
					}
				}.start();
				boolean find = true;
				int time=0;
				while(Connection.waiting7 == true){
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
							result.setText("����������ӳ�ʱ�����ڳ�������������");
							break;
						}
					}catch(Exception e){
						System.out.println("Pane Message recv timeout error");
						e.printStackTrace();
					}
				}
    	        Connection.waiting7 = true;
    	        if(find){
    	        	window();
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
    						result.setText("��������ʧ�ܣ���������");
    					}else{
    						new Thread(){
    							public void run(){
    								Connection.writer.write("login\n");
    								Connection.writer.write(Connection.user.getId()+"\n");
    								Connection.writer.write(Connection.user.getPasswd()+"\n");
    								Connection.writer.flush();
    							}
    						}.start();
    						result.setText("�������ӳɹ�");
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					System.out.println("Retry Connection Failed!");
    					e.printStackTrace();
    				}
    			}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        windowSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
    				public void run(){
    					Connection.writer.write("recipe"+"\n");				//ͨ��Э��[client��server��]������ĳ������ĳ��¥��ĳ���������в���Ϣ
    					Connection.writer.write(buildSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.write(layerSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.write(windowSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.flush();
    				}
    			}.start();
    			boolean find = true;
				int time=0;
				while(Connection.waiting8 == true){
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
							result.setText("����������ӳ�ʱ�����ڳ�����������");
							break;
						}
					}catch(Exception e){
						System.out.println("Pane Message recv timeout error");
						e.printStackTrace();
					}
				}
    	        Connection.waiting8 = true;
    	        if(find){
    	        	dish();
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
    						result.setText("��������ʧ�ܣ���������");
    					}else{
    						new Thread(){
    							public void run(){
    								Connection.writer.write("login\n");
    								Connection.writer.write(Connection.user.getId()+"\n");
    								Connection.writer.write(Connection.user.getPasswd()+"\n");
    								Connection.writer.flush();
    							}
    						}.start();
    						result.setText("�������ӳɹ�");
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					System.out.println("Retry Connection Failed!");
    					e.printStackTrace();
    				}
    			}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        searchButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		build = (buildSpinner.getSelectedItem()).toString().trim();
        		layer = (layerSpinner.getSelectedItem()).toString().trim();
        		window = (windowSpinner.getSelectedItem()).toString().trim();
        		dish = (dishSpinner.getSelectedItem()).toString().trim();
        		if((dishSpinner.getSelectedItem()).toString().equals("--")){
        			dish = "";
        		}
        		if(build.equals("") && layer.equals("") && window.equals("") && dish.equals("")){
        			result.setText("������ϢΪ��");
        		}else if(dish.equals("")){
        			new Thread(){
        				public void run(){
        					doWhat = "dimsearch";
        					Connection.writer.write(doWhat+"\n");			//ͨ��Э��[client��server��]����������[������]
        					Connection.writer.write(build+"\n");
        					Connection.writer.write(layer+"\n");
        					Connection.writer.write(window+"\n");
        					Connection.writer.write(dish+"\n");
        					Connection.writer.flush();
        				}
        			}.start();
        			boolean find = true;
    				int time=0;
        			while(Connection.waiting11 == true){
    					try{
    						Thread.sleep(100);
    						time++;
    						if(time>20){
    							find = false;
    							Connection.link = false;
            	//				Connection.pan2 = false;						//�����߳���ֹѭ��
            	//				Connection.heartBeaten.interrupt();				//�ر������߳�
            					Connection.pan = false;
            					Connection.listenThread.interrupt();
            					Connection.br.close();
            					Connection.writer.close();
            					Connection.client.close();
    							result.setText("����������ӳ�ʱ�����ڳ�����������");
    							break;
    						}
    					}catch(Exception e){
    						System.out.println("Pane Message recv timeout error");
    						e.printStackTrace();
    					}
    				}
        	        Connection.waiting11 = true;
        	        if(find){
        	        	result.setText("�����������ô���������Ϣ");
            	        display_window();
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
        						result.setText("��������ʧ�ܣ���������");
        					}else{
        						new Thread(){
        							public void run(){
        								Connection.writer.write("login\n");
        								Connection.writer.write(Connection.user.getId()+"\n");
        								Connection.writer.write(Connection.user.getPasswd()+"\n");
        								Connection.writer.flush();
        							}
        						}.start();
        						result.setText("�������ӳɹ�");
        					}
        				} catch (Exception e) {
        					// TODO Auto-generated catch block
        					System.out.println("Retry Connection Failed!");
        					e.printStackTrace();
        				}
        			}
        		}else{
        			new Thread(){
        				public void run(){
        					doWhat = "dimsearch";
        					Connection.writer.write(doWhat+"\n");			//ͨ��Э��[client��server��]����������[������]
        					Connection.writer.write(build+"\n");
        					Connection.writer.write(layer+"\n");
        					Connection.writer.write(window+"\n");
        					Connection.writer.write(dish+"\n");
        					Connection.writer.flush();
        				}
        			}.start();
        			boolean find = true;
    				int time=0;
        			while(Connection.waiting10 == true){
    					try{
    						Thread.sleep(100);
    						time++;
    						if(time>20){
    							find = false;
    							Connection.link = false;
            	//				Connection.pan2 = false;						//�����߳���ֹѭ��
            	//				Connection.heartBeaten.interrupt();				//�ر������߳�
            					Connection.pan = false;
            					Connection.listenThread.interrupt();
            					Connection.br.close();
            					Connection.writer.close();
            					Connection.client.close();
    							result.setText("����������ӳ�ʱ�����ڳ�����������");
    							break;
    						}
    					}catch(Exception e){
    						System.out.println("Pane Message recv timeout error");
    						e.printStackTrace();
    					}
    				}
        	        Connection.waiting10 = true;
        	        if(find){
        	        	if(Connection.result.equals("OK")){
            	        	result.setText("����������������Ϣ������ʾ");
            	        	display_dish();
            	        }else{
            	        	list.setAdapter(null);
            	        	result.setText("��������������Ϊ��");
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
        						result.setText("��������ʧ�ܣ���������");
        					}else{
        						new Thread(){
        							public void run(){
        								Connection.writer.write("login\n");
        								Connection.writer.write(Connection.user.getId()+"\n");
        								Connection.writer.write(Connection.user.getPasswd()+"\n");
        								Connection.writer.flush();
        							}
        						}.start();
        						result.setText("�������ӳɹ�");
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
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		shift();
        	}
        });
	}
	/*
	 * �����б���ʾ��Ӧ��������¥��
	 */
	private void layer(){
		String[] layeritem = new String[Connection.layer.size()];
        for(int i=0;i<Connection.layer.size();i++){
        	layeritem[i] = (String)(Connection.layer.get(i));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,layeritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layerSpinner.setAdapter(adapter);
        layerSpinner.setVisibility(View.VISIBLE);
	}
	/*
	 * �����б���ʾ��Ӧ������Ӧ¥�����д���
	 */
	private void window(){
		String[] windowitem = new String[Connection.window.size()];
        for(int i=0;i<Connection.window.size();i++){
        	windowitem[i] = (String)(Connection.window.get(i));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,windowitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(adapter);
        windowSpinner.setVisibility(View.VISIBLE);
	}
	/*
	 * �����б���ʾ��Ӧ������Ӧ¥���Ӧ�������в���
	 */
	private void dish(){
		String[] dishitem = new String[Connection.dish.size()+1];
		dishitem[0] = "--";
        for(int i=0;i<Connection.dish.size();i++){
        	dishitem[i+1] = (String)(Connection.dish.get(i));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dishitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dishSpinner.setAdapter(adapter);
        dishSpinner.setVisibility(View.VISIBLE);
	}
	/*
	 * �����б���ʾ��Ӧ������Ӧ¥���Ӧ���ڶ�Ӧ������������
	 */
	private void display_dish(){
		ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(int i=0;i<Connection.remark2.size();i++){
        	Map<String,Object> item = new HashMap<String,Object>();
        	item.put("l1", (String)(Connection.remark2.get(i).getBuildName()+" "+Connection.remark2.get(i).getLayer()+"��"+" "+Connection.remark2.get(i).getWindowName()+" "+Connection.remark2.get(i).getDishName())+"\n");
        	item.put("l2", Connection.remark2.get(i).getUserName()+":"+(String)(Connection.remark2.get(i).getContent())+"\n");
        	item.put("l3", "����:"+(Connection.remark2.get(i).getDishScore()));
        	item.put("l4", ""+Connection.remark2.get(i).getTimeId());
        	items.add(item);
        }
		SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.simple_item3,new String[]{"l1","l2","l3","l4",},new int[]{R.id.simple3_textView1,R.id.simple3_textView2,R.id.simple3_textView3,R.id.simple3_textView4});
        list.setAdapter(adapter);
	}
	/*
	 * �����б���ʾ��Ӧ������Ӧ¥���Ӧ����������Ϣ
	 */
	private void display_window(){
		ArrayList<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
        Map<String,Object> item = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<Connection.dish.size();i++){
        	sb.append("Dish"+(i+1)+" "+(String)(Connection.dish.get(i))+"\n");
        }
        String sc = Connection.window_score;
        if(sc.equals("0.0")){
        	sc = "��";
        }else{
			sc = (String)(sc.subSequence(0, 3));
		}
        item.put("l1", build+" "+layer+" "+window+"	      "+"����:"+sc);
        item.put("l2", sb.toString());
        items.add(item);
        SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.simple_item4,new String[]{"l1","l2"},new int[]{R.id.simple_item4_textView1,R.id.simple_item4_textView11});
        list.setAdapter(adapter);
	}
	//�[activity]ת�ƣ���Pane activity[������]����Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Search.this, Pane.class);
        /* �رյ�ǰ��Activity */
        Search.this.finish();
        /* ����һ���µ�Activity */
        Search.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
