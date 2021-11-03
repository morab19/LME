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

        var listOfAssignments : ArrayList<AssignmentClass> = ArrayList()
        var beginningAndEndTime: ArrayList<String> = ArrayList()

        setBeginningTimeButton = findViewById(R.id.set_beginning_time_button)
        setEndTimeButton = findViewById(R.id.set_end_time_button)
        nextButton = findViewById(R.id.next_button)

        // Initialize the ArrayLists in the beginning to be passed
        // around throughout the entire app until we reach the end.
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
                // Checks to see the hour entered by the user.
                var startTimeValue : String

                if(setBeginningTimeButton.text.length == 8){
                    startTimeValue = setBeginningTimeButton.text.toString().substring(0,2)
                    beginningAndEndTime.add(startTimeValue)
                }
                else{
                    startTimeValue = setBeginningTimeButton.text.toString().substring(0,1)
                    beginningAndEndTime.add(startTimeValue)
                }

                // Checks to see the hour entered by the user.
                var endTimeValue : String

                if(setEndTimeButton.text.toString().length == 8){
                    endTimeValue = setEndTimeButton.text.toString().substring(0,2)
                    beginningAndEndTime.add(endTimeValue)
                }
                else{
                    endTimeValue = setEndTimeButton.text.toString().substring(0,1)
                    beginningAndEndTime.add(endTimeValue)
                }

                // Checks to see if the start time of the
                // schedule entered by the user is am or pm.
                var startTimeZone : String

                if(setBeginningTimeButton.text.toString().endsWith("PM")){
                    startTimeZone = "pm"
                    beginningAndEndTime.add(startTimeZone)
                }
                else{
                    startTimeZone = "am"
                    beginningAndEndTime.add(startTimeZone)
                }

                // Checks to see if the start time of the
                // schedule entered by the user is am or pm.
                var endTimeZone : String

                if(setEndTimeButton.text.toString().endsWith("PM")){
                    endTimeZone = "pm"
                    beginningAndEndTime.add(endTimeZone)
                }
                else{
                    endTimeZone = "am"
                    beginningAndEndTime.add(endTimeZone)
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("set", beginningAndEndTime)
                startActivity(intent)
            }
        }
    }
}