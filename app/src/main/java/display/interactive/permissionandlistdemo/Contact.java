package display.interactive.permissionandlistdemo;

import java.io.Serializable;

/**
 * @ClassName Contact
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/23 20:38
 * @Version 1.0
 */
public class Contact implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String lookupKey;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Contact(int id, String name, String phone, String lookupKey) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.lookupKey = lookupKey;
    }

    public String getLookUpKey() {
        return lookupKey;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLookUpKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }
}
