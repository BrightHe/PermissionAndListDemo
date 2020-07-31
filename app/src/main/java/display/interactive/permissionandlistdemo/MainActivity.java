package display.interactive.permissionandlistdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private RecyclerView recyclerView;

    private FloatingActionButton fabAdd;

    private List<Contact> mContactList = new ArrayList<>();

    private ContactAdapter mContactAdapter;

    private QueryContactTask queryContactTask;

    private ProgressDialog progressDialog;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Constant.RECEIVER_DATA_CONTACT:
                    mContactAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private TaskListener listener = new TaskListener() {
        @Override
        public void onSuccess(List<Contact> ContactList) {
            mContactList = ContactList;

            progressDialog.dismiss();
            mContactAdapter.setData(MainActivity.this.mContactList);
            mContactAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailed() {
            Toast.makeText(MainActivity.this, getText(R.string.string_empty_contact), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //渲染UI
                    init();
                } else {
                    Toast.makeText(this, this.getText(R.string.string_toast_info), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, Constant.PERMISSION_CODE);
        } else {
            //渲染UI
            init();
        }
    }

    public void init() {
        initContact();

        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactDialog();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // adapter recyclerView
        mContactAdapter = new ContactAdapter(this, mContactList);
        recyclerView.setAdapter(mContactAdapter);

        //拖拽移动和左滑删除
        ContactItemTouchCallBack contactItemTouchCallBack = new ContactItemTouchCallBack(mContactAdapter);
        //设置支持侧滑删除子项
        contactItemTouchCallBack.setSwipeEnable(true);
        ItemTouchHelper helper = new ItemTouchHelper(contactItemTouchCallBack);
        helper.attachToRecyclerView(recyclerView);
    }

    public void showDialogLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void initContact() {
        //todo 动态获取手机的里面的通讯录，Loading
        showDialogLoading();
        // queryContactTask = new QueryContactTask(this, listener);
        // queryContactTask.execute();
        new Thread(mGetContactRunnable).start();
    }

    Runnable mGetContactRunnable = new Runnable() {
        @Override
        public void run() {
            if (getContactData()) {
                mHandler.sendEmptyMessage(Constant.RECEIVER_DATA_CONTACT);
            }
        }
    };

    public boolean getContactData() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                    int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact contact = new Contact(id, name, phone, lookupKey);
                    mContactList.add(contact);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            progressDialog.dismiss();
        }
    }

    public void addContactDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.add_contact_dialog, null);
        final EditText editContactName = view.findViewById(R.id.name_input);
        final EditText editContactPhone = view.findViewById(R.id.phone_input);
        AlertDialog.Builder add_contact_dialog = new AlertDialog.Builder(this);
        add_contact_dialog.setTitle(getText(R.string.string_add_contact));
        add_contact_dialog.setIcon(android.R.drawable.ic_dialog_info);
        add_contact_dialog.setView(view);
        add_contact_dialog.setPositiveButton(getText(R.string.string_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

                String editName = editContactName.getText().toString();

                String editPhone = editContactPhone.getText().toString();

                if (!TextUtils.isEmpty(editName) && !TextUtils.isEmpty(editPhone)) {
                    Contact contact = new Contact(editName, editPhone);
                    Log.i("TAG", "addContactDialog: onClick: add_name is " + contact.getName() + " add_phone is " + contact.getPhone());
                    mContactList.add(contact);
                    mContactAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, getText(R.string.string_toast_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
        add_contact_dialog.setNegativeButton(getText(R.string.string_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(MainActivity.this, getText(R.string.string_add_contact_cancel), Toast.LENGTH_SHORT).show();
            }
        });
        add_contact_dialog.show();
    }

}