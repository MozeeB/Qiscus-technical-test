package com.cikup.qiscuschat.ui.chat_room

import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.local.QiscusCacheManager
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import com.qiscus.sdk.chat.core.event.QiscusMqttStatusEvent
import com.qiscus.sdk.chat.core.presenter.QiscusChatRoomEventHandler
import com.qiscus.sdk.chat.core.util.QiscusAndroidUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class ChatRoomPresenter(view: ChatRoomContruct.View, chatRoomMain: QiscusChatRoom) : ChatRoomContruct.Presenter,
    QiscusChatRoomEventHandler.StateListener {

    var viewModel: ChatRoomContruct.View? = null
    var chatRoom : QiscusChatRoom? = null
    var roomEventHandler: QiscusChatRoomEventHandler? = null
    var qiscusAccount : QiscusAccount? = null


    init {
        viewModel = view
        chatRoom = chatRoomMain
        roomEventHandler = QiscusChatRoomEventHandler(chatRoom, this)

        qiscusAccount = QiscusCore.getQiscusAccount()
    }

    private fun clearUnreadCount() {
        chatRoom?.unreadCount = 0
        chatRoom?.lastComment = null
        QiscusCore.getDataStore().addOrUpdate(chatRoom)
    }

    fun detachView() {
        roomEventHandler!!.detach()
        clearUnreadCount()
        chatRoom = null
        EventBus.getDefault().unregister(this)
    }

    override fun sendChat(roomId: String, message: String) {
        val message = QiscusComment.generateMessage(roomId.toLong(), message)
        QiscusApi.getInstance().sendMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSend ->
                viewModel?.onSendMessage(onSend)
            }) { throwable: Throwable ->
                viewModel?.onFailed(throwable.message.toString())

            }

    }

    override fun getChatRoom(roomId: String) {
        QiscusApi.getInstance().getChatRoomWithMessages(roomId.toLong())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{ roomData ->
                roomEventHandler?.setChatRoom(roomData.first)
            }
            .subscribe({ onComment ->
                viewModel?.showChats(onComment.first, onComment.second)
            }) { throwable: Throwable ->
                viewModel?.onFailed(throwable.message.toString())

            }
    }

    override fun loadCommentsAfter(comment: QiscusComment) {
        QiscusApi.getInstance().getNextMessagesById(chatRoom!!.id, 20, comment.id)
            .doOnNext { qiscusComment: QiscusComment -> qiscusComment.roomId = chatRoom!!.id }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({comments ->
                if (viewModel != null) {
                    viewModel?.onLoadMore(listOf(comments))
                }
            }){throwable:Throwable ->
                viewModel?.onFailed(throwable.message.toString())
            }
    }


    @Subscribe
    fun onMqttEvent(event: QiscusMqttStatusEvent) {
        viewModel?.onRealtimeStatusChanged(event == QiscusMqttStatusEvent.CONNECTED)
    }

    override fun onChatRoomNameChanged(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onChatRoomMemberAdded(p0: QiscusRoomMember?) {
        TODO("Not yet implemented")
    }

    override fun onChatRoomMemberRemoved(p0: QiscusRoomMember?) {
        TODO("Not yet implemented")
    }

    override fun onUserTypng(email: String?, typing: Boolean) {
        QiscusAndroidUtil.runOnUIThread {
            if (viewModel != null) {
                viewModel?.onUserTyping(email, typing)
            }
        }    }

    override fun onChangeLastDelivered(lastDeliveredCommentId: Long) {
        QiscusAndroidUtil.runOnUIThread {
            if (viewModel != null) {
                viewModel?.updateLastDeliveredComment(lastDeliveredCommentId)
            }
        }    }

    override fun onChangeLastRead(lastReadCommentId: Long) {
        QiscusAndroidUtil.runOnUIThread {
            if (viewModel != null) {
                viewModel?.updateLastReadComment(lastReadCommentId)
            }
        }
    }

}