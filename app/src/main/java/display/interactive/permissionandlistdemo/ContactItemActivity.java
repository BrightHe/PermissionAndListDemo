package display.interactive.permissionandlistdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactItemActivity extends AppCompatActivity {
    private TextView tvNameItem, phone_item;
    private Button btnUpdate;
    private ImageView imgCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_item);

        Intent intent = getIntent();
        final Contact contact = (Contact) intent.getSerializableExtra("itemContact");

        tvNameItem = findViewById(R.id.name_item);
        phone_item = findViewById(R.id.phone_item);
        tvNameItem.setText(contact.getName());
        phone_item.setText(contact.getPhone());
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到系统界面修改联系人信息
                Intent intent = new Intent(Intent.ACTION_EDIT);
                Uri data = ContactsContract.Contacts.getLookupUri(contact.getId(), contact.getLookUpKey());
                intent.setDataAndType(data, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                intent.putExtra("finishActivityOnSaveCompleted", true);
                startActivity(intent);
            }
        });
        imgCall = findViewById(R.id.img_call);
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打电话
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                startActivity(callIntent);
            }
        });
    }
}