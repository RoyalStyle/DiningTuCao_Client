package com.example.xidian_dining;
/*
 * �˵����ݽṹ�������ݿ��Ӧ
 */
public class Recipe_Data {
    private String recipeid;			//��ID
    private String windowid;			//�����ڴ���ID
    private String dishname;			//����
    private double averagedishscore;	//���ڲ˵��������۵����ֵľ�ֵ
    public Recipe_Data(String recipeid,String windowid,String dishname,double averagedishscore){
    	this.recipeid = recipeid;
    	this.windowid = windowid;
    	this.dishname = dishname;
    	this.averagedishscore = averagedishscore;
    }
    public void setRecipeId(String id){
        this.recipeid = id;
    }
    public void setWindowId(String name){
        this.windowid = name;
    }
    public void setDishname(String name){
        this.dishname = name;
    }
    public void setAverageDishScore(double score){
        this.averagedishscore = score;
    }
    public String getRecipeId(){
        return this.recipeid;
    }
    public String getWindowId(){
        return this.windowid;
    }
    public String getDishName(){
        return this.dishname;
    }
    
    public double getAverageDishScore(){
        return this.averagedishscore;
    }
}
