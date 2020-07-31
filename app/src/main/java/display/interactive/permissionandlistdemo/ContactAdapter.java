package display.interactive.permissionandlistdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;


/**
 * @ClassName ContactAdapter
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/23 20:29
 * @Version 1.0
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements TouchCallBack {

    private final String TAG = ContactAdapter.class.getName();

    private Context mContext;

    private List<Contact> mContactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        mContext = context;
        mContactList = contactList;
    }

    public void setData(List<Contact> list) {
        this.mContactList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Bundle data = new Bundle();
                Contact itemContact = mContactList.get(position);
                data.putSerializable("itemContact", itemContact);
                Intent intent = new Intent(mContext, ContactItemActivity.class);
                intent.putExtras(data);
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        Log.i("TAG", "onBindViewHolder: " + contact.getName() + ", " + contact.getPhone());
        holder.contact_name.setText(contact.getName());
        holder.contact_phone.setText(contact.getPhone());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // 交换位置
        Collections.swap(mContactList, fromPosition, toPosition);
        // 局部刷新（移动）
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDelete(int position) {
        // 删除数据
        Log.i(TAG, "onItemDelete: mContactList.size() is " + mContactList.size());
        Log.i(TAG, "onItemDelete: delete position is " + position);
        mContactList.remove(position);
        // 局部刷新（删除）
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contact_name, contact_phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_phone = itemView.findViewById(R.id.contact_phone);
        }
    }

}
