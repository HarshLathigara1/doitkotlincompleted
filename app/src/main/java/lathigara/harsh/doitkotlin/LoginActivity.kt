package lathigara.harsh.doitkotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_actrivity.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_actrivity)

        btnLogin.setOnClickListener {
            val logEmail = edtLogEmail.text.toString()
            val logPass = edtLogPass.text.toString()

           val mAuth = FirebaseAuth.getInstance().signInWithEmailAndPassword(logEmail, logPass)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    val user:FirebaseUser? = mAuth.currentUser

                    Toast.makeText(applicationContext, "Logged in $user ", Toast.LENGTH_LONG).show()

                }


        }

    }


}