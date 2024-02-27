package edu.skku.cs.final_project

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class PopUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up)

        val type = intent.getStringExtra(MainActivity.EXT_TYPE)

        val popUpMessageTextView = findViewById<TextView>(R.id.popUpWindowMessageTextView)
        val popUpTitleTextView = findViewById<TextView>(R.id.popUpWindowTitleTextView)
        val popUpOkButton = findViewById<Button>(R.id.okPopUpWindowButton)

        if(type == "sign up success") {
            val username = intent.getStringExtra(MainActivity.EXT_USERNAME)

            val message =
                "Congratulations ${username}!\n\nYou have successfully signed up to our service!\n\n" +
                        "Please log in to continue!"

            popUpMessageTextView.text = message

            popUpTitleTextView.text = "Signed Up Successfully!"
        }
        else{
            popUpTitleTextView.text = "Error!"
            popUpMessageTextView.text = intent.getStringExtra(MainActivity.EXT_ERR_MSG)

            popUpTitleTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
            popUpTitleTextView.setTypeface(null, Typeface.BOLD)
        }

        popUpOkButton.setOnClickListener {
            finish()
        }
    }
}