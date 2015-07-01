package com.example.xidian_dining;
/*
 * 菜的数据结构，与数据库对应
 */
public class Recipe_Data {
    private String recipeid;			//菜ID
    private String windowid;			//菜所在窗口ID
    private String dishname;			//菜名
    private double averagedishscore;	//关于菜的所有评论的评分的均值
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
