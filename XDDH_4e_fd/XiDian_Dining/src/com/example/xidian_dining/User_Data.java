package com.example.xidian_dining;
/*
 * 用户的数据结构，与数据库对应
 */
public class User_Data {
    private String id;				//账号
    private String name;			//昵称
    private String passwd;			//密码
    private String grade;			//年级
    private String birth;			//生日
    private String sex;				//性别
    private String email;			//邮箱
    private String phone;			//电话
    private String registertime ;	//注册时间
    
    public User_Data(String id,String passwd,String name,String grade,String birth,String sex,String email,String phone,String registertime){
    	this.id = id;
    	this.name = name;
    	this.passwd = passwd;
    	this.grade = grade;
    	this.sex = sex;
    	this.birth = birth;
    	this.email = email;
    	this.phone = phone;
    	this.registertime = registertime;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPasswd(String passwd){
        this.passwd = passwd;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public void setGrade(String grade){
        this.grade = grade;
    }
    public void setBirth(String birth){
        this.birth = birth;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setRegisterTime(String time){
        this.registertime = time;
    }   
    
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getPasswd(){
        return this.passwd;
    }
    public String getSex(){
        return this.sex;
    }
    public String getGrade(){
        return this.grade;
    }
    public String getBirth(){
        return this.birth;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getRegisterTime(){
        return this.registertime;
    }
}
