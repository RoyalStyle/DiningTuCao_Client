package com.example.xidian_dining;
/*
 * �ͻ���������ĵ�һ�����ػ����ʾ��¼[����ע��]
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
        	//��ת��fragment_main.xml������activity_main.xml
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
    //�[activity]ת�ƣ���Pane activity[������]����Pane.java
    private void shift(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(MainActivity.this, Pane.class);
        /* �رյ�ǰ��Activity */
        MainActivity.this.finish();
        /* ����һ���µ�Activity */
        MainActivity.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
    //�[activity]ת�ƣ���ע��activity����Register.java
    private void shift2(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(MainActivity.this, Register.class);
        /* �رյ�ǰ��Activity */
        MainActivity.this.finish();
        /* ����һ���µ�Activity */
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
            			result.setText("���ڵ�½...");
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
        				msg.obj = "��������ʧ�ܣ��Ժ�����";
        				handler.sendMessage(msg);
        				return;
            		}else{
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "�������ӳɹ�";
        				handler.sendMessage(msg);
            		}
            		final String s1 = number.getText().toString().trim();
            		final String s2 = passwd.getText().toString().trim();
            		new Thread(){
            			public void run(){
            				try{
            					Connection.writer.write("login\n");		//ͨ��Э��[client��server��]����ʾ��¼
            					Connection.writer.write(s1+"\n");		//�˺ŷ��͸�������
                				Connection.writer.write(s2+"\n");		//���뷢�͸�������
                    			Connection.writer.flush();
                    			//Thread.sleep(500);
                    			{
                    				//�����߳̽�ֹ����UI�̵߳�����������Ҫ��Ҫ����handler����Message�������ع���
                    				Message msg = new Message();
                    				msg.what = 0x003;
                    				msg.obj = "���ڵ�½";
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
                            				msg.obj = "����������ӳ�ʱ�����Գ�����ת������������µ�¼";
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
                        				msg.obj = "�˺Ŵ���";
                        				handler.sendMessage(msg);
                        			}else if(Connection.result.equals("passwderror")){
                        				Message msg = new Message();
                        				msg.what = 0x001;
                        				msg.obj = "�������";
                        				handler.sendMessage(msg);
                        			}else if(Connection.result.equals("alreadyonline")){
                        				Message msg = new Message();
                        				msg.what = 0x001;
                        				msg.obj = "�˻�������";
                        				handler.sendMessage(msg);
                        			}else{
                        				Message msg = new Message();
                        				msg.what = 0x002;
                        				msg.obj = "��¼�ɹ�";
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
                   // 					Connection.pan2 = false;						//�����߳���ֹѭ��
                   // 					Connection.heartBeaten.interrupt();				//�ر������߳�
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
        				msg.obj = "��������ʧ�ܣ��Ժ�����";
        				handler.sendMessage(msg);
        				return;
            		}else{
            			Connection.number = number.getText().toString().trim();
            			Message msg = new Message();
        				msg.what = 0x004;
        				msg.obj = "�������ӳɹ�";
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
        				msg.obj = "��������ңԶ�ľ������û����";
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
    				msg2.obj = "��������������ӳɹ�";
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
