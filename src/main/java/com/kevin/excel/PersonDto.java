package com.kevin.excel;

import java.io.Serializable;

/**
 * @ClassName DemoDataBean
 * @Description TODO
 * @Author kevin.yang
 * @Date 2021/4/29 16:44
 */
public class PersonDto implements Serializable {
    private static final long serialVersionUID = -2265643122536552073L;

    private int id;
    private String name;
    private String address;
    private String mobile;
    private String remark;

    public PersonDto() {
    }

    public PersonDto(int id, String name, String address, String mobile, String remark) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
