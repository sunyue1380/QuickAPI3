package cn.schoolwow.quickapi.springboot.domain;

public class UserExtend extends User {
    /**用户住址*/
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
