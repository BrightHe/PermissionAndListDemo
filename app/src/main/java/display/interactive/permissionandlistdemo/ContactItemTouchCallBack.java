package display.interactive.permissionandlistdemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName ContactItemTouchCallBack
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/24 11:35
 * @Version 1.0
 */
public class ContactItemTouchCallBack extends ItemTouchHelper.Callback {

    private TouchCallBack mTouchCallBack;
    // 左滑删除
    private boolean mSwipeEnable = true;

    public ContactItemTouchCallBack(TouchCallBack mTouchCallBack) {
        this.mTouchCallBack = mTouchCallBack;
    }

    /**
     * 返回可以滑动的方向,一般使用makeMovementFlags(int,int)
     * 或makeFlag(int, int)来构造我们的返回值
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 允许上下拖拽
        int drag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 允许向左滑动
        int swipe = ItemTouchHelper.LEFT;
        // 设置
        return makeMovementFlags(drag, swipe);
    }

    /**
     * 上下拖动item时回调,可以调用Adapter的notifyItemMoved方法来交换两个ViewHolder的位置，
     * 最后返回true，
     * 表示被拖动的ViewHolder已经移动到了目的位置
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
       //通知适配器，两个子项位置发生改变
        mTouchCallBack.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 当用户左右滑动item时达到删除条件就会调用,一般为一半,条目继续滑动删除,否则弹回
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mTouchCallBack.onItemDelete(viewHolder.getAdapterPosition());
    }

    /**
     * 支持长按拖动，默认为true
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 支持滑动，即可以调用onSwipe()方法，默认为true
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 设置是否支持左滑
     *
     * @param enable
     */
    public void setSwipeEnable(boolean enable) {
        this.mSwipeEnable = enable;
    }
}
