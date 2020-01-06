package lathigara.harsh.doitkotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import lathigara.harsh.doitkotlin.adapter.ChatFromItems
import lathigara.harsh.doitkotlin.adapter.ChattoItem
import lathigara.harsh.doitkotlin.model.ChatMessage
import lathigara.harsh.doitkotlin.model.User

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "chatLog"
    }

    var toUser: User? = null

    var adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = toUser?.userName


        // setUpDummyData()

        btnChatLogSend.setOnClickListener {
            Log.d(TAG, "message Sent")
            Toast.makeText(this, "Sent", Toast.LENGTH_LONG).show()
            performSendMessage()
        }
        listenForMessage()




    }


    private fun performSendMessage() {
        val text = edtChatLogMessage.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        // val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = toUser!!.uid
        /*  if (fromId == null) return*/
        // To Firebase
        // val messageRefe = FirebaseDatabase.getInstance().getReference("/messages")
        //     .push()

        val messageRefe =
            FirebaseDatabase.getInstance().getReference("/User_messages/$fromId/$toId")
                .push()

        val toref = FirebaseDatabase.getInstance().getReference("/User_messages/$toId/$fromId")
            .push()

        val chatMessage = ChatMessage(
            messageRefe.key!!, text, fromId!!, toId
            , System.currentTimeMillis() / 1000
        )

        messageRefe.setValue(chatMessage).addOnSuccessListener {

            Toast.makeText(this, " Message Saved To firebase ", Toast.LENGTH_LONG).show()
            // charRecyclerView.scrollToPosition(adapter.itemCount -1)
        }

        toref.setValue(chatMessage).addOnSuccessListener {

            Toast.makeText(this, " Message Saved To firebase ", Toast.LENGTH_LONG).show()
        }

        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("/latestMessages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)


        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latestMessages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)


        /*  toref.setValue(chatMessage).addOnSuccessListener {

              //  Toast.makeText(this, " Message Saved To firebase ", Toast.LENGTH_LONG).show()
          }
          messageRefe.setValue(chatMessage).addOnSuccessListener {

              Toast.makeText(this, " Message Saved To firebase ", Toast.LENGTH_LONG).show()
              charRecyclerView.scrollToPosition(adapter.itemCount -1)
          }


*/


        edtChatLogMessage.text = null
        recyclerVeiwChatLog.scrollToPosition(adapter.itemCount - 1)


    }

    private fun listenForMessage() {

        val fromId = FirebaseAuth.getInstance().uid

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid


        val ref = FirebaseDatabase.getInstance().getReference("/User_messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessageActivity.currentUser ?: return
                        adapter.add(
                            ChatFromItems(
                                chatMessage.text,
                                currentUser!!,
                                applicationContext
                            )
                        )

                    } else {

                        // val toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                        adapter.add(ChattoItem(chatMessage.text, toUser!!, applicationContext))

                    }





                    recyclerVeiwChatLog.adapter = adapter
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
        recyclerVeiwChatLog.scrollToPosition(adapter.itemCount - 1)


    }

}
