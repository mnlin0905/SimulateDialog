package com.knowledge.mnlin.sdialog.base;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.knowledge.mnlin.sdialog.R;
import com.knowledge.mnlin.sdialog.interfaces.ProvideIncludeDialogInterface;
import com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup;

/**
 * Created on 2019/4/3  15:48
 * function : 基类,为插件提供 IncludeDialogViewGroup 引用;重写返回键逻辑,达到模拟弹出框的效果
 *
 * @author mnlin
 */
@SuppressLint("Registered")
public class SDActivity extends AppCompatActivity implements ProvideIncludeDialogInterface {
    /**
     * dialog-content view,容纳dialog布局
     * <p>
     * 该布局用于基本的 dialog 显示/隐藏等操作,可能为null
     */
    private IncludeDialogViewGroup dialogContentView;

    @Override
    public void onBackPressed() {
        //判断如果content中包含有IncludeDialogViewGroup
        if (getIncludeDialog() != null && getIncludeDialog().existDialogOpened()) {
            //如果当前 IncludeDialogViewGroup 有 子布局处于展开状态,则先将子布局的展开状态关闭掉
            getIncludeDialog().closeDialogsOpened(null, null, true, true);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @return IncludeDialogViewGroup对象
     */
    @Nullable
    @Override
    public IncludeDialogViewGroup getIncludeDialog() {
        //防止根部局为动态添加进入,防止布局加载完成前直接引用 dialogContentView
        if(dialogContentView == null){
            //获取容纳dialog容器
            dialogContentView = ((IncludeDialogViewGroup) findViewById(android.R.id.content).getTag(R.id.id_include_dialog_view_group));
        }
        return dialogContentView;
    }
}
