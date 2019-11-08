package com.oppo.commhelper.custompage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.Utils;
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
    private static final String TAG = "CustomPageParent";

    HttpInterface mHttpInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Utils.init(getApplication());

        // {1.0 460mcc2mnc [zh_CN] ldltr sw392dp w392dp h776dp 440dpi nrml long hdr widecg port finger -keyb/v/h -nav/h winConfig={ mBounds=Rect(0, 0 - 1080, 2340) mAppBounds=Rect(0, 75 - 1080, 2210) mWindowingMode=fullscreen mDisplayWindowingMode=fullscreen mActivityType=standard mAlwaysOnTop=undefined mRotation=ROTATION_0} s.1 themeChanged=0 themeChangedFlags=0}
        //{1.0 460mcc2mnc [zh_CN] ldltr sw374dp w374dp h739dp 461dpi nrml long hdr widecg port finger -keyb/v/h -nav/h winConfig={ mBounds=Rect(0, 0 - 1080, 2340) mAppBounds=Rect(0, 75 - 1080, 2210) mWindowingMode=fullscreen mDisplayWindowingMode=fullscreen mActivityType=standard mAlwaysOnTop=undefined mRotation=ROTATION_0} s.1 themeChanged=0 themeChangedFlags=0}
        Configuration configuration = getResources().getConfiguration();
        Log.e(TAG,">>>>>" +configuration.toString());

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
