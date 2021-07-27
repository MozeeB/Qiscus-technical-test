package com.cikup.qiscuschat.ui.list_room

import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomePresenter (view: HomeContruct.View) : HomeContruct.Presenter{
    var viewModel : HomeContruct.View? = null

    init {
        viewModel = view
    }
    override fun getAllChatRoom() {

        Observable.from(QiscusCore.getDataStore().getChatRooms(100))
            .filter { chatRoom: QiscusChatRoom -> chatRoom.lastComment != null }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                viewModel?.showChatRooms(data)
            }){ throwable: Throwable ->
                viewModel?.showErrorMessage(throwable.message.toString())
            }

        QiscusApi.getInstance()
            .getAllChatRooms(true, false, true, 1, 100)
            .flatMap { iterable: List<QiscusChatRoom?>? -> Observable.from(iterable) }
            .filter{chatRoom -> chatRoom?.lastComment?.id != 0L}
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                viewModel?.showChatRooms(data as List<QiscusChatRoom>)
            }){ throwable: Throwable ->
                viewModel?.showErrorMessage(throwable.message.toString())
            }

    }

    override fun openChatRoom(chatRoom: QiscusChatRoom) {
        if (chatRoom.isGroup) {
            viewModel?.showGroupChatRoomPage(chatRoom)
            return
        }
        viewModel?.showChatRoomPage(chatRoom)
    }

}