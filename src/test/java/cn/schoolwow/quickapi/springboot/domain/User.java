package cn.schoolwow.quickapi.springboot.domain;

public class User {
    /**用户名*/
    private String username;

    /**密码*/
    private String password;

    /**年龄*/
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}