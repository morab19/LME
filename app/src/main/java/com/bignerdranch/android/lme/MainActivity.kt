package com.bignerdranch.android.lme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var assignmentCheckBox: CheckBox
    private lateinit var otherCheckBox: CheckBox

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextButton = findViewById(R.id.next_button)
        assignmentCheckBox = findViewById(R.id.assignment_checkbox)
        otherCheckBox = findViewById(R.id.other_checkbox)

        val listOfAssignments:MutableList<AssignmentClass> = intent.getSerializableExtra("key") as MutableList<AssignmentClass>
        val beginningAndEndTime = intent.getSerializableExtra("set")

        nextButton.setOnClickListener {
            if(assignmentCheckBox.isChecked && otherCheckBox.isChecked) {
                val messageRedId = R.string.only_one
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
            else if( assignmentCheckBox.isChecked && !otherCheckBox.isChecked ){
                val intent = Intent(this, AssignmentAttributesPage::class.java)
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("set", beginningAndEndTime)
                startActivity(intent)
            }
            else if( otherCheckBox.isChecked && !assignmentCheckBox.isChecked ) {
                val intent = Intent(this, OtherAttributesPage::class.java)
                intent.putExtra("key", listOfAssignments as Serializable)
                intent.putExtra("set", beginningAndEndTime)
                startActivity(intent)
            }
            else{
                val messageRedId = R.string.choose_one
                Toast.makeText(this, messageRedId, Toast.LENGTH_SHORT).show()
            }
        }
    }
}