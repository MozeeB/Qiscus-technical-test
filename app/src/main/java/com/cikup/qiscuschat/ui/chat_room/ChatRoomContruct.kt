package com.cikup.qiscuschat.ui.chat_room

import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment

interface ChatRoomContruct {
    interface View{
        fun onFailed(message: String)
        fun showChats(chatRoom:QiscusChatRoom ,comments: List<QiscusComment>)
        fun onSendMessage(comment: QiscusComment)
        fun onRealtimeStatusChanged(status:Boolean)
        fun onLoadMore(qiscusComments: List<QiscusComment>)
        fun onUserTyping(user: String?, typing: Boolean)
        fun updateLastDeliveredComment(lastDeliveredCommentId: Long)
        fun updateLastReadComment(lastReadCommentId: Long)
        fun onNewComment(qiscusComment: QiscusComment)

    }
    interface Presenter{
        fun sendChat(roomId:String,message: String)
        fun getChatRoom(roomId: String)
        fun loadCommentsAfter(comment: QiscusComment)
    }
}