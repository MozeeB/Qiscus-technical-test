package com.cikup.qiscuschat.ui.contact

import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ContactPresenter(view: ContactContruct.View) : ContactContruct.Presenter {

    var viewModel: ContactContruct.View? = null

    init {
        viewModel = view
    }

    override fun getContacts(query: String, page: Long, limit: Long) {
        viewModel?.showLoading()
        QiscusApi.getInstance().getUsers(query, page, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ qiscusAccounts ->
                viewModel?.hideLoading()
                viewModel?.showContacts(qiscusAccounts)
            }) { throwable: Throwable ->
                viewModel?.hideLoading()
                viewModel?.onFailed(throwable.message.toString())
            }


    }

    override fun createRoom(userModel: QiscusAccount) {
        viewModel?.showLoading()

        val savedChatRoom = QiscusCore.getDataStore().getChatRoom(userModel.email)

        if (savedChatRoom != null){
            viewModel?.showChatRoomPage(savedChatRoom)
            return
        }
        QiscusApi.getInstance().chatUser(userModel.email, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onCreated ->
                viewModel?.hideLoading()
                viewModel?.showChatRoomPage(onCreated)
            }){throwable : Throwable ->
                viewModel?.hideLoading()
                viewModel?.onFailed(throwable.message.toString())

            }
    }




}