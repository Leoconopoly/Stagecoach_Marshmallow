package uk.ac.tees.scedt.b1055988.stagecoach_marshmallow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.tees.scedt.b1055988.stagecoach_marshmallow.R
import java.text.SimpleDateFormat
import java.util.*

class CourseAdapter(private val courseList: ArrayList<Course>) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare the TextViews for each item view in the RecyclerView
        val courseTitle: TextView = itemView.findViewById(R.id.course_title)
        val location: TextView = itemView.findViewById(R.id.course_location)
        val cost: TextView = itemView.findViewById(R.id.course_cost)
        val date: TextView = itemView.findViewById(R.id.course_date)
        val numOfClasses: TextView = itemView.findViewById(R.id.course_num_of_classes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        // Inflate the item view layout and create a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        // Bind data to the views of each item in the RecyclerView
        val course = courseList[position]
        holder.courseTitle.text = course.title
        holder.location.text = course.location
        holder.cost.text = course.cost
        holder.date.text = formatDate(course.date)
        holder.numOfClasses.text = course.numOfClasses?.toString()
    }

    override fun getItemCount() = courseList.size

    private fun formatDate(date: String?): String {
        // Format the date string from input format (dd/MM/yyyy) to output format (dd MMM yyyy)
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.UK)
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }
}



