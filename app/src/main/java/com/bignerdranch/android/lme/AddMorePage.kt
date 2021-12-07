package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.Serializable
import com.bignerdranch.android.lme.AssignmentClass as AssignmentClass1

class AddMorePage : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private lateinit var scheduleTextView: TextView
    private lateinit var currentListTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_page)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        noButton = findViewById(R.id.no_button)
        yesButton =  findViewById(R.id.yes_button)
        scheduleTextView = findViewById(R.id.time_schedule)
        currentListTextView = findViewById(R.id.current_list)

        //Information being passed around throughout the app.
        val listOfAssignments:MutableList<AssignmentClass1> = intent.getSerializableExtra("key") as MutableList<AssignmentClass1>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        scheduleTextView.text = "Schedule: " + startTimeValue + " - " + endTimeValue
        currentListTextView.text = currentListString(listOfAssignments)

        //If the user no longer wants to add another event then we process
        //the entered assignments/scheduled events on the final screen.
        noButton.setOnClickListener {
            val intent = Intent(this, RecommendedSchedulePage::class.java)
            intent.putExtra("key", listOfAssignments as Serializable)
            intent.putExtra("startTimeValue", startTimeValue)
            intent.putExtra("endTimeValue", endTimeValue)
            startActivity(intent)
        }

        //If the user would like to continue to add more then we return to the
        //screen where they are asked what type of event they would like to add.
        yesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("key", listOfAssignments as Serializable)
            intent.putExtra("startTimeValue", startTimeValue)
            intent.putExtra("endTimeValue", endTimeValue)
            intent.putExtra("startTimeZone", startTimeAbbreviation)
            intent.putExtra("endTimeZone", endTimeAbbreviation)
            startActivity(intent)
        }
    }

    private fun currentListString(
        listOfAssignments: MutableList<AssignmentClass1>
    ): String {

        var varStr = ""

        for(index in listOfAssignments){

            if(index.isAssignmentClass){
                varStr = varStr + index.name + ": Difficulty: " + index.difficulty + "\n"
            }
            else{
                varStr = varStr + index.name + ": " + index.startTime + " - " + index.endTime + "\n"
            }
        }

        return varStr
    }
}


