package com.cikup.qiscuschat.ui.list_room

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.ui.adapter.HomeAdapter
import com.cikup.qiscuschat.utils.EXTRAS
import com.cikup.qiscuschat.utils.OnItemClickListener
import com.cikup.qiscuschat.utils.event.ContactPosition
import com.cikup.qiscuschat.utils.navigation.backToLogin
import com.cikup.qiscuschat.utils.navigation.navigationToChatRoom
import com.cikup.qiscuschat.utils.navigation.navigationToContacts
import com.orhanobut.hawk.Hawk
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.event.QiscusCommentReceivedEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity(), HomeContruct.View, OnItemClickListener {

    lateinit var homePresenter: HomePresenter

    private var adapterHome: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homePresenter = HomePresenter(this)
        adapterHome?.setOnItemClickListener(this)

        adapterHome = HomeAdapter(this)

        allChatRoomRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterHome
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.newChat) {
            navigationToContacts(this)
            return true
        }
        if (id == R.id.logout) {
            backToLogin(this)
            QiscusCore.clearUser()
            Hawk.deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    @Subscribe
    fun onReceiveComment(event: QiscusCommentReceivedEvent?) {
        homePresenter.getAllChatRoom()
    }

    @Subscribe
    fun onEventContactPosition(contactPosition: ContactPosition){
        adapterHome?.data?.get(contactPosition.position)?.let { homePresenter.openChatRoom(it) }
    }

    override fun onResume() {
        super.onResume()
        homePresenter.getAllChatRoom()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)

    }

    override fun showChatRooms(chatRooms: List<QiscusChatRoom>) {
        adapterHome?.addOrUpdate(chatRooms)
    }

    override fun showChatRoomPage(chatRoom: QiscusChatRoom) {
        val bundle = bundleOf(EXTRAS.chatRoom to chatRoom)
        navigationToChatRoom(this, bundle)
    }

    override fun showGroupChatRoomPage(chatRoom: QiscusChatRoom) {
        val bundle = bundleOf(EXTRAS.chatRoom to chatRoom)
        navigationToChatRoom(this, bundle)
    }

    override fun showErrorMessage(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        adapterHome?.data?.get(position)?.let { homePresenter.openChatRoom(it) }
    }
}