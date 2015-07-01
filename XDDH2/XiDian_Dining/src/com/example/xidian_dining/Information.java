package com.example.xidian_dining;
/*
 * ������Ϣ������Բ鿴���޸ĸ�����Ϣ
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
	private String grade = new String("��һ");
	private String sex = new String("��");
	private String phone = new String("");
	private String birth = new String("��԰");
	private final static int DATE_DIALOG = 0;
	private Button dateButton;
	private static final String[] gradeitem={"--","��һ","���","����","����","��һ","�ж�","����"};
	private ArrayAdapter<String> adapter = null;
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 * Android�ֻ����ؼ��¼� --- Ӧ�ó����˳�
	 */
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
        			sex = "��";
        		}else if(checkedId == R.id.information_radioFemale){
        			sex = "Ů";
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
        //�����Է�����
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
        			result.setText("���벻��Ϊ��");
        		}else if(!passwd.equals(passwd2)){
        			result.setText("�������벻һ��");
        		}else{
        			if(birth.equals("δ����")){
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
        			result.setText("�޸���...");
        			boolean find = true;
        			int time=0;
        			while(Connection.waiting2 == true){
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
                				result.setText("����������ӳ�ʱ�����Գ�����ת������������µ�¼");
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
            				result.setText("�޸ĳɹ�");
            			}else{
            				result.setText("�޸�ʧ��");
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
        numberText.setText(Connection.user.getId());				//��Щ������Ϣ��Connection.user�ж����У�һ��¼�ͻ��յ����Է������ĸ�����Ϣ
        passwdText.setText(Connection.user.getPasswd());
        passwdText2.setText(Connection.user.getPasswd());
        nameText.setText(Connection.user.getName());
        emailText.setText(Connection.user.getEmail());
        phoneText.setText(Connection.user.getPhone());
        String temp = Connection.user.getBirth().equals("")?"δ����":Connection.user.getBirth();
        dateButton.setText(temp);
        for(int i=0;i<gradeitem.length;i++){
        	if(gradeitem[i].equals(Connection.user.getGrade())){
        		gradeSpinner.setSelection(i,true);
        		break;
        	}
        }
        if(Connection.user.getSex().equals("")){
        	rdb3.setChecked(true);
        }else if(Connection.user.getSex().equals("��")){
        	rdb1.setChecked(true);
        }else{
        	rdb2.setChecked(true);
        }
    } 
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 * ������ڰ��������ʾ����ѡ���Dialog����ѡ������ʵ��
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
                c.get(Calendar.YEAR), // �������
                c.get(Calendar.MONTH), // �����·�
                c.get(Calendar.DAY_OF_MONTH) // ��������
            );
        return dialog;
    }
	//�[activity]ת�ƣ���Pane activity[������]����Pane.java
	private void shift(){
    	Intent intent = new Intent();   
        /* ָ��intentҪ�������� */
        intent.setClass(Information.this, Pane.class);
        /* �رյ�ǰ��Activity */
        Information.this.finish();
        /* ����һ���µ�Activity */
        Information.this.startService(intent);
        startActivity(intent);
      //  setContentView(R.layout.pane);
    }
}
