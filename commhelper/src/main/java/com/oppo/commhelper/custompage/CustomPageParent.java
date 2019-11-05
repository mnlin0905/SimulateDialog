package com.oppo.commhelper.custompage;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.knowledge.mnlin.page.core.PageParent;
import com.oppo.commhelper.models.ResponseBean;
import com.oppo.commhelper.plugins.retrofit.HttpInterface;
import com.oppo.commhelper.plugins.retrofit.RetrofitConfigs;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/11/4  20:03
 * function :
 *
 * @author mnlin0905@gmail.com
 */
public class CustomPageParent extends PageParent {
    HttpInterface mHttpInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注入interface-retrofit
        mHttpInterface = new RetrofitConfigs().generateHttpInterface();

        Observable<ResponseBean> commonAgreementList = mHttpInterface.getCommonAgreementList(new HashMap());
        Disposable subscribe = commonAgreementList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBean>() {
                    @Override
                    public void accept(ResponseBean responseBean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable responseBean) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }
}
