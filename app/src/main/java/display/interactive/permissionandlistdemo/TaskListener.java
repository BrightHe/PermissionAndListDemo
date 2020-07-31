package display.interactive.permissionandlistdemo;

import java.util.List;

/**
 * @ClassName TaskListener
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/24 15:43
 * @Version 1.0
 */
public interface TaskListener {
    void onSuccess(List<Contact> contactList);

    void onFailed();
}
