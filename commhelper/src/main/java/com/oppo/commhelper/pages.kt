package com.oppo.commhelper

import android.graphics.Color
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.knowledge.mnlin.page.core.PageImpl
import com.knowledge.mnlin.page.factory.PageTransAnimKeyConst
import com.knowledge.mnlin.page.interfaces.PageLauncherType
import com.knowledge.mnlin.page_annotation.annotations.InjectPageLauncherType
import com.knowledge.mnlin.page_annotation.annotations.InjectPageLayoutRes
import com.knowledge.mnlin.page_annotation.annotations.InjectPageTransAnim
import com.knowledge.mnlin.page_annotation.annotations.PageEnterPoint
import com.oppo.commhelper.adapters.ChatAdapter
import com.oppo.commhelper.adapters.TYPE_CHAT_MESSAGE_MINE
import com.oppo.commhelper.adapters.TYPE_CHAT_MESSAGE_SYSTEM
import com.oppo.commhelper.adapters.TYPE_CHAT_MESSAGE_YOURS
import com.oppo.commhelper.models.ChatMessageBean
import com.oppo.commhelper.plugins.functionplus.*
import kotlinx.android.synthetic.main.page_chat.view.*
import kotlinx.android.synthetic.main.page_main_route.view.*
import org.jetbrains.anko.backgroundDrawable

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author mnlin0905@gmail.com
 **************************************/

/**
 * function : page模块入口页,进行路由界面跳转
 *
 * Created on 2019/11/4  18:56
 * @author 80270427
 */
@PageEnterPoint
@InjectPageLayoutRes(layoutResId = R.layout.page_main_route)
class MainRoutePage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()

        contentView.apply {
            // tecent
            tv_tecent.dOnClick {
                pageContext.addPage(ChatPage())
            }

            // ali
            tv_ali.dOnClick {
                pageContext.addPage(ChatPage())
            }
        }
    }
}

/**
 * function : 主交互翻译助手界面
 *
 * Created on 2019/11/4  18:57
 * @author 80270427
 */
@InjectPageLayoutRes(layoutResId = R.layout.page_chat)
@InjectPageLauncherType(launcherType = PageLauncherType.LAUNCHER_SINGLE_TASK)
@InjectPageTransAnim(animations = [PageTransAnimKeyConst.PageTAScale, PageTransAnimKeyConst.PageTABottomRaise])
class ChatPage : PageImpl() {
    /**
     * 聊天記錄,數據源
     * 適配器
     */
    private var mDatas: MutableList<ChatMessageBean> = mutableListOf(
            ChatMessageBean(chat_status = TYPE_CHAT_MESSAGE_SYSTEM, chat_message = "Henry為您服務", chat_time = "03-06 16:04"),
            ChatMessageBean(chat_status = TYPE_CHAT_MESSAGE_YOURS, chat_message = "您好,歡迎訪問!", chat_time = "03-06 16:04"),
            ChatMessageBean(chat_status = TYPE_CHAT_MESSAGE_MINE, chat_message = "在哪里切換語言?", chat_time = "03-06 16:04")
    )
    private lateinit var mAdapter: ChatAdapter<ChatMessageBean>

    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.apply {
            //背景色
            et_message.postV {
                backgroundDrawable =
                    customShapeDrawable(height.toFloat(), Color.parseColor("#FFFFFF"))
            }

            //初始化
            mAdapter = ChatAdapter(mDatas)
            rv_messages.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
            }

            //文本框(设置返回键位逻辑)
            et_message.setOnEditorActionListener { view, actionId, _ ->
                if (actionId != EditorInfo.IME_ACTION_UNSPECIFIED) {
                    et_message.text.toString().filterAlso({ it.isNotBlank() }) {
                        mDatas.add(ChatMessageBean(chat_status = TYPE_CHAT_MESSAGE_MINE, chat_message = it, chat_time = "03-06 16:04"))
                        mDatas.add(ChatMessageBean(chat_status = TYPE_CHAT_MESSAGE_YOURS, chat_message = "請諮詢百度!", chat_time = "03-06 16:04"))
                        mAdapter.notifyDataSetChanged()
                        rv_messages.post { rv_messages.smoothScrollToPosition(mDatas.size - 1) }

                        et_message.text = null.empty(comment = "發送消息,清除記錄")
                    }
                }
                //KeyboardUtils.showSoftInput(view)
                true
            }
        }
    }
}
