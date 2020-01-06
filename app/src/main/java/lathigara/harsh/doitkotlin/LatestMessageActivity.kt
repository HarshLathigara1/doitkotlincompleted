package lathigara.harsh.doitkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*
import lathigara.harsh.doitkotlin.adapter.LatestMessageRow
import lathigara.harsh.doitkotlin.adapter.UserItem
import lathigara.harsh.doitkotlin.model.ChatMessage
import lathigara.harsh.doitkotlin.model.User

class LatestMessageActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    val adapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_latest_message)

        latestrecyclerView.adapter = adapter
        latestrecyclerView.addItemDecoration(DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL))

        verifyUserIsLogin()

        adapter.setOnItemClickListener{item, view ->

            // val userItem = item as UserItem
            val intent = Intent(applicationContext,ChatLogActivity::class.java)
            val row = item as LatestMessageRow

            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUSer)
            startActivity(intent)
        }

        fetchUser()

        listenForLatestMessages()


    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it,applicationContext))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latestMessages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageRow(chatMessage,applicationContext))
                adapter.add(LatestMessageRow(chatMessage!!,applicationContext))
                latestMessagesMap[p0.key!!] = chatMessage

                refreshRecyclerViewMessages()


            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageRow(chatMessage!!,applicationContext))
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })
    }

    // fetching current user
    private fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                //  val adapter = GroupAdapter<GroupieViewHolder>()

                currentUser = p0.getValue(User::class.java)
                Log.d("lt", "current ${currentUser!!.userName}")

                /* adapter.setOnItemClickListener{ item, view ->
                     val userItem = item as UserItem
                     val intent = Intent(view.context,ChatLogActivity::class.java)
                     intent.putExtra(USER_KEY,userItem.user)
                     //intent.putExtra(USER_KEY,userItem.user)
                     startActivity(intent)
                     finish()
                 }
                 recyclerView_newMeassage.adapter = adapter*/

            }

        })
    }

    private fun verifyUserIsLogin() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menures, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Sign_Out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            R.id.new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)


            }
        }
        return super.onOptionsItemSelected(item)
    }
}
