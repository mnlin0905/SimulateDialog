package com.oppo.commhelper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oppo.commhelper.R
import com.oppo.commhelper.models.ChatMessageBean
import com.oppo.commhelper.utils.BaseRecyclerViewHolder
import kotlinx.android.synthetic.main.item_order_chat_system.view.*

/**
 * 聊天消息类型
 *
 * 系统消息
 * 己方消息
 * 对方消息
 */
const val KEY_TYPE_CHAT_MESSAGE = "key_type_chat_message"
const val TYPE_CHAT_MESSAGE_SYSTEM = 0
const val TYPE_CHAT_MESSAGE_MINE = 1
const val TYPE_CHAT_MESSAGE_YOURS = 2

/**
 * function : 聊天適配器
 *
 * Created on 2018/12/26  11:25
 * @author mnlin
 */
class ChatAdapter<T : ChatMessageBean>(
    private var datas: MutableList<T>
) : RecyclerView.Adapter<BaseRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        val layout = when (viewType) {
            TYPE_CHAT_MESSAGE_SYSTEM -> R.layout.item_order_chat_system
            TYPE_CHAT_MESSAGE_MINE -> R.layout.item_order_chat_mine
            TYPE_CHAT_MESSAGE_YOURS -> R.layout.item_order_chat_yours
            else -> TODO()
        }

        //創建holder
        return BaseRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false
            ), null
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    /**
     * 目前支持三種類型數據: KEY_TYPE_CHAT_MESSAGE
     * 系統消息 TYPE_CHAT_MESSAGE_SYSTEM
     * 對方 TYPE_CHAT_MESSAGE_MINE
     * 自己 TYPE_CHAT_MESSAGE_YOURS
     */
    override fun getItemViewType(position: Int): Int {
        return datas[position].chat_status
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        holder.itemView.tv_message.text = datas[position].chat_message
        holder.itemView.tv_time?.text = datas[position].chat_time
    }
}
