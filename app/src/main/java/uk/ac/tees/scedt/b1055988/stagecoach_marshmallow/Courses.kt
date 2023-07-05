package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import uk.ac.tees.scedt.b1055988.stagecoach_marshmallow.databinding.ActivityCoursesBinding

class Courses : AppCompatActivity() {

    private lateinit var binding: ActivityCoursesBinding
    private lateinit var database: DatabaseReference
    private lateinit var courseList: ArrayList<Course>
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to the "courses" node in the Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("courses")

        // Initialize the courseList and adapter
        courseList = ArrayList()
        adapter = CourseAdapter(courseList)

        // Set up the RecyclerView with LinearLayoutManager and the adapter
        binding.coursesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.coursesRecyclerView.adapter = adapter

        // Set up ValueEventListener to listen for changes in the "courses" node
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the courseList before adding updated data
                courseList.clear()

                // Iterate through each child snapshot in the "courses" node
                for (courseSnapshot in snapshot.children) {
                    // Deserialize the courseSnapshot to a Course object
                    val course = courseSnapshot.getValue(Course::class.java)
                    course?.let { courseList.add(it) }
                }

                // Sort the courseList based on the date in descending order
                courseList.sortByDescending { it.date }

                // Notify the adapter of the updated data
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors or cancellation of the database operation
            }
        })
    }
}




