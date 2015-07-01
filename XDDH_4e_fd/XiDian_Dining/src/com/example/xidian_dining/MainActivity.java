package com.example.xidian_dining;
/*
 * 客户端启动后的第一个加载活动，显示登录[可以注册]
 */
import com.example.xidian_dining.Connection;
import com.example.xidian_dining.MainActivity;
import com.example.xidian_dining.Pane;
import com.example.xidian_dining.R;
import com.example.xidian_dining.Register;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

import java.io.*;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
        	//跳转至fragment_main.xml，跳过activity_main.xml
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(this))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //活动[activity]转移，至Pane activity[主界面]，见Pane.java
    private void shift(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(MainActivity.this, Pane.class);
        /* 关闭当前的Activity */
        MainActivity.this.finish();
        /* 启动一个新的Activity */
        MainActivity.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
    //活动[activity]转移，至注册activity，见Register.java
    private void shift2(){
    	Intent intent = new Intent();   
        /* 指定intent要启动的类 */
        intent.setClass(MainActivity.this, Register.class);
        /* 关闭当前的Activity */
        MainActivity.this.finish();
        /* 启动一个新的Activity */
        MainActivity.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	private MainActivity ma = null;
    	private Handler handler;
    	//private BufferedReader br;
    	//private PrintWriter wr;
        public PlaceholderFragment(MainActivity m) {
        	ma = m;
        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	if(Connection.link == false){
        		new MyThread1();
        	}
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
          /*try{
            	File file = new File("/data/login_cache.txt");
            	br = new BufferedReader(new FileReader(file));
            	wr = new PrintWriter(new FileWriter(file));
            }catch(Exception e){
            	System.out.println("File Access Failed!");
            	e.printStackTrace();
            }*/
            final Button log = (Button)rootView.findViewById(R.id.fragment_button1); 
            final Button register = (Button)rootView.findViewById(R.id.fragment_button2);
            final EditText number = (EditText)rootView.findViewById(R.id.fragment_editText1);
            final EditText passwd = (EditText)rootView.findViewById(R.id.fragment_editText2);
            final TextView result = (TextView)rootView.findViewById(R.id.fragment_textView3);
            /*try{
            	String brread = br.readLine();
            	number.setText(brread);
            }catch(Exception e){
            	System.out.println("Main error0 -> Read failed");
            	e.printStackTrace();
            }*/
            handler = new Handler(){
            	@Override
            	public void handleMessage(Message msg){
            		if(msg.what == 0x001){
            			result.setText("Log failed,"+msg.obj.toString());
            		}else if(msg.what == 0x002){
            			result.setText("Log success");
            			ma.shift();
            		}else if(msg.what == 0x003){
            			result.setText("正在登陆...");
            		}else if(msg.what == 0x004){
            			result.setText(msg.obj.toString()+"");
            		}
            	}
            };
            log.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		if(Connection.link == false){
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "网络连接失败，稍后重试";
        				handler.sendMessage(msg);
        				return;
            		}else{
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "网络连接成功";
        				handler.sendMessage(msg);
            		}
            		final String s1 = number.getText().toString().trim();
            		final String s2 = passwd.getText().toString().trim();
            		new Thread(){
            			public void run(){
            				try{
            					Connection.writer.write("login\n");		//通信协议[client发server收]，表示登录
            					Connection.writer.write(s1+"\n");		//账号发送给服务器
                				Connection.writer.write(s2+"\n");		//密码发送给服务器
                    			Connection.writer.flush();
                    			//Thread.sleep(500);
                    			{
                    				//工作线程禁止操作UI线程的组件，如果必要需要借助handler传递Message，完成相关功能
                    				Message msg = new Message();
                    				msg.what = 0x003;
                    				msg.obj = "正在登陆";
                    				handler.sendMessage(msg);
                    			}
                    			boolean find = true;
                    			int time=0;
                    			while(Connection.waiting == true){
                    				try{
                    					Thread.sleep(100);
                    					time++;
                    					if(time>20){
                    						find = false;
                    						Message msg = new Message();
                            				msg.what = 0x004;
                            				msg.obj = "与服务器连接超时，可以尝试跳转其他版面或重新登录";
                            				handler.sendMessage(msg);
                    						break;
                    					}
                    				}catch(Exception e){
                    					System.out.println("Pane Message recv timeout error");
                    					e.printStackTrace();
                    				}
                    			}
                    			Connection.waiting = true;
                    			if(find){
                    				if(Connection.result.equals("nouser")){				
                        				Message msg = new Message();
                        				msg.what = 0x001;
                        				msg.obj = "账号错误";
                        				handler.sendMessage(msg);
                        			}else if(Connection.result.equals("passwderror")){
                        				Message msg = new Message();
                        				msg.what = 0x001;
                        				msg.obj = "密码错误";
                        				handler.sendMessage(msg);
                        			}else if(Connection.result.equals("alreadyonline")){
                        				Message msg = new Message();
                        				msg.what = 0x001;
                        				msg.obj = "账户已在线";
                        				handler.sendMessage(msg);
                        			}else{
                        				Message msg = new Message();
                        				msg.what = 0x002;
                        				msg.obj = "登录成功";
                        				/*try{
                        					wr.write(s1+"\n");
                        					wr.flush();
                        		            br.close();
                        		            wr.close();
                        				}catch(Exception e){
                        					System.out.println("File Write Failed!");
                        					e.printStackTrace();
                        				}*/
                        				handler.sendMessage(msg);
                        			}
                    			}else{
                    				try {
                    					Connection.link = false;
                   // 					Connection.pan2 = false;						//心跳线程终止循环
                   // 					Connection.heartBeaten.interrupt();				//关闭心跳线程
                    					Connection.pan = false;
                    					Connection.listenThread.interrupt();
                    					Connection.br.close();
                    					Connection.writer.close();
                    					Connection.client.close();
                    					new Thread(){
                    						public void run(){
                    							Connection.connect();
                    						}
                    					}.start();
                    					try{
                    						Thread.sleep(2000);			//重连需要时间
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
                    				} catch (IOException e) {
                    					// TODO Auto-generated catch block
                    					System.out.println("Retry Connection Failed!");
                    					e.printStackTrace();
                    				}
                    			}
            				}catch(Exception e){
            					System.out.println("Main error1 -> log failed");
            					e.printStackTrace();
            				}
            			}
            		}.start();
            	}
            });
            register.setOnClickListener(new OnClickListener(){
            	public void onClick(View v){
            		if(Connection.link == false){
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "网络连接失败，稍后重试";
        				handler.sendMessage(msg);
        				return;
            		}else{
            			Connection.number = number.getText().toString().trim();
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "网络连接成功";
        				handler.sendMessage(msg);
            		}
            		result.setText("Register");
            		ma.shift2();
            	}
            });
            new Thread(){
            	public void run(){
            		Message msg = new Message();
            		if(Connection.link == false){
            			msg.what = 0x004;
        				msg.obj = "世界上最遥远的距离就是没有网";
        				handler.sendMessage(msg);
            		}
            		while(Connection.link == false){
            			try{
            				sleep(1000);
            			}catch(Exception e){
            				System.out.println("Main error2 -> Beat heart failed");
            				e.printStackTrace();
            			}
            		}
            		Message msg2 = new Message();
            		msg2.what = 0x004;
    				msg2.obj = "与服务器建立连接成功";
    				handler.sendMessage(msg2);
            	}
            }.start();
            return rootView;
        }
        private class MyThread1 extends Thread{
        	public MyThread1(){
        		start();
        	}
        	public void run(){
        		Connection.connect();
        	}
        }
    }

}
