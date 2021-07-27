package com.cikup.qiscuschat.ui.chat_room

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.ui.chat_room.fragment.ChatRoomFragment
import com.cikup.qiscuschat.utils.EXTRAS
import com.cikup.qiscuschat.utils.navigation.backToMain
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import com.qiscus.sdk.chat.core.event.QiscusUserStatusEvent
import com.qiscus.sdk.chat.core.util.QiscusDateUtil
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import rx.Observable

class ChatRoomActivity : AppCompatActivity(), ChatRoomFragment.OnUserTyping {

    private lateinit var chatRoom: QiscusChatRoom

    var opponentEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        chatRoom = intent?.getParcelableExtra<QiscusChatRoom>(EXTRAS.chatRoom)!!


        val bundle = bundleOf(EXTRAS.chatRoom to chatRoom)
        val chatRoomFragment = ChatRoomFragment()
        chatRoomFragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, chatRoomFragment)
            .commit()

        getOpponentIfNotGroupEmail()
        listenUser()

        tvTitle.text = chatRoom.name

        btn_back.setOnClickListener {
            backToMain(this)
        }
    }

    private fun getOpponentIfNotGroupEmail() {
        if (!chatRoom.isGroup) {
            opponentEmail = Observable.from(chatRoom.member)
                .map { obj: QiscusRoomMember -> obj.email }
                .filter { email: String ->
                    email != QiscusCore.getQiscusAccount().email
                }
                .first()
                .toBlocking()
                .single()
        }
    }

    private fun listenUser() {
        if (!chatRoom.isGroup && opponentEmail.isNotEmpty()) {
            Log.e("TAG", "listenUser: $opponentEmail", )
            QiscusPusherApi.getInstance().subscribeUserOnlinePresence(opponentEmail)
        }
    }

    private fun unListenUser() {
        if (!chatRoom.isGroup && opponentEmail.isNotEmpty()) {
            QiscusPusherApi.getInstance().unsubscribeUserOnlinePresence(opponentEmail)
        }
    }

    override fun onUserTyping(user: String, typing: Boolean) {
        subtitle.text = if (typing) "Typing..." else "Online"
        subtitle.visibility = View.VISIBLE
    }

    @Subscribe
    fun onUserOnlinePresence(event: QiscusUserStatusEvent) {
        val last = QiscusDateUtil.getRelativeTimeDiff(event.lastActive)
        subtitle.text = if (event.isOnline) "Online" else "Last seen $last"
        subtitle.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        unListenUser()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        backToMain(this)
    }

}