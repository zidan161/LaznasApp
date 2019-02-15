package com.zidan.laznasapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFire: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        mFire = FirebaseFirestore.getInstance()

        btn_register.setOnClickListener { setRegister() }
    }

    private fun setRegister() {

        val name = edt_name.text.toString().trim()
        val email =  edt_email.text.toString().trim()
        val password = edt_password.text.toString().trim()

        val isEmail = isEmailValid(email)

        if (isEmail) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mFire.collection("User").add(
                            mapOf(
                                "name" to name,
                                "email" to email
                            )
                        )
                        startActivity<LoginActivity>()
                    } else toast("Sign Up Failed").show()
                }
        } else {
            edt_email.error = "Email Invalid"
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
