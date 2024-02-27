package edu.skku.cs.final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LogInActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val client = OkHttpClient()

        val host = "https://w6wwciwhg5.execute-api.us-east-1.amazonaws.com/"
        var path = "getuser?username="

        val confirmLogInButton = findViewById<Button>(R.id.confirmLoginButton)
        val usernameEditText = findViewById<EditText>(R.id.usernameLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordLoginEditText)

        confirmLogInButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            path += "${username}&password=${password}"

            val req = Request.Builder().url(host + path).build()

            client.newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(applicationContext,
                                        "Failed to connect with the server",
                                        Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if(!response.isSuccessful) {
                            CoroutineScope(Dispatchers.Main).launch{
                                Toast.makeText(applicationContext,
                                    "Failed to get a valid response",
                                    Toast.LENGTH_SHORT).show()
                            }

                            throw IOException("Unexpected code $response")
                        }

                        val successStr = response.body!!.string()

                        val successDataModel = Gson().fromJson(successStr, UserStatus::class.java)

                        if(successDataModel.success == "true") {
                            val searchIntent = Intent(this@LogInActivity, SearchActivity::class.java).apply {
                                putExtra(MainActivity.EXT_USERNAME, username)
                            }

                            startActivity(searchIntent)
                            finish()
                        }
                        else {
                            val loginFailedIntent = Intent(this@LogInActivity, PopUpActivity::class.java).apply{
                                putExtra(MainActivity.EXT_TYPE, "login")
                                putExtra(MainActivity.EXT_ERR_MSG, "Login Invalid")
                            }

                            startActivity(loginFailedIntent)
                        }
                    }
                }
            })
        }
    }
}