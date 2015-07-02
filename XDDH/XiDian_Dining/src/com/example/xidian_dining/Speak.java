package com.example.xidian_dining;
/*
 * 吐槽活动，选择好吐槽的菜后发表品论内容和分数
 */
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Speak extends Activity{
	private ArrayAdapter<String> adapter;
	private Spinner layerSpinner;
    private Spinner windowSpinner;
    private Spinner dishSpinner;
	private static final String[] scoreitem={"1","2","3","4","5"};
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
        setContentView(R.layout.speak);
        layerSpinner = (Spinner)findViewById(R.id.speak_spinner2);
        windowSpinner = (Spinner)findViewById(R.id.speak_spinner3);
        dishSpinner = (Spinner)findViewById(R.id.speak_spinner4);
        final Spinner buildingSpinner = (Spinner)findViewById(R.id.speak_spinner1);
        final Spinner scoreSpinner = (Spinner)findViewById(R.id.speak_spinner5);
        final Button saveButton = (Button)findViewById(R.id.speak_Button1);
        final Button exitButton = (Button)findViewById(R.id.speak_Button2);
        final EditText talkText = (EditText)findViewById(R.id.speak_editText);
        final TextView result = (TextView)findViewById(R.id.speak_textView6);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,scoreitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSpinner.setAdapter(adapter);
        scoreSpinner.setVisibility(View.VISIBLE);
        new Thread(){
        	public void run(){
				Connection.writer.write("build"+"\n");
				Connection.writer.flush();
			}
        }.start();
    /*  try{
			Thread.sleep(500);
		}catch(Exception e){
			System.out.println("Speak error1 -> build failed");
			e.printStackTrace();
		}*/
        boolean find = true;
		int time=0;
		while(Connection.waiting5 == true){
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
					result.setText("与服务器连接超时，正在尝试重新连接");
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
        	buildingSpinner.setAdapter(adapter);
        	buildingSpinner.setVisibility(View.VISIBLE);
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
        buildingSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
    				public void run(){
    					Connection.writer.write("layer"+"\n");
    					Connection.writer.write(buildingSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.flush();
    				}
    			}.start();
    		/*	try{
    				Thread.sleep(500);
    			}catch(Exception e){
    				System.out.println("Speak error2 -> layer failed");
    				e.printStackTrace();
    			}*/
    	        boolean find = true;
    			int time=0;
    			while(Connection.waiting6 == true){
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
    						result.setText("与服务器连接超时，正在尝试重新连接");
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
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        layerSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
    				public void run(){
    					Connection.writer.write("window"+"\n");
    					Connection.writer.write(buildingSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.write(layerSpinner.getSelectedItem().toString().trim()+"\n");
    					Connection.writer.flush();
    				}
    			}.start();
    		/*	try{
    				Thread.sleep(500);
    			}catch(Exception e){
    				System.out.println("Speak error3 -> window failed");
    				e.printStackTrace();
    			}*/
    			boolean find = true;
    			int time=0;
    			while(Connection.waiting7 == true){
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
    						result.setText("与服务器连接超时，正在尝试重新连接");
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
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        windowSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        		new Thread(){
    				public void run(){
    					Connection.writer.write("recipe"+"\n");
    					Connection.writer.write(buildingSpinner.getSelectedItem().toString().trim()+"\n");
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
        		//			Connection.pan2 = false;						//心跳线程终止循环
        		//			Connection.heartBeaten.interrupt();				//关闭心跳线程
        					Connection.pan = false;
        					Connection.listenThread.interrupt();
        		//			Connection.br.close();
        		//			Connection.writer.close();
        					Connection.client.close();
    						result.setText("与服务器连接超时，正在尝试重新连接");
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
        	public void onNothingSelected(AdapterView<?> arg0) {
        		//do Noting
        	}
        });
        saveButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		String build = (buildingSpinner.getSelectedItem()).toString().trim();
        		String layer = (layerSpinner.getSelectedItem()).toString().trim();
        		String window = (windowSpinner.getSelectedItem()).toString().trim();
        		String dish = (dishSpinner.getSelectedItem()).toString().trim();
        		String score = (scoreSpinner.getSelectedItem()).toString().trim();
        		String talk = talkText.getText().toString().trim();
        		if(talk.equals("") || build.equals("") || layer.equals("") || window.equals("") || dish.equals("")){
        			result.setText("评论为空");
        		}else if(talk.length() >= 100){
        			result.setText("超过最大评论字数限制");
        		}else{
        			new Thread(){
        				public void run(){
        					Connection.writer.write("tucao"+"\n");
        					Connection.writer.write(Connection.user.getName()+"\n");
        					Connection.writer.write(buildingSpinner.getSelectedItem().toString().trim()+"\n");
        					Connection.writer.write(layerSpinner.getSelectedItem().toString().trim()+"\n");
        					Connection.writer.write(windowSpinner.getSelectedItem().toString().trim()+"\n");
        					Connection.writer.write(dishSpinner.getSelectedItem().toString().trim()+"\n");
        					Connection.writer.write(talkText.getText().toString().trim()+"\n");
        					Connection.writer.write(scoreSpinner.getSelectedItem().toString().trim()+"\n");
        					Connection.writer.flush();
        				}
        			}.start();
        			boolean find = true;
        			int time=0;
        			while(Connection.waiting9 == true){
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
        						result.setText("与服务器连接超时，正在尝试重新连接");
        						break;
        					}
        				}catch(Exception e){
        					System.out.println("Pane Message recv timeout error");
        					e.printStackTrace();
        				}
        			}
        	        Connection.waiting9 = true;
        	        if(find){
        	        	if(Connection.result.equals("OK")){
            	        	result.setText("评论成功");
            	        }else{
            	        	result.setText("评论失败");
            	        }
            			shift();
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
        exitButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		shift();
        	}
        });
	}
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
	private void dish(){
		String[] dishitem = new String[Connection.dish.size()];
        for(int i=0;i<Connection.dish.size();i++){
        	dishitem[i] = (String)(Connection.dish.get(i));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dishitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dishSpinner.setAdapter(adapter);
        dishSpinner.setVisibility(View.VISIBLE);
	}
	//活动[activity]转移，至Pane activity[主界面]，见Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(Speak.this, Pane.class);
        /* 关闭当前的Activity */
        Speak.this.finish();
        /* 启动一个新的Activity */
        Speak.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
