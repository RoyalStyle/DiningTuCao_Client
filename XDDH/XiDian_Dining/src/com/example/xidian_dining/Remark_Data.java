package com.example.xidian_dining;
/*
 * ���۵����ݽṹ�������ݿ��Ӧ
 */
public class Remark_Data {
	private String timeid;				//���۷���ʱ����Ϊ���۵�ID
    private String username;			//�������۵��û��ǳ�
    private String windowname;			//������Դ���
    private String dishname;			//���������
    private String buildname;			//������Բ���
    private String layer;				//������Բ���¥��
    private String content;				//��������
    private int dishscore;				//���۷���
    private int zancount;				//�����޵�����
    private int caicount;				//���۲ȵ�����
    private String zanstate;
    private String caistate;
    
    public Remark_Data(String timeid,String username,String windowname,String dishname,String buildname,String layer,String content,int dishscore,int zancount,int caicount,String zanstate,String caistate){
    	this.timeid = timeid;
    	this.username = username;
    	this.windowname = windowname;
    	this.dishname = dishname;
    	this.buildname = buildname;
    	this.layer = layer;
    	this.content = content;
    	this.dishscore = dishscore;
    	this.zancount = zancount;
    	this.caicount = caicount;
    	this.zanstate = zanstate;
    	this.caistate = caistate;
    }
    public String getTimeId(){
        return this.timeid;
    }
    public String getUserName(){
        return this.username;
    }
    public String getWindowName(){
        return this.windowname;
    }
    public String getDishName(){
        return this.dishname;
    }
    public String getBuildName(){
        return this.buildname;
    }
    public String getLayer(){
        return this.layer;
    }
    public int getDishScore(){
        return this.dishscore;
    }
    public int getZanCount(){
        return this.zancount;
    }
    public int getCaiCount(){
        return this.caicount;
    }
    public String getContent(){
        return this.content;
    }
    public String getZanState(){
        return this.zanstate;
    }
    public String getCaiState(){
        return this.caistate;
    }
    
    public void setUserName(String id){
        this.username = id;  
    }
    public  void setTimeId(String time){
        this.timeid = time;
    }
    public void setWindowName(String window){
        this.windowname = window;
    }
    public void setDishName(String dish){
        this.dishname = dish;
    }
    public void setBuildName(String build){
        this.buildname = build;
    }
    public void setLayer(String layer){
        this.layer = layer;
    }
    public void setDishScore(int score){
        this.dishscore = score;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setZanCount(int count){
        this.zancount = count;
    }
    public void setCaiCount(int count){
        this.caicount = count;
    }
    public void setZanState(String zanstate){
        this.zanstate = zanstate;
    }
    public void setCaiState(String caistate){
        this.caistate = caistate;
    }
}
