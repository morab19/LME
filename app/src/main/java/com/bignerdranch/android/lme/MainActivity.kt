package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    //Initialize all objects on the android screen we will be interacting with.
    private lateinit var nextButton: Button
    private lateinit var assignmentCheckBox: CheckBox
    private lateinit var otherCheckBox: CheckBox
    private lateinit var scheduleTextView: TextView
    private lateinit var currentListTextView: TextView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Connecting all objects on the android screen to
        //variable names we will be interacting with.
        nextButton = findViewById(R.id.next_button)
        assignmentCheckBox = findViewById(R.id.assignment_checkbox)
        otherCheckBox = findViewById(R.id.other_checkbox)
        scheduleTextView = findViewById(R.id.time_schedule)
        currentListTextView = findViewById(R.id.current_list)

        //Information being passed around throughout the app.
        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val startTimeValue = intent.getStringExtra("startTimeValue")
        val endTimeValue = intent.getStringExtra("endTimeValue")
        val startTimeAbbreviation = intent.getStringExtra("startTimeZone")
        val endTimeAbbreviation = intent.getStringExtra("endTimeZone")

        scheduleTextView.text = "Schedule: " + startTimeValue + " - " + endTimeValue

        if(listOfAssignments.size > 0){
            currentListTextView.text = currentListString(listOfAssignments)
        }

        // The user is asked what type of event they are adding next.
        nextButton.setOnClickListener {

            //If statement executes if both boxes are
            //checked and prevents the user from continuing.
            if(assignmentCheckBox.isChecked && otherCheckBox.isChecked) {
                val messageRedId = R.string.only_one
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            //Else if statement executes if just the assignment box is
            //checked and allows the user to move on to the next screen.
            else if( assignmentCheckBox.isChecked && !otherCheckBox.isChecked ){
                val intent = Intent(this, AssignmentAttributesPage::class.java)
                //Information being passed around throughout the app.
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("startTimeValue", startTimeValue)
                intent.putExtra("endTimeValue", endTimeValue)
                intent.putExtra("startTimeZone", startTimeAbbreviation)
                intent.putExtra("endTimeZone", endTimeAbbreviation)
                startActivity(intent)
            }
            //Else if statement executes if just the scheduled event box is
            //checked and allows the user to move on to the next screen.
            else if( otherCheckBox.isChecked && !assignmentCheckBox.isChecked ) {
                val intent = Intent(this, OtherAttributesPage::class.java)
                //Information being passed around throughout the app.
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("startTimeValue", startTimeValue)
                intent.putExtra("endTimeValue", endTimeValue)
                intent.putExtra("startTimeZone", startTimeAbbreviation)
                intent.putExtra("endTimeZone", endTimeAbbreviation)
                startActivity(intent)
            }
            //Else statement executes if neither box are
            //checked and prevents the user from continuing.
            else{
                val messageRedId = R.string.choose_one
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //This function is used to print out and display the current list.
    private fun currentListString(
        listOfAssignments: MutableList<AssignmentClass>
    ): String {

        var varStr = ""

        for(index in listOfAssignments){

            if(index.booleanClass){
                varStr = varStr + index.name + ": Difficulty: " + index.difficulty + "\n"
            }
            else{
                varStr = varStr + index.name + ": " + index.startTime + " - " + index.endTime + "\n"
            }
        }

        return varStr
    }
}