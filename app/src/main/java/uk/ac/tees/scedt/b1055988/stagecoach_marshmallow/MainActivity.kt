package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Database instance
    private lateinit var database: DatabaseReference

    // UI elements
    private lateinit var logoutButton: Button
    private lateinit var textView: TextView

    // Current user
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialise Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialise UI elements
        logoutButton = findViewById(R.id.logout)
        textView = findViewById(R.id.user_details)

        // Get the current user
        user = auth.currentUser

        // Check if the user is not signed in
        if (user == null) {
            // Redirect to the login screen
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // Retrieve the user profile from the database
            database.child("users").child(user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("firstName").value.toString()
                    val lastName = snapshot.child("lastName").value.toString()
                    textView.text = "Welcome $firstName $lastName"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Display a generic welcome message if retrieving the user profile fails
                    textView.text = "Welcome"
                }
            })
        }

        val coursesButton: Button = findViewById(R.id.courses)
        coursesButton.setOnClickListener {
            val intent = Intent(this, Courses::class.java)
            startActivity(intent)
        }

        val addCoursesButton: Button = findViewById(R.id.course_add_screen)
        addCoursesButton.setOnClickListener {
            val intent = Intent(this, AddCourses::class.java)
            startActivity(intent)
        }

        // Logout button click listener
        logoutButton.setOnClickListener {
            // Sign out the user from Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Redirect to the login screen
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}



