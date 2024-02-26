package com.example.Assignment.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project155.R

lateinit var userName: EditText
lateinit var userPass: EditText
lateinit var loginB: Button
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userName =findViewById(R.id.editTextText)
        userPass =findViewById(R.id.editTextPassword)
        loginB =findViewById(R.id.loginBtn)

        loginB.setOnClickListener {
            if(userName.text.toString().isEmpty() || userPass.text.toString().isEmpty())
                Toast.makeText(this@LoginActivity, "Please Fill your user name and password", Toast.LENGTH_SHORT).show()
            else if(userName.text.toString().equals("test") && userPass.text.toString().equals("test123"))
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            else
                Toast.makeText(this@LoginActivity, "Your user name and password is incorrect", Toast.LENGTH_SHORT).show()
        }
    }
}