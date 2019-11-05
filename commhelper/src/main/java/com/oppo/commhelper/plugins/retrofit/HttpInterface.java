package com.oppo.commhelper.plugins.retrofit;

import com.oppo.commhelper.models.ResponseBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created on 2019/11/4  20:08
 * function :
 *
 * @author mnlin0905@gmail.com
 */
public interface HttpInterface {
    /**
     * 腾讯请求接口参数
     * <p>
     * https://tbp.tencentcloudapi.com/?Action=VadAudioProcess
     * &SecretId=xxx
     * &Nonce=12722
     * &Timestamp=1559530999&Region=ap-beijing
     * &Version=2019-03-11
     * &BotId=bf3ea64b-4e27-4277-aa1a-b31bec19229a
     * &VoiceId=i2he
     * &Seq=0
     * &AudioData=base64(audioData)
     * &UserId=userid
     * &TotalAudioLength=8765
     * &LastTalkingLength=0
     * &<公共请求参数>
     * <p>
     * <p>
     * {
     * "Response": {
     * "DialogStatus": "CONTINUE",
     * "BotName": "电话⾃自动答复",
     * "IntentName": "回答啥事2",
     * "ResponseText": "刚说的我已经记下来了了，请问您还有什什么要补充的么",
     * "ResponseId": "",
     * "UserText": "",
     * "RequestId": "6f3ec71e-c8e6-4f1a-bc12-72f8a1ff26cd"
     * }
     * }
     */
    @GET("?Action=VadAudioProcess")
    Observable<ResponseBean> getCommonAgreementList(@QueryMap Map<String,String> valueMap);
}
