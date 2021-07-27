package com.cikup.qiscuschat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cikup.qiscuschat.R
import com.cikup.qiscuschat.utils.DateUtil
import com.cikup.qiscuschat.utils.SortedRecyclerViewAdapter
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.util.QiscusAndroidUtil
import com.qiscus.sdk.chat.core.util.QiscusDateUtil
import kotlinx.android.synthetic.main.item_my_chat.view.*
import kotlinx.android.synthetic.main.item_other_chat.view.*
import kotlinx.android.synthetic.main.item_other_chat.view.date
import kotlinx.android.synthetic.main.item_other_chat.view.dateOfMessage
import kotlinx.android.synthetic.main.item_other_chat.view.message

class ChatAdapter(private val context: Context) : SortedRecyclerViewAdapter<QiscusComment, ChatAdapter.ViewHolder>() {

    private val TYPE_MY_TEXT = 1
    private val TYPE_CHAT_TEXT = 2

    private var lastDeliveredCommentId: Long = 0
    private var lastReadCommentId: Long = 0

    interface RecyclerViewItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    override fun getItemClass(): Class<QiscusComment> {
        return QiscusComment::class.java
    }

    override fun compare(item1: QiscusComment, item2: QiscusComment): Int {
        if (item2 == item1) {
            return 0
        } else if (item2.id == -1L && item1.id == -1L) {
            return item2.time.compareTo(item1.time)
        } else if (item2.id != -1L && item1.id != -1L) {
            return QiscusAndroidUtil.compare(item2.id, item1.id)
        } else if (item2.id == -1L) {
            return 1
        } else if (item1.id == -1L) {
            return -1
        }
        return item2.time.compareTo(item1.time)
    }

    override fun getItemViewType(position: Int): Int {
        val comment: QiscusComment = data.get(position)
        return when (comment.type) {
            QiscusComment.Type.TEXT -> if (comment.isMyComment) TYPE_MY_TEXT else TYPE_CHAT_TEXT;
            else -> if (comment.isMyComment) TYPE_MY_TEXT else TYPE_CHAT_TEXT;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            TYPE_MY_TEXT -> {
                return ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_my_chat, parent, false)
                )
            }
            TYPE_CHAT_TEXT -> {
                return ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_other_chat, parent, false)
                )
            }
            else -> {
                return ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_other_chat, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentData = data[position]
        val itemView = holder.itemView


        if (position == data.size() - 1) {
            setNeedToShowDate(true, itemView)
        } else {
            setNeedToShowDate(
                !QiscusDateUtil.isDateEqualIgnoreTime(
                    data.get(position).time,
                    data.get(position + 1).time
                ),
                itemView
            )
        }

        itemView.message.text = commentData.message

        setOnClickListener(holder.itemView, position)

        if (itemView.sender != null) {
            itemView.sender.text = commentData.sender
        }
        itemView.date.text = DateUtil.getTimeStringFromDate(commentData.time)

        if (itemView.dateOfMessage != null) {
            itemView.dateOfMessage.text = DateUtil.toFullDate(commentData.time)
        }

        val pendingStateColor =
            ContextCompat.getColor(context, android.R.color.darker_gray)
        val readStateColor = ContextCompat.getColor(context, R.color.purple_200)
        val failedStateColor =
            ContextCompat.getColor(context, android.R.color.holo_red_dark)

        val state = itemView.state

        if (state != null) {
            when (commentData.state) {
                QiscusComment.STATE_PENDING, QiscusComment.STATE_SENDING -> {
                    state.setColorFilter(pendingStateColor)
                    state.setImageResource(R.drawable.ic_qiscus_info_time)
                }
                QiscusComment.STATE_ON_QISCUS -> {
                    state.setColorFilter(pendingStateColor)
                    state.setImageResource(R.drawable.ic_qiscus_sending)
                }
                QiscusComment.STATE_DELIVERED -> {
                    state.setColorFilter(pendingStateColor)
                    state.setImageResource(R.drawable.ic_qiscus_read)
                }
                QiscusComment.STATE_READ -> {
                    state.setColorFilter(readStateColor)
                    state.setImageResource(R.drawable.ic_qiscus_read)
                }
                QiscusComment.STATE_FAILED -> {
                    state.setColorFilter(failedStateColor)
                    state.setImageResource(R.drawable.ic_qiscus_sending_failed)
                }
            }
        }


    }

    private fun setNeedToShowDate(showDate: Boolean, itemView: View) {
        if (showDate) {
            if (itemView.dateOfMessage != null) {
                itemView.dateOfMessage.visibility = View.VISIBLE
            }
        } else {
            if (itemView.dateOfMessage != null) {
                itemView.dateOfMessage.visibility = View.GONE
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addOrUpdateComment(comment: QiscusComment?) {
        val index = findPosition(comment!!)
        if (index == -1) {
            data.add(comment)
        } else {
            data.updateItemAt(index, comment)
        }
        notifyDataSetChanged()
    }

    fun addOrUpdateList(comments: List<QiscusComment?>) {
        for (comment in comments) {
            val index = findPosition(comment!!)
            if (index == -1) {
                data.add(comment)
            } else {
                data.updateItemAt(index, comment)
            }
        }
        notifyDataSetChanged()
    }

    fun getLatestSentComment(): QiscusComment? {
        val size: Int = data.size()
        for (i in 0 until size) {
            val comment: QiscusComment = data.get(i)
            if (comment.state >= QiscusComment.STATE_ON_QISCUS) {
                return comment
            }
        }
        return null
    }

    fun updateLastDeliveredComment(lastDeliveredCommentId: Long) {
        this.lastDeliveredCommentId = lastDeliveredCommentId
        updateCommentState()
        notifyDataSetChanged()
    }

    fun updateLastReadComment(lastReadCommentId: Long) {
        this.lastReadCommentId = lastReadCommentId
        this.lastDeliveredCommentId = lastReadCommentId
        updateCommentState()
        notifyDataSetChanged()
    }

    private fun updateCommentState() {
        val size: Int = data.size()
        for (i in 0 until size) {
            if (data.get(i).state > QiscusComment.STATE_SENDING) {
                if (data.get(i).getId() <= lastReadCommentId) {
                    if (data.get(i).getState() === QiscusComment.STATE_READ) {
                        break
                    }
                    data.get(i).state = QiscusComment.STATE_READ
                } else if (data.get(i).getId() <= lastDeliveredCommentId) {
                    if (data.get(i).state === QiscusComment.STATE_DELIVERED) {
                        break
                    }
                    data.get(i).setState(QiscusComment.STATE_DELIVERED)
                }
            }
        }
    }
}