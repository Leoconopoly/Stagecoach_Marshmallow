package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Registration : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mAuth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.loginNow)

        textView.setOnClickListener {
            // Click listener for "Login Now" TextView
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            // Click listener for registration button
            showProgressBar()

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Registration, "Enter email", Toast.LENGTH_SHORT)
                    .show()
                hideProgressBar()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Registration, "Enter password", Toast.LENGTH_SHORT)
                    .show()
                hideProgressBar()
                return@setOnClickListener
            }

            // Validate password criteria
            val passwordPattern = "(?=.*[A-Z])(?=.*[!@#\$%^&*()_+=])(?=\\S+\$).{6,15}".toRegex()
            if (!passwordPattern.matches(password)) {
                // Password doesn't meet the criteria
                Toast.makeText(
                    this@Registration,
                    "Password should be 6-15 characters long, with at least one capital letter and one special character.",
                    Toast.LENGTH_SHORT
                ).show()
                hideProgressBar()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        hideProgressBar()

                        if (task.isSuccessful) {
                            // Registration successful
                            Toast.makeText(
                                this@Registration, "Account Created.",
                                Toast.LENGTH_SHORT
                            ).show()
                            mAuth.signOut() // Log out the user after registration
                            val intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmailAndPassword:failure", task
                                .exception)
                            Toast.makeText(
                                this@Registration, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    companion object {
        private const val TAG = "Registration"
    }
}







