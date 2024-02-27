package edu.skku.cs.final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity: AppCompatActivity() {
    companion object{
        const val EXT_MEDICINE_NAME = "extra_key_medicine_name"
        const val EXT_NEED_PRESCRIPTION = "extra_key_need_prescription"
        const val EXT_DESCRIPTION = "extra_key_medicine_description"
        const val EXT_INDICATION_USAGE = "extra_key_indication_and_usage"
        const val EXT_STORAGE_HANDLING = "extra_key_storage_and_handling"
        const val EXT_MANUFACTURER = "extra_key_manufacturer_name"
        const val EXT_ROUTE = "extra_key_route"
        const val EXT_SUBSTANCES = "extra_key_substances_name"
        const val EXT_PHOTO_URL = "extra_key_medicine_photo_url"
        const val EXT_ERR_MSG = "extra_key_error_msg"

        const val EXT_USERNAME = "extra_key_username"

        const val EXT_TYPE = "extra_key_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        signUpButton.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        loginButton.setOnClickListener {
            val loginIntent = Intent(this, LogInActivity::class.java)
            startActivity(loginIntent)
        }
    }
}