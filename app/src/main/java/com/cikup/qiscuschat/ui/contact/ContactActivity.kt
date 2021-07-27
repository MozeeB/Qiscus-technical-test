package com.cikup.qiscuschat.ui.contact

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.ui.adapter.ContactAdapter
import com.cikup.qiscuschat.utils.EXTRAS
import com.cikup.qiscuschat.utils.event.ContactEvent
import com.cikup.qiscuschat.utils.navigation.backToMain
import com.cikup.qiscuschat.utils.navigation.navigationToChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import kotlinx.android.synthetic.main.activity_contact.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ContactActivity : AppCompatActivity(), ContactContruct.View {

    lateinit var presenter: ContactPresenter

    val bus: EventBus = EventBus.getDefault()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)


        presenter = ContactPresenter(this)
        presenter.getContacts("", 1, 100)
    }

    override fun onSupportNavigateUp(): Boolean {
        backToMain(this)
        return true
    }

    override fun onBackPressed() {
        backToMain(this)
        super.onBackPressed()

    }

    override fun onFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressBarHolderCL.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBarHolderCL.visibility = View.GONE
    }


    override fun showContacts(user: List<QiscusAccount>) {
        contactsRV.apply {
            layoutManager = LinearLayoutManager(this@ContactActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ContactAdapter(user)
        }
    }

    override fun showChatRoomPage(chatRoom: QiscusChatRoom?) {
        val bundle = bundleOf(EXTRAS.chatRoom to chatRoom)
        navigationToChatRoom(this, bundle)
    }

    @Subscribe
    fun onEventContact(contactEvent: ContactEvent){
        presenter.createRoom(contactEvent.qiscusAccount)
    }

    override fun onResume() {
        super.onResume()
        bus.register(this)
    }

    override fun onPause() {
        super.onPause()
        bus.unregister(this)
    }
}