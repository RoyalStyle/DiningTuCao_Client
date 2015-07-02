/*
 * 存放来自服务器的临时消息，同时负责监听来自服务器的消息，供显示界面调用相关消息，与客户端同步[waiting]
 */
package com.example.xidian_dining;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Connection {
	public static String Domain_IP;			//域名地址
	public static Socket client;				//与服务器通信的连接
	public static BufferedReader br;			//读服务器
	public static PrintWriter writer;			//写服务器
	public static User_Data user;				//当前登录的用户
	public static String number = "";			//临时全局变量，存放用户账号
	public static String passwd = "";			//临时全局变量，存放用户密码
	public static String name = "";				//临时全局变量，存放用户姓名
	public static String sex = "";				//临时全局变量，存放用户名字
	public static String email = "";			//临时全局变量，存放用户邮箱
	public static String grade = "";			//临时全局变量，存放用户年级
	public static String phone = "";			//临时全局变量，存放用户电话
	public static String birth = "";			//临时全局变量，存放用户生日
	public static String registertime = "";		//临时全局变量，存放用户注册时间
	public static String result = "";			//临时全局变量，存放用户账号
	public static ArrayList<Remark_Data> remark = new ArrayList<Remark_Data>();		//临时全局变量，存放所有的评论信息[remark版面]
	public static ArrayList<Remark_Data> remark2 = new ArrayList<Remark_Data>();	//临时全局变量，存放搜索找到的评论信息，对赞和踩不做记录[按菜搜索]
	public static ArrayList<String> build = new ArrayList<String>();				//临时全局变量，存放餐厅名字			---搜索存放的临时记录[搜索版面]
	public static ArrayList<String> layer = new ArrayList<String>();				//临时全局变量，存放对应餐厅楼层		---搜索存放的临时记录[搜索版面]
	public static ArrayList<String> window = new ArrayList<String>();				//临时全局变量，存放对应餐厅楼层的对应窗口	---搜索存放的临时记录[搜索版面]
	public static ArrayList<String> dish = new ArrayList<String>();					//临时全局变量，存放对应餐厅楼层窗口的菜名	---搜索存放的临时记录[搜索版面]
	public static ArrayList<String> dish2 = new ArrayList<String>();				//临时全局变量，存放对应餐厅楼层窗口的菜名	---每周推荐的窗口下对应的菜名记录[推荐版面]
	public static ArrayList<String> score2 = new ArrayList<String>();				//临时全局变量，存放对应餐厅楼层窗口的评分	---每周推荐的窗口下对应的评分记录[推荐版面]
	public static ArrayList<String> tuijianwindowname = new ArrayList<String>();	//临时全局变量，存放对应餐厅楼层窗口		---每周推荐的窗口名字（15个）[推荐版面]
	public static ArrayList<String> tuijianwindowid = new ArrayList<String>();		//临时全局变量，存放对应餐厅楼层窗口		---每周推荐的窗口ID（15个）[推荐版面]
	public static String window_score = "";											//按窗口搜索的对应窗口分数[搜索版面]
	public static boolean waiting = true;		//锁变量，等到服务器发来登陆成功与否，根据服务器发来的结果决定下一个界面跳转
	public static boolean waiting2 = true;		//锁变量，等到服务器发来修改个人信息成功与否，根据服务器发来的结果决定下一个界面跳转
	public static boolean waiting3 = true;		//锁变量，等到服务器发来注册成功与否，根据服务器发来的结果决定下一个界面跳转
	public static boolean waiting4 = true;		//锁变量，当需要加载评论界面时需要等用户接收到从服务器发来的所有评论[服务器限制每个人只能查看50条最新的评论]
	public static boolean waiting5 = true;		//锁变量，在搜索界面下客户端拉列表需要加载西电有的餐厅名称，来自服务器
	public static boolean waiting6 = true;		//锁变量，在搜索界面下客户端拉列表需要加载西电某个餐厅的所有楼层，来自服务器
	public static boolean waiting7 = true;		//锁变量，在搜索界面下客户端拉列表需要加载西电某个餐厅某个楼层的所有窗口，来自服务器
	public static boolean waiting8 = true;		//锁变量，在搜索界面下客户端拉列表需要加载西电某个餐厅某个楼层某个窗口的所有菜名，来自服务器
	public static boolean waiting9 = true;		//锁变量，当有用户在吐槽面板发表评论时，服务器收到来自用户的评论返回结果[失败/成功]给用户通知，用户才可以进行下一步操作	
	public static boolean waiting10 = true;		//锁变量，用户按菜名搜索后等所有对应菜名评论从服务器接收结束后才可以显示给用户
	public static boolean waiting11 = true;		//锁变量，用户按窗口搜索后等对应窗口所有菜名等信息从服务器接收结束后才可以显示给用户
	public static boolean waiting12 = true;		//用户在评论版面点赞后等服务器发来点赞结果才可以进行下一步操作
	public static boolean waiting13 = true;		//用户在评论版面点踩后等服务器发来点踩结果才可以进行下一步操作
	public static boolean waiting14 = true;		//用户在推荐版面点击某一推荐窗口后等服务器把所有该窗口的菜名-评分信息发送结束后才可以显示给用户
	public static boolean waiting15 = true;		//用户一登录进入推荐版面，显示所有的推荐窗口信息，当然必须在所有推荐信息发送给客户之后
	public static boolean waiting16 = true;		//保留
	public static boolean link = false;			//表示与服务器网络连接状态
	public static boolean pan = true; 			//接收线程监听条件，如果为假表示与服务器不在通信，关闭了线程
	//public static boolean pan2 = true; 			//心跳线程监听条件，如果为假表示与服务器不在通信，关闭了线程
	public static Thread listenThread = null;	//监听来自服务器的消息，监听线程
	//public static Thread heartBeaten = null;	//心跳信号，保证与服务器Socket连接不断
	/*
	 * 与服务器建立Socket连接，建立监听线程
	 */
	public static boolean connect(){
		try{
			pan = true;
			Domain_IP = InetAddress.getByName("roast.xiantaoli.cn").getHostAddress();		//域名地址
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
	 * 监听线程，含有与服务器的通信协议[client收server发]，根据不同协议接收不同消息
	 */
	private static class ListenThread extends Thread{
		public ListenThread(){
			start();
		}
		public void run(){
			while(pan){
				try{
					String type = br.readLine();
					if(type.equals("login")){				//登录协议
						String s = br.readLine();
						if(s.equals("loginsucceed")){		//登录结果1 -> 成功
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
						}else if(s.equals("passwderror")){	//登录结果2 -> 密码错误
							result = "passwderror";
						}else if(s.equals("nouser")){		//登录结果3 -> 账号不存在
							result = "nouser";
						}else if(s.equals("alreadyonline")){//登录结果4 -> 账号已经在线
							result = "alreadyonline";
						}
						waiting = false;
					}else if(type.equals("modify")){		//修改个人信息协议
						if(br.readLine().equals("succeed")){
							result = "OK";
						}else{
							result = "failed";
						}
						waiting2 = false;
					}else if(type.equals("register")){		//注册新账号协议
						String s = br.readLine();
						if(s.equals("succeed")){
							result = "OK";
						}else if(s.equals("alreadyused")){
							result = "alreadyused";
						}else{
							result = "failed";
						}
						waiting3 = false;
					}else if(type.equals("remark")){		//评论接收协议
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
					}else if(type.equals("build")){			//接收餐厅名称协议
						build.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							build.add(first);
						}
						waiting5 = false;
					}else if(type.equals("layer")){			//接收某个餐厅所有楼层协议
						layer.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							layer.add(first);
						}
						waiting6 = false;
					}else if(type.equals("window")){		//接收某个餐厅某个楼层所有窗口协议
						window.clear();
						String first = "";
						while(!((first = br.readLine()).equals("end"))){
							window.add(first);
						}
						waiting7 = false;
					}else if(type.equals("recipe")){		//接收某个餐厅某个楼层某个窗口所有菜名协议
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
					}else if(type.equals("dishremarksearch")){	//接收某个餐厅某个楼层某个窗口某道菜的所有评论协议
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
					}else if(type.equals("windowscoresearch")){	//接收某个餐厅某个楼层某个窗口所有窗口信息协议
						window_score = br.readLine();
						waiting11 = false;
					}else if(type.equals("zan")){
						result = br.readLine();
						waiting12 = false;
					}else if(type.equals("cai")){
						result = br.readLine();
						waiting13 = false;
					}else if(type.equals("windowinformation")){	//接收推荐窗口所有窗口信息的协议
						dish2.clear();
						score2.clear();
						String first;
						while(!(first = br.readLine()).equals("end")){
							dish2.add(first);
							score2.add(br.readLine());
						}
						waiting14 = false;
					}else if(type.equals("tuijian")){			//接收每周推荐的所有推荐[窗口-评分]信息
						tuijianwindowname.clear();
						tuijianwindowid.clear();
						String first;
						while(!((first = br.readLine()).equals("end"))){
							String s1 = first+" "+br.readLine()+"层 "+" "+br.readLine()+"\n评分:";
							String sc = br.readLine();
							if(sc.equals("0.0")){
								sc = "无";
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
	 * 心跳线程，监视Socket连接，保证都掉后自动重新连接
	 
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
