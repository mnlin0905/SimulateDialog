package com.oppo.commhelper.custompage;

import com.knowledge.mnlin.page.core.PageImpl;

/**
 * Created on 2019/11/4  20:21
 * function :
 *
 * @author mnlin0905@gmail.com
 */
public abstract class CustomPageImpl extends PageImpl {
    protected void requeset(String msg) {
        // 基本常量的定义
        String secretId = "AKID0pSs30x17xb2IJ9wVq26yiJhupaChjfO";
        String secretKey = "gCVcV9YhctvkKflcEJOFrELn10nLeicH";
        String region = "ap-guangzhou";

        // 请求参数，用户根据实际自己填充
        String botId = "32b2951b-d956-4075-8114-4a5be5468ecb";
        String voiceId = "123456789";
        String userId = "liqiansuntest";
        String voidUli = "test.pcm";
        int totalAudioLength = 0;
        int lastTalkingLength = 0;
        int seq = 0;
    }
}
