package com.cikup.qiscuschat.ui.login

import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount

class LoginPresenter (view : LoginContruct.View) : LoginContruct.Presenter {

    var viewModel : LoginContruct.View? = null

    init {
        viewModel = view
    }


    override fun doStart(userId: String, password: String, displayName: String) {
        viewModel?.showLoading()
        QiscusCore.setUser(userId, password)
            .withUsername(displayName)
            .save(object : QiscusCore.SetUserListener{
                override fun onSuccess(p0: QiscusAccount?) {
                    viewModel?.hideLoading()
                    viewModel?.onSuccess(p0?.email.toString())
                }

                override fun onError(p0: Throwable?) {
                    viewModel?.hideLoading()
                    viewModel?.onFailed(p0?.message.toString())
                }

            })
    }


}