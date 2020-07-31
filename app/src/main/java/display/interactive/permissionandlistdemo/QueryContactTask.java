package display.interactive.permissionandlistdemo;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QueryContactTask
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/24 13:53
 * @Version 1.0
 */
public class QueryContactTask extends AsyncTask<Void, Integer, Integer> {
    private static final int TYPE_SUCCESS = 1;
    private static final int TYPE_FAILED = 0;

    private Context mContext;
    private List<Contact> mContactList = new ArrayList<>();

    private TaskListener taskListener;

    public QueryContactTask(Context mContext,TaskListener taskListener) {
        this.mContext = mContext;
        this.taskListener = taskListener;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Cursor cursor = null;
        try{
            cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("TAG", "doInBackground: name is "+name+", phone is "+phone);
                    Contact contact = new Contact(name,phone);
                    mContactList.add(contact);
                }
            } else {
                return TYPE_FAILED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return TYPE_SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                Log.i("TAG", "onPostExecute: success");
                taskListener.onSuccess(mContactList);
                break;
            case TYPE_FAILED:
                Log.i("TAG", "onPostExecute: failed");
                taskListener.onFailed();
                break;
            default:
                break;
        }
    }
}