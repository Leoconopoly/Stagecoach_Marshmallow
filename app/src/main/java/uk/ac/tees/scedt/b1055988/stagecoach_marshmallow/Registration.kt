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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

data class User(
    val userId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone_number: String? = null
)

class Registration : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextSurname: TextInputEditText
    private lateinit var editTextPhoneNumber: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextFirstName = findViewById(R.id.firstname)
        editTextSurname = findViewById(R.id.surname)
        editTextPhoneNumber = findViewById(R.id.phone_number)
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
            val firstName = editTextFirstName.text.toString()
            val surname = editTextSurname.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()

            if (TextUtils.isEmpty(email)) {
                // Validate email field
                Toast.makeText(this@Registration, "Enter email", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                // Validate password field
                Toast.makeText(this@Registration, "Enter password", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(firstName)) {
                // Validate first name field
                Toast.makeText(this@Registration, "Enter first name", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (firstName.length < 3 || firstName.length > 25) {
                // Validate first name length
                Toast.makeText(this@Registration, "First name should be between 3 and 25 characters", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (!firstName.matches(Regex("^[a-zA-Z]+$"))) {
                // Validate first name contains letters only
                Toast.makeText(this@Registration, "First name should contain letters only", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(surname)) {
                // Validate surname field
                Toast.makeText(this@Registration, "Enter surname", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (surname.length < 3 || surname.length > 25) {
                // Validate surname length
                Toast.makeText(this@Registration, "Surname should be between 3 and 25 characters", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (!surname.matches(Regex("^[a-zA-Z]+$"))) {
                // Validate surname contains letters only
                Toast.makeText(this@Registration, "Surname should contain letters only", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phoneNumber)) {
                // Validate phone number field
                Toast.makeText(this@Registration, "Enter phone number", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (!phoneNumber.matches(Regex("^\\d+$"))) {
                // Validate phone number contains numbers only
                Toast.makeText(this@Registration, "Phone number should contain numbers only", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return@setOnClickListener
            } else if (phoneNumber.length > 15) {
                // Validate phone number length
                Toast.makeText(this@Registration, "Phone number should not exceed 15 digits", Toast.LENGTH_SHORT).show()
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
                            val firebaseUser: FirebaseUser? = mAuth.currentUser
                            val userId: String? = firebaseUser?.uid

                            if (userId != null) {
                                val user = User(userId, firstName, surname, email, phoneNumber)
                                // Save user profile to the database
                                saveUserProfile(userId, user)
                            }

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
                            Log.w(TAG, "createUserWithEmailAndPassword:failure", task.exception)
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

    private fun saveUserProfile(userId: String, user: User) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child(userId).setValue(user)
    }

    companion object {
        private const val TAG = "Registration"
    }
}











