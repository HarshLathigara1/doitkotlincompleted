package lathigara.harsh.doitkotlin.adapter

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import lathigara.harsh.doitkotlin.R

class ChatFromItems(val leftText:String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
      return  R.layout.chat_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtOfffRight.text = leftText
    }
}