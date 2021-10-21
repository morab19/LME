package com.bignerdranch.android.lme

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class TimeSchedulePage : AppCompatActivity(){

    private lateinit var setBeginningTimeButton: Button
    private lateinit var setEndTimeButton: Button
    private lateinit var nextButton: Button

    lateinit var listOfAssignments : List<AssignmentClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_schedule_page)

        setBeginningTimeButton = findViewById(R.id.set_beginning_time_button)
        setEndTimeButton = findViewById(R.id.set_end_time_button)
        nextButton = findViewById(R.id.next_button)

        setBeginningTimeButton.text = SimpleDateFormat("HH:mm a").format(Calendar.getInstance().time)
        setEndTimeButton.text = SimpleDateFormat("HH:mm a").format(Calendar.getInstance().time)


        setBeginningTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                setBeginningTimeButton.text = SimpleDateFormat("HH:mm a").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false ).show()
        }

        setEndTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                setEndTimeButton.text = SimpleDateFormat("HH:mm a").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false ).show()
        }

        nextButton.setOnClickListener {

            if(setBeginningTimeButton.text == setEndTimeButton.text){
                val messageRedId = R.string.please_set_the_time
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}