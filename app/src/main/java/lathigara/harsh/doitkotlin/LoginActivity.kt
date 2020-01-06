package lathigara.harsh.doitkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_actrivity.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_actrivity)

        btnLogin.setOnClickListener {
            val logEmail = edtLogEmail.text.toString()
            val logPass = edtLogPass.text.toString()

           FirebaseAuth.getInstance().signInWithEmailAndPassword(logEmail, logPass)
                .addOnCompleteListener{

                    if (!it.isSuccessful) return@addOnCompleteListener

                    val user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    val intent = Intent(this@LoginActivity,LatestMessageActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Logged in ${user!!.displayName} ", Toast.LENGTH_LONG).show()

                }


        }

    }


}