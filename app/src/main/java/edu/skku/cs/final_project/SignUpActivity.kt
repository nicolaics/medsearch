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

class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val client = OkHttpClient()

        val host = "https://w6wwciwhg5.execute-api.us-east-1.amazonaws.com/"
        val path = "adduser"

        val mediaType = "application/json".toMediaType()

        val confirmSignUpButton = findViewById<Button>(R.id.confirmSignUpButton)
        val usernameEditText = findViewById<EditText>(R.id.usernameSignUpEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordSignUpEditText)

        confirmSignUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val json = Gson().toJson(PostUsername(username, password))

            val req =
                Request.Builder().url(host + path).post(json.toString().toRequestBody(mediaType))
                    .header("Content-Type", "application/json").build()

            client.newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()

                    CoroutineScope(Dispatchers.Main).launch{
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
                            val popUpIntent = Intent(this@SignUpActivity, PopUpActivity::class.java).apply {
                                putExtra(MainActivity.EXT_USERNAME, username)
                                putExtra(MainActivity.EXT_TYPE, "sign up success")
                            }

                            startActivity(popUpIntent)
                            finish()
                        }
                        else {
                            val signUpFailedIntent = Intent(this@SignUpActivity, PopUpActivity::class.java).apply{
                                putExtra(MainActivity.EXT_TYPE, "sign up fail")
                                putExtra(
                                    MainActivity.EXT_ERR_MSG, "Sign Up Failed:\n" +
                                        "Username exists already\n" +
                                        "Please choose another username")
                            }

                            startActivity(signUpFailedIntent)
                        }
                    }
                }
            })
        }
    }
}