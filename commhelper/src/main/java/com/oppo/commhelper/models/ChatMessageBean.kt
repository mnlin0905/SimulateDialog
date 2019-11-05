package com.oppo.commhelper.models

/**
 * function : 聊天记录消息
 *
 * [chat_status] 消息类型 [Const.KEY_TYPE_CHAT_MESSAGE]
 *
 * Created on 2018/12/26  11:31
 * @author mnlin
 */
data class ChatMessageBean(
        var chat_status: Int = 0,
        var chat_message: String? = null,
        var chat_time: String? = null
)