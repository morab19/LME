package com.bignerdranch.android.lme

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeSchedulePage : AppCompatActivity(){

    private lateinit var setBeginningTimeButton: Button
    private lateinit var setEndTimeButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_schedule_page)

        // Initialize the ArrayList in the beginning to be passed
        // around throughout the entire app until we reach the end.
        var listOfAssignments : ArrayList<AssignmentClass> = ArrayList()

        setBeginningTimeButton = findViewById(R.id.set_beginning_time_button)
        setEndTimeButton = findViewById(R.id.set_end_time_button)
        nextButton = findViewById(R.id.next_button)

        setBeginningTimeButton.text = SimpleDateFormat("h:00 a").format(Calendar.getInstance().time)
        setEndTimeButton.text = SimpleDateFormat("h:00 a").format(Calendar.getInstance().time)

        // This Button is for the user to set the time for when
        // they want their schedule to begin.
        setBeginningTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                setBeginningTimeButton.text = SimpleDateFormat("h:00 a").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), false ).show()
        }

        // This Button is for the user to set the time for when
        // they want their schedule to end.
        setEndTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                setEndTimeButton.text = SimpleDateFormat("h:00 a").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), false ).show()
        }

        nextButton.setOnClickListener {
            if(setBeginningTimeButton.text == setEndTimeButton.text){
                val messageRedId = R.string.please_set_the_time
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{

                var startTimeValue : String = setBeginningTimeButton.text.toString()
                var endTimeValue : String = setEndTimeButton.text.toString()

                // Checks to see if the start time of the
                // schedule entered by the user is am or pm.
                var startTimeAbbreviation : String

                if(setBeginningTimeButton.text.toString().endsWith("PM")){
                    startTimeAbbreviation = "pm"
                }
                else{
                    startTimeAbbreviation = "am"
                }

                // Checks to see if the start time of the
                // schedule entered by the user is am or pm.
                var endTimeAbbreviation : String

                if(setEndTimeButton.text.toString().endsWith("PM")){
                    endTimeAbbreviation = "pm"
                }
                else{
                    endTimeAbbreviation = "am"
                }

                val intent = Intent(this, MainActivity::class.java)
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