package com.cikup.qiscuschat.ui.list_room

import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom

interface HomeContruct {

    interface View{
        fun showChatRooms(chatRooms: List<QiscusChatRoom>)

        fun showChatRoomPage(chatRoom: QiscusChatRoom)

        fun showGroupChatRoomPage(chatRoom: QiscusChatRoom)

        fun showErrorMessage(errorMessage: String?)
    }

    interface Presenter{
        fun getAllChatRoom()
        fun openChatRoom(qiscusChatRoom: QiscusChatRoom)
    }
}