package com.knowledge.mnlin.page;

import android.app.Application;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created on 2019/4/25  9:38
 * function : page - app
 *
 * @author mnlin
 */
public class PageApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
        Utils.init(this);
    }

    /**
     * 日志框架
     */
    private void initLogger() {
        LogStrategy strategy = new LogcatLogStrategy();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodOffset(2)
                .methodCount(0)// (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(strategy) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(getString(R.string.app_name))   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return Logger.VERBOSE >= Logger.VERBOSE;
            }

            @Override
            public void log(int priority, String tag, String message) {
                Log.e("PageApp=== ", message);
                // super.log(priority, tag, message);
            }
        });
    }
}
