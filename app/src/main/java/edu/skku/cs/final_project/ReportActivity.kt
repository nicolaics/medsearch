package edu.skku.cs.final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ReportActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val confirmReportButton = findViewById<Button>(R.id.confirmReportButton)
        val backButton = findViewById<Button>(R.id.backReportButton)

        val reportRadioGroup = findViewById<RadioGroup>(R.id.reportRadioGroup)

        val otherEditText = findViewById<EditText>(R.id.otherEditText)
        otherEditText.isEnabled = false

        var selectedRadioButton : RadioButton

        reportRadioGroup.setOnCheckedChangeListener {group, checkedId ->
            selectedRadioButton = findViewById(checkedId)

            otherEditText.isEnabled = (selectedRadioButton.text == "Other")
        }

        confirmReportButton.setOnClickListener {
            val selectedId = reportRadioGroup.checkedRadioButtonId
            val finalSelectedRadioButton = findViewById<RadioButton>(selectedId).text

            var report = "Thank you for reporting the incorrect information:\n"

            if(finalSelectedRadioButton == "Other"){
                report += otherEditText.text.toString()
            }
            else{
                report += finalSelectedRadioButton
            }

            Toast.makeText(applicationContext, report, Toast.LENGTH_LONG).show()

            confirmReportButton.isEnabled = false
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}