package com.cikup.qiscuschat.ui.contact

import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom

interface ContactContruct{

    interface View{
        fun onFailed(message: String)
        fun showLoading()
        fun hideLoading()
        fun showContacts(userModel: List<QiscusAccount>)
        fun showChatRoomPage(chatRoom: QiscusChatRoom?)


    }
    interface Presenter{
        fun getContacts(query:String, page:Long, limit:Long)
        fun createRoom(userModel: QiscusAccount)

    }
}