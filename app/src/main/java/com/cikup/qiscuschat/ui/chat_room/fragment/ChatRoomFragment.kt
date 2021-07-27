package com.cikup.qiscuschat.ui.chat_room.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.ui.adapter.ChatAdapter
import com.cikup.qiscuschat.ui.chat_room.ChatRoomContruct
import com.cikup.qiscuschat.ui.chat_room.ChatRoomPresenter
import com.cikup.qiscuschat.utils.EXTRAS
import com.qiscus.sdk.chat.core.data.local.QiscusCacheManager
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import com.qiscus.sdk.chat.core.event.QiscusCommentReceivedEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat_room.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ChatRoomFragment : Fragment(), ChatRoomContruct.View {


    lateinit var presenter: ChatRoomPresenter
    lateinit var chatRoomMain: QiscusChatRoom

    private var commentsAdapter: ChatAdapter? = null

    private var userTypingListener: OnUserTyping? = null
    private var stopTypingNotifyTask: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRoomMain = arguments?.getParcelable<QiscusChatRoom>(EXTRAS.chatRoom)!!

        presenter = ChatRoomPresenter(this, chatRoomMain)

        commentsAdapter = ChatAdapter(requireContext())
        val layoutManage = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        layoutManage.reverseLayout = true
        chatRoomRV.setHasFixedSize(true)
        chatRoomRV.apply {
            layoutManager = layoutManage
            adapter = commentsAdapter
        }

        stopTypingNotifyTask = Runnable {
            notifyServerTyping(false)
        }

        messageEDT.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                notifyServerTyping(true)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        sendIV.setOnClickListener{
            val textMessage = messageEDT.text.toString()
            if (textMessage.isNotEmpty()){
                presenter.sendChat(chatRoomMain.id.toString(), textMessage)
            }
            messageEDT.setText("")
        }

        presenter.getChatRoom(chatRoomMain.id.toString())


    }
    private fun notifyServerTyping(typing: Boolean){
        QiscusPusherApi.getInstance().publishTyping(chatRoomMain.id, typing)
    }

    interface OnUserTyping{
        fun onUserTyping(user:String, typing:Boolean)
    }

    override fun onFailed(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showChats(chatRoom: QiscusChatRoom, comments: List<QiscusComment>) {
        this.chatRoomMain = chatRoom
        commentsAdapter?.addOrUpdateList(comments)
        notifyLatestRead()
    }

    private fun notifyLatestRead() {
        val comment = commentsAdapter?.getLatestSentComment()
        if (comment != null) {
            QiscusPusherApi.getInstance()
                .markAsRead(chatRoomMain.id, comment.id)
        }
    }

    override fun onSendMessage(comment: QiscusComment) {
        chatRoomRV.smoothScrollToPosition(0)
        commentsAdapter?.addOrUpdateComment(comment)
    }

    override fun onRealtimeStatusChanged(status: Boolean) {
        if (status) {
            val comment = commentsAdapter!!.getLatestSentComment()
            if (comment != null) {
                presenter.loadCommentsAfter(comment)
            }
        }
    }

    override fun onLoadMore(qiscusComments: List<QiscusComment>) {
        commentsAdapter?.addOrUpdateList(qiscusComments)
    }

    override fun onUserTyping(user: String?, typing: Boolean) {
        if ( userTypingListener != null){
            userTypingListener!!.onUserTyping(user!!, typing)
        }
    }

    override fun updateLastDeliveredComment(lastDeliveredCommentId: Long) {
        commentsAdapter?.updateLastDeliveredComment(lastDeliveredCommentId)
    }

    override fun updateLastReadComment(lastReadCommentId: Long) {
        commentsAdapter?.updateLastReadComment(lastReadCommentId)
    }

    override fun onNewComment(qiscusComment: QiscusComment) {
        commentsAdapter?.addOrUpdateComment(qiscusComment)
        if ((allChatRoomRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() <= 2) {
            chatRoomRV.smoothScrollToPosition(0)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onResume() {
        super.onResume()
        QiscusCacheManager.getInstance().setLastChatActivity(true, chatRoomMain.id)
        notifyLatestRead()
    }

    override fun onPause() {
        super.onPause()
        QiscusCacheManager.getInstance().setLastChatActivity(false, chatRoomMain.id)

    }

    override fun onDestroy() {
        super.onDestroy()
        notifyLatestRead()
        presenter.detachView()
    }

    @Subscribe
    fun onMessageReceived(event: QiscusCommentReceivedEvent) {
        if (event.qiscusComment.roomId == chatRoomMain.id) {
            commentsAdapter?.addOrUpdateComment(event.qiscusComment)
        }
    }
}