package com.example.xidian_dining;
/*
 * ���ڵ����ݽṹ�������ݿ��Ӧ
 */
public class Window_Data {
	private String windowid ;				//����ID
    private String windowname;				//��������
    private String layer;					//��������¥��
    private String buildname;				//�������ڲ�������
    private double windowscore;				//��������
    public Window_Data(String id,String name,String layer,String build,double score){
    	this.windowid = id;
    	this.windowname = name;
    	this.layer = layer;
    	this.buildname = build;
    	this.windowscore = score;
    }
    public void setWindowId(String windowid){
        this.windowid = windowid;
    }
    public void setWindowName(String name){
        this.windowname = name;
    }
    public void setLayer(String layer){
        this.layer = layer;
    }
    public void setBuildName(String buildname){
        this.buildname = buildname;
    }
    public void setWindowScore(double windowscore){
        this.windowscore = windowscore;
    }
    public String getWindowId(){
        return this.windowid;
    }
    public String getWindowName(){
        return this.windowname;
    }
    public String getLayer(){
        return this.layer;
    }
    public String getBuildName(){
        return this.buildname;
    }
    public double getWindowScore(){
        return this.windowscore;
    }

}
