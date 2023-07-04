package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

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
            // Display the users email in the text view
            textView.text = user?.email
        }

        val coursesButton: Button = findViewById(R.id.courses)
        coursesButton.setOnClickListener {
            val intent = Intent(this, Courses::class.java)
            startActivity(intent)
        }

        val addCoursesButton: Button = findViewById(R.id.course_add_screen)
        coursesButton.setOnClickListener {
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


