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

        database = FirebaseDatabase.getInstance().getReference("courses")
        courseList = ArrayList()
        adapter = CourseAdapter(courseList)

        binding.coursesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.coursesRecyclerView.adapter = adapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for (courseSnapshot in snapshot.children) {
                    val course = courseSnapshot.getValue(Course::class.java)
                    course?.let { courseList.add(it) }
                }

                // Sort the courseList based on the date
                courseList.sortByDescending { it.date }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}

