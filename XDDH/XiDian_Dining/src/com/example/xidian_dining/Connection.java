/*
 * ������Է���������ʱ��Ϣ��ͬʱ����������Է���������Ϣ������ʾ������������Ϣ����ͻ���ͬ��[waiting]
 */
package com.example.xidian_dining;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Connection {
	public static String Domain_IP;			//������ַ
	public static Socket client;				//�������ͨ�ŵ�����
	public static BufferedReader br;			//��������
	public static PrintWriter writer;			//д������
	public static User_Data user;				//��ǰ��¼���û�
	public static String number = "";			//��ʱȫ�ֱ���������û��˺�
	public static String passwd = "";			//��ʱȫ�ֱ���������û�����
	public static String name = "";				//��ʱȫ�ֱ���������û�����
	public static String sex = "";				//��ʱȫ�ֱ���������û�����
	public static String email = "";			//��ʱȫ�ֱ���������û�����
	public static String grade = "";			//��ʱȫ�ֱ���������û��꼶
	public static String phone = "";			//��ʱȫ�ֱ���������û��绰
	public static String birth = "";			//��ʱȫ�ֱ���������û�����
	public static String registertime = "";		//��ʱȫ�ֱ���������û�ע��ʱ��
	public static String result = "";			//��ʱȫ�ֱ���������û��˺�
	public static ArrayList<Remark_Data> remark = new ArrayList<Remark_Data>();		//��ʱȫ�ֱ�����������е�������Ϣ[remark����]
	public static ArrayList<Remark_Data> remark2 = new ArrayList<Remark_Data>();	//��ʱȫ�ֱ�������������ҵ���������Ϣ�����޺ͲȲ�����¼[��������]
	public static ArrayList<String> build = new ArrayList<String>();				//��ʱȫ�ֱ�������Ų�������			---������ŵ���ʱ��¼[��������]
	public static ArrayList<String> layer = new ArrayList<String>();				//��ʱȫ�ֱ�������Ŷ�Ӧ����¥��		---������ŵ���ʱ��¼[��������]
	public static ArrayList<String> window = new ArrayList<String>();				//��ʱȫ�ֱ�������Ŷ�Ӧ����¥��Ķ�Ӧ����	---������ŵ���ʱ��¼[��������]
	public static ArrayList<String> dish = new ArrayList<String>();					//��ʱȫ�ֱ�������Ŷ�Ӧ����¥�㴰�ڵĲ���	---������ŵ���ʱ��¼[��������]
	public static ArrayList<String> dish2 = new ArrayList<String>();				//��ʱȫ�ֱ�������Ŷ�Ӧ����¥�㴰�ڵĲ���	---ÿ���Ƽ��Ĵ����¶�Ӧ�Ĳ�����¼[�Ƽ�����]
	public static ArrayList<String> score2 = new ArrayList<String>();				//��ʱȫ�ֱ�������Ŷ�Ӧ����¥�㴰�ڵ�����	---ÿ���Ƽ��Ĵ����¶�Ӧ�����ּ�¼[�Ƽ�����]
	public static ArrayList<String> tuijianwindowname = new ArrayList<String>();	//��ʱȫ�ֱ�������Ŷ�Ӧ����¥�㴰��		---ÿ���Ƽ��Ĵ������֣�15����[�Ƽ�����]
	public static ArrayList<String> tuijianwindowid = new ArrayList<String>();		//��ʱȫ�ֱ�������Ŷ�Ӧ����¥�㴰��		---ÿ���Ƽ��Ĵ���ID��15����[�Ƽ�����]
	public static String window_score = "";											//�����������Ķ�Ӧ���ڷ���[��������]
	public static boolean waiting = true;		//���������ȵ�������������½�ɹ���񣬸��ݷ����������Ľ��������һ��������ת
	public static boolean waiting2 = true;		//���������ȵ������������޸ĸ�����Ϣ�ɹ���񣬸��ݷ����������Ľ��������һ��������ת
	public static boolean waiting3 = true;		//���������ȵ�����������ע��ɹ���񣬸��ݷ����������Ľ��������һ��������ת
	public static boolean waiting4 = true;		//������������Ҫ�������۽���ʱ��Ҫ���û����յ��ӷ�������������������[����������ÿ����ֻ�ܲ鿴50�����µ�����]
	public static boolean waiting5 = true;		//�������������������¿ͻ������б���Ҫ���������еĲ������ƣ����Է�����
	public static boolean waiting6 = true;		//�������������������¿ͻ������б���Ҫ��������ĳ������������¥�㣬���Է�����
	public static boolean waiting7 = true;		//�������������������¿ͻ������б���Ҫ��������ĳ������ĳ��¥������д��ڣ����Է�����
	public static boolean waiting8 = true;		//�������������������¿ͻ������б���Ҫ��������ĳ������ĳ��¥��ĳ�����ڵ����в��������Է�����
	public static boolean waiting9 = true;		//�������������û����²���巢������ʱ���������յ������û������۷��ؽ��[ʧ��/�ɹ�]���û�֪ͨ���û��ſ��Խ�����һ������	
	public static boolean waiting10 = true;		//���������û�����������������ж�Ӧ�������۴ӷ��������ս�����ſ�����ʾ���û�
	public static boolean waiting11 = true;		//���������û�������������ȶ�Ӧ�������в�������Ϣ�ӷ��������ս�����ſ�����ʾ���û�
	public static boolean waiting12 = true;		//�û������۰�����޺�ȷ������������޽���ſ��Խ�����һ������
	public static boolean waiting13 = true;		//�û������۰����Ⱥ�ȷ�����������Ƚ���ſ��Խ�����һ������
	public static boolean waiting14 = true;		//�û����Ƽ�������ĳһ�Ƽ����ں�ȷ����������иô��ڵĲ���-������Ϣ���ͽ�����ſ�����ʾ���û�
	public static boolean waiting15 = true;		//�û�һ��¼�����Ƽ����棬��ʾ���е��Ƽ�������Ϣ����Ȼ�����������Ƽ���Ϣ���͸��ͻ�֮��
	public static boolean waiting16 = true;		//����
	public static boolean link = false;			//��ʾ���������������״̬
	public static boolean pan = true; 			//�����̼߳������������Ϊ�ٱ�ʾ�����������ͨ�ţ��ر����߳�
	//public static boolean pan2 = true; 			//�����̼߳������������Ϊ�ٱ�ʾ�����������ͨ�ţ��ر����߳�
	public static Thread listenThread = null;	//�������Է���������Ϣ�������߳�
	//public static Thread heartBeaten = null;	//�����źţ���֤�������Socket���Ӳ���
	/*
	 * �����������Socket���ӣ����������߳�
	 */
	public static boolean connect(){
		try{
			pan = true;
			Domain_IP = InetAddress.getByName("roast.xiantaoli.cn").getHostAddress();		//������ַ
			client = new Socket(Domain_IP,9802);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			listenThread = new ListenThread();
			//heartBeaten = new HeartThread();
			link = true;
			return true;
		}catch(Exception e){
			link = false;
			System.out.println("Connection error1 -> connect failed!");
			e.printStackTrace();
		}
		return false;
	}
	/*
	 * �����̣߳��������������ͨ��Э��[client��server��]�����ݲ�ͬЭ����ղ�ͬ��Ϣ
	 */
	private static class ListenThread extends Thread{
		public ListenThread(){
			start();
		}
		public void run(){
			while(pan){
				try{
					String type = br.readLine();
					if(type.equals("login")){				//��¼Э��
						String s = br.readLine();
						if(s.equals("loginsucceed")){		//��¼���1 -> �ɹ�
							result = "OK";
							number = br.readLine();
							passwd = br.readLine();
							name = br.readLine();
							grade = br.readLine();
							birth = br.readLine();
							sex = br.readLine();
							email = br.readLine();
							phone = br.readLine();
							registertime = br.readLine();
							user = new User_Data(number,passwd,name,grade,birth,sex,email,phone,registertime);
						}else if(s.equals("passwderror")){	//��¼���2 -> �������
							result = "passwderror";
						}else if(s.equals("nouser")){		//��¼���3 -> �˺Ų�����
							result = "nouser";
						}else if(s.equals("alreadyonline")){//��¼���4 -> �˺��Ѿ�����
							result = "alreadyonline";
						}
						waiting = false;
					}else if(type.equals("modify")){		//�޸ĸ�����ϢЭ��
						if(br.readLine().equals("succeed")){
							result = "OK";
						}else{
							result = "failed";
						}
						waiting2 = false;
					}else if(type.equals("register")){		//ע�����˺�Э��
						String s = br.readLine();
						if(s.equals("succeed")){
							result = "OK";
						}else if(s.equals("alreadyused")){
							result = "alreadyused";
						}else{
							result = "failed";
						}
						waiting3 = false;
					}else if(type.equals("remark")){		//���۽���Э��
						remark.clear();
						String build = "";
						while(!(build = br.readLine()).equals("end")){
							String layer = br.readLine();
							String window = br.readLine();
							String dish = br.readLine();
							String user = br.readLine();
							String content = br.readLine();
							String score = br.readLine();
							String zan = br.readLine();
							String cai = br.readLine();
							String remarkid = br.readLine();
							String zanstate = br.readLine();
							String caistate = br.readLine();
							remark.add(new Remark_Data(remarkid,user,window,dish,build,layer,content,new Integer(score).intValue(),new Integer(zan).intValue(),new Integer(cai).intValue(),zanstate,caistate));
						}
						waiting4 = false;
					}else if(type.equals("build")){			//���ղ�������Э��
						build.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							build.add(first);
						}
						waiting5 = false;
					}else if(type.equals("layer")){			//����ĳ����������¥��Э��
						layer.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							layer.add(first);
						}
						waiting6 = false;
					}else if(type.equals("window")){		//����ĳ������ĳ��¥�����д���Э��
						window.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							window.add(first);
						}
						waiting7 = false;
					}else if(type.equals("recipe")){		//����ĳ������ĳ��¥��ĳ���������в���Э��
						dish.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							dish.add(first);
						}
						waiting8 = false;
					}else if(type.equals("tucao")){
						if(br.readLine().equals("succeed")){
							result = "OK";
						}else{
							result = "NO";
						}
						waiting9 = false;
					}else if(type.equals("dishremarksearch")){	//����ĳ������ĳ��¥��ĳ������ĳ���˵���������Э��
						remark2.clear();
						String build = "";
						while(!(build = br.readLine()).equals("end")){
							String layer = br.readLine();
							String window = br.readLine();
							String dish = br.readLine();
							String user = br.readLine();
							String content = br.readLine();
							String score = br.readLine();
							String zan = br.readLine();
							String cai = br.readLine();
							String remarkid = br.readLine();
							remark2.add(new Remark_Data(remarkid,user,window,dish,build,layer,content,new Integer(score).intValue(),new Integer(zan).intValue(),new Integer(cai).intValue(),"",""));
						}
						if(remark2.size()==0){
							result = "NO";
						}else{
							result = "OK";
						}
						waiting10 = false;
					}else if(type.equals("windowscoresearch")){	//����ĳ������ĳ��¥��ĳ���������д�����ϢЭ��
						window_score = br.readLine();
						waiting11 = false;
					}else if(type.equals("zan")){
						result = br.readLine();
						waiting12 = false;
					}else if(type.equals("cai")){
						result = br.readLine();
						waiting13 = false;
					}else if(type.equals("windowinformation")){	//�����Ƽ��������д�����Ϣ��Э��
						dish2.clear();
						score2.clear();
						String first;
						while(!(first = br.readLine()).equals("end")){
							dish2.add(first);
							score2.add(br.readLine());
						}
						waiting14 = false;
					}else if(type.equals("tuijian")){			//����ÿ���Ƽ��������Ƽ�[����-����]��Ϣ
						tuijianwindowname.clear();
						tuijianwindowid.clear();
						String first;
						while(!((first = br.readLine()).equals("end"))){
							String s1 = first+" "+br.readLine()+"�� "+" "+br.readLine()+"\n����:";
							String sc = br.readLine();
							if(sc.equals("0.0")){
								sc = "��";
							}else{
								sc = (String)(sc.subSequence(0, 3));
							}
							tuijianwindowname.add(s1+sc);
							tuijianwindowid.add(br.readLine());
						}
						waiting15 = false;
					}
				}catch(Exception e){
					System.out.println("Connection error2 -> communication failed!");
					e.printStackTrace();
				}
			}
		}
	}
	/*
	 * �����̣߳�����Socket���ӣ���֤�������Զ���������
	 
	private static class HeartThread extends Thread{
		public HeartThread(){
			start();
		}
		public void run(){
			while(pan2){
				try{
					sleep(1000);
				}catch (Exception e) {
					System.out.println("HeartThread sleep failed");
					e.printStackTrace();
				}
				if(!(client.isConnected())){
					try {
						pan = false;
						listenThread.interrupt();
						sleep(1000);
						Domain_IP = InetAddress.getByName("roast.xiantaoli.cn").getHostAddress();
						client = new Socket(Domain_IP,9802);
						br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
						pan = true;
						listenThread = new ListenThread();
						link = true;
					} catch (Exception e) {
						System.out.println("HeartThread retry failed");
						e.printStackTrace();
					}
				}
			}
		}
	}
	*/
}
