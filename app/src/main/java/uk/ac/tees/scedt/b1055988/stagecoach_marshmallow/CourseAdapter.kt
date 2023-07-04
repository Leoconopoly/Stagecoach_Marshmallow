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
        val courseTitle: TextView = itemView.findViewById(R.id.course_title)
        val location: TextView = itemView.findViewById(R.id.course_location)
        val cost: TextView = itemView.findViewById(R.id.course_cost)
        val date: TextView = itemView.findViewById(R.id.course_date)
        val numOfClasses: TextView = itemView.findViewById(R.id.course_num_of_classes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.courseTitle.text = course.title
        holder.location.text = course.location
        holder.cost.text = course.cost
        holder.date.text = formatDate(course.date)
        holder.numOfClasses.text = course.numOfClasses?.toString()
    }

    override fun getItemCount() = courseList.size

    private fun formatDate(date: String?): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.UK)
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }
}


