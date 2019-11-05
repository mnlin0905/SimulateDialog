package com.oppo.commhelper.models;

/**
 * Created on 2019/11/4  20:33
 * function : response for tecent
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
 *
 * @author mnlin0905@gmail.com
 */
public class ResponseBean {
    private Response response;

    public static final class Response {
        private String DialogStatus;

        private String BotName;

        private String IntentName;

        private String ResponseText;

        private String ResponseId;

        private String UserText;

        private String RequestId;
    }
}
