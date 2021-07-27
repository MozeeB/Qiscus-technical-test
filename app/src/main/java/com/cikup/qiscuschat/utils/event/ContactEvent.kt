package com.cikup.qiscuschat.utils.event

import com.qiscus.sdk.chat.core.data.model.QiscusAccount

data class ContactEvent(var qiscusAccount: QiscusAccount)

data class ContactPosition(var position:Int)