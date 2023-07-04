package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddCourses : AppCompatActivity() {

    // Declare Firebase database reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_courses)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("courses")

        val courseTitleInput: EditText = findViewById(R.id.CourseTitle)
        val locationInput: EditText = findViewById(R.id.Location)
        val costInput: EditText = findViewById(R.id.CostOfCourse)
        val dateInput: EditText = findViewById(R.id.DateOfCourse)
        val numOfClassesInput: NumberPicker = findViewById(R.id.num_classes)

        // Set min and max values for NumberPicker
        numOfClassesInput.minValue = 1
        numOfClassesInput.maxValue = 12

        // Set up DatePickerDialog for date input
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateInput.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    dateInput.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        val addCourseButton: Button = findViewById(R.id.add_course)
        addCourseButton.setOnClickListener {
            val title = courseTitleInput.text.toString().trim()
            val location = locationInput.text.toString().trim()
            val cost = costInput.text.toString().trim()
            val date = dateInput.text.toString().trim()
            val numOfClasses = numOfClassesInput.value

            if (title.isEmpty() || title.length > 50) {
                courseTitleInput.error = "Course Title is required and should be less than 50 characters"
                courseTitleInput.requestFocus()
                return@setOnClickListener
            }

            if (location.isEmpty() || location.length > 50) {
                locationInput.error = "Location is required and should be less than 50 characters"
                locationInput.requestFocus()
                return@setOnClickListener
            }

            if (cost.isEmpty()) {
                costInput.error = "Cost is required"
                costInput.requestFocus()
                return@setOnClickListener
            } else {
                try {
                    val costAsDouble = cost.toDouble()
                    if (costAsDouble < 0) {
                        costInput.error = "Cost should not be negative"
                        costInput.requestFocus()
                        return@setOnClickListener
                    }
                } catch (e: NumberFormatException) {
                    costInput.error = "Please enter a valid cost"
                    costInput.requestFocus()
                    return@setOnClickListener
                }
            }

            if (date.isEmpty()) {
                dateInput.error = "Date is required"
                dateInput.requestFocus()
                return@setOnClickListener
            } else {
                // Add logic to validate the date (it should not be in the past)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val selectedDate = sdf.parse(date)
                if (selectedDate.before(Calendar.getInstance().time)) {
                    dateInput.error = "Date should not be in the past"
                    dateInput.requestFocus()
                    return@setOnClickListener
                }
            }

            // Create a Course object
            val course = Course(title, location, cost, date, numOfClasses)

            // Push a new course to the database
            // 'push()' method creates a unique id for each new child
            database.push().setValue(course)

            Toast.makeText(this, "Course added successfully", Toast.LENGTH_LONG).show()

            // Clear the inputs
            courseTitleInput.text.clear()
            locationInput.text.clear()
            costInput.text.clear()
            dateInput.text.clear()
            numOfClassesInput.value = 1 // reset to minimum value
        }
    }
}

data class Course(
    var title: String? = null,
    var location: String? = null,
    var cost: String? = null,
    var date: String? = null,
    var numOfClasses: Int? = null
)

