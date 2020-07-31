package display.interactive.permissionandlistdemo;

/**
 * @ClassName TouchCallBack
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/24 11:03
 * @Version 1.0
 */
public interface TouchCallBack {

    /**
     * 数据交换
     * @param fromPosition 起始位置
     * @param toPosition 目标位置
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 数据删除
     * @param position 删除的子项位置
     */
    void onItemDelete(int position);
}
