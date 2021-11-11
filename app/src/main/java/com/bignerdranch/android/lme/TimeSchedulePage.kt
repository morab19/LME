package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.Serializable
import kotlin.collections.ArrayList

class TimeSchedulePage : AppCompatActivity(){

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var nextButton: Button
    private lateinit var startHourTimeSpinner: Spinner
    private lateinit var endHourTimeSpinner: Spinner
    private lateinit var startHalfHourTimeSpinner: Spinner
    private lateinit var endHalfHourTimeSpinner: Spinner
    private lateinit var startTimeOfDaySpinner: Spinner
    private lateinit var endTimeOfDaySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_schedule_page)

        // Initialize the ArrayList in the beginning to be passed
        // around throughout the entire app until we reach the end.
        val listOfAssignments : ArrayList<AssignmentClass> = ArrayList()

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        nextButton = findViewById(R.id.next_button)
        startHourTimeSpinner = findViewById(R.id.starting_hour_spinner)
        endHourTimeSpinner = findViewById(R.id.ending_hour_spinner)
        startHalfHourTimeSpinner = findViewById(R.id.starting_half_hour_spinner)
        endHalfHourTimeSpinner = findViewById(R.id.ending_half_hour_spinner)
        startTimeOfDaySpinner = findViewById(R.id.starting_time_of_day_spinner)
        endTimeOfDaySpinner = findViewById(R.id.ending_time_of_day_spinner)

        //The spinner the user interacts with to set the hour.
        val hoursArray = resources.getStringArray(R.array.hour_array)
        val hoursAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, hoursArray)
        startHourTimeSpinner.adapter = hoursAdapter
        endHourTimeSpinner.adapter = hoursAdapter
        //The spinner the user interacts with to set the minutes.
        val halfHourArray = resources.getStringArray(R.array.half_hour_array)
        val halfHourAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, halfHourArray)
        startHalfHourTimeSpinner.adapter = halfHourAdapter
        endHalfHourTimeSpinner.adapter = halfHourAdapter
        //The spinner the user interacts with to set the time of day.
        val timeOfDayArray = resources.getStringArray(R.array.am_pm_array)
        val timeOfDayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeOfDayArray)
        startTimeOfDaySpinner.adapter = timeOfDayAdapter
        endTimeOfDaySpinner.adapter = timeOfDayAdapter

        //The following code executes when the user clicks the "NEXT" Button
        nextButton.setOnClickListener {

            //The inputted start and end times are turned into a string.
            val startTimeValue: String = startHourTimeSpinner.selectedItem.toString() + ":" + startHalfHourTimeSpinner.selectedItem.toString() + " " + startTimeOfDaySpinner.selectedItem.toString()
            val endTimeValue: String = endHourTimeSpinner.selectedItem.toString() + ":" + endHalfHourTimeSpinner.selectedItem.toString() + " " + endTimeOfDaySpinner.selectedItem.toString()

            //This if statement prevents the user from setting
            //the beginning and end times of the schedule the same.
            if(startTimeValue == endTimeValue){
                val messageRedId = R.string.please_set_the_time
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            //This if statement prevents the user from setting the
            //schedule from PM to AM(Ex: 10PM to 1AM is invalid).
            else if(startTimeValue.endsWith("PM") && (endTimeValue.endsWith("AM"))) {
                val messageRedId = R.string.pm_to_am_invalid
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{
                val startTimeAbbreviation : String
                // Checks to see if the start time of the
                // schedule entered by the user is am or pm.
                if(startTimeValue.endsWith("PM")){
                    startTimeAbbreviation = "PM"
                }
                else{
                    startTimeAbbreviation = "AM"
                }

                val endTimeAbbreviation : String
                // Checks to see if the end time of the
                // schedule entered by the user is am or pm.
                if(endTimeValue.endsWith("PM")){
                    endTimeAbbreviation = "PM"
                }
                else{
                    endTimeAbbreviation = "AM"
                }
                //Moving on to the next screen.
                val intent = Intent(this, MainActivity::class.java)
                //Information being passed around throughout the app.
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("startTimeValue", startTimeValue)
                intent.putExtra("endTimeValue", endTimeValue)
                intent.putExtra("startTimeZone", startTimeAbbreviation)
                intent.putExtra("endTimeZone", endTimeAbbreviation)
                startActivity(intent)
            }
        }
    }
}