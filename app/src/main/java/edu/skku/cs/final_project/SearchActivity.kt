package edu.skku.cs.final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SearchActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val apiKeyFDA = "Uj1n3NFcbYGmIFhHByGhaCkj1pmk4NDFE2eCPw1c"
        val host = "https://api.fda.gov/drug/label.json?api_key=$apiKeyFDA"

        val welcomeTextView = findViewById<TextView>(R.id.welcomeBackTextView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val startSearchButton = findViewById<Button>(R.id.startSearchButton)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val chipGroup = findViewById<ChipGroup>(R.id.searchTypeChipGroup)

        val username = intent.getStringExtra(MainActivity.EXT_USERNAME)

        var show = 10
        val medicineData = ArrayList<Medicine>()

        welcomeTextView.text = "Welcome Back ${username}!"

        logoutButton.setOnClickListener {
            finish()
        }

        startSearchButton.setOnClickListener {
            var searchType : String

            try {
                when(chipGroup.findViewById<Chip>(chipGroup.checkedChipId).text.toString()) {
                    "Substance Name" -> searchType = "openfda.substance_name"
                    "Manufacturer Name" -> searchType = "openfda.manufacturer_name"
                    "Usage" -> searchType = "indications_and_usage"
                    "Description" -> searchType = "description"
                    "Consumption Way" -> searchType = "openfda.route"
                    else -> searchType = "openfda.brand_name"
                }
            }
            catch(_: java.lang.NullPointerException){
                searchType = "openfda.brand_name"
            }

            val searchMedicineName = searchEditText.text.toString()

            val searchParams = "&search=${searchType}:\"${searchMedicineName}\"&limit=50"

            val client = OkHttpClient()

            val req = Request.Builder().url(host + searchParams).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            this@SearchActivity,
                            "Failed to Establish Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if(!response.isSuccessful) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    this@SearchActivity,
                                    "Invalid Search Keyword",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            throw IOException("Unexpected code $response")
                        }

                        val str = response.body!!.string()
                        val medicineDataModel = Gson().fromJson(str, MedicineDataModel::class.java)

                        var medicineName =
                            medicineDataModel.results.get(0).openfda.brand_name?.get(0)
                        var storageAndHandling =
                            medicineDataModel.results.get(0).storage_and_handling?.get(0)
                        var description = medicineDataModel.results.get(0).description?.get(0)
                        var indicationAndUsage =
                            medicineDataModel.results.get(0).indications_and_usage?.get(0)
                        var manufacturerName =
                            medicineDataModel.results.get(0).openfda.manufacturer_name?.get(0)
                        var route = medicineDataModel.results.get(0).openfda.route?.get(0)

                        for(i in 0 until medicineDataModel.results.size) {
                            medicineName =
                                medicineDataModel.results.get(i).openfda.brand_name?.get(0)
                                    ?: medicineName
                            val effectiveDate = medicineDataModel.results.get(i).effective_time
                            storageAndHandling =
                                medicineDataModel.results.get(i).storage_and_handling?.get(0)
                                    ?: storageAndHandling
                            description =
                                medicineDataModel.results.get(i).description?.get(0) ?: description
                            val needPrescription: String

                            if(medicineDataModel.results.get(i).openfda.product_type?.get(0) == "HUMAN PRESCRIPTION DRUG") {
                                needPrescription = "Yes"
                            }
                            else {
                                needPrescription = "No"
                            }

                            indicationAndUsage =
                                medicineDataModel.results.get(i).indications_and_usage?.get(0)
                                    ?: indicationAndUsage
                            manufacturerName =
                                medicineDataModel.results.get(i).openfda.manufacturer_name?.get(0)
                                    ?: manufacturerName
                            route = medicineDataModel.results.get(i).openfda.route?.get(0) ?: route
                            val substancesName =
                                medicineDataModel.results.get(i).openfda.substance_name

                            try {
                                val meds = Medicine(
                                    medicineName, needPrescription, description,
                                    indicationAndUsage, effectiveDate, storageAndHandling,
                                    manufacturerName, route, substancesName, null
                                )
                                medicineData.add(meds)
                            }
                            catch(_: java.lang.NullPointerException) {
                            }
                        }

                        var filtered: Boolean

                        do {
                            filtered = false
                            var holdIndex = 0

                            for(i in 0 until medicineData.size) {
                                for(j in (i + 1) until medicineData.size) {
                                    if(medicineData.get(i).name!!.uppercase() == medicineData.get(j).name!!.uppercase()) {
                                        if(medicineData.get(i).effectiveDate > medicineData.get(j).effectiveDate) {
                                            holdIndex = j
                                            filtered = true
                                            break
                                        }
                                        else {
                                            holdIndex = i
                                            filtered = true
                                            break
                                        }
                                    }
                                }

                                if(filtered) {
                                    medicineData.remove(medicineData.get(holdIndex))
                                    break
                                }
                            }
                        } while(filtered)

                        if(medicineData.isEmpty()) {
                            val popUpIntent = Intent(this@SearchActivity, PopUpActivity::class.java).apply {
                                putExtra(MainActivity.EXT_TYPE, "search")
                                putExtra(MainActivity.EXT_ERR_MSG, "Result not found!")
                            }

                            startActivity(popUpIntent)
                        }
                        else {
                            for(i in 0 until medicineData.size) {
                                if(!medicineData.get(i).description.isNullOrEmpty()) {
                                    var uppercaseString = false
                                    var j = 0
                                    var holdIndex = 0

                                    do {
                                        if(medicineData.get(i).description!![j].isUpperCase()) {
                                            if(medicineData.get(i).description!![j + 1].isUpperCase()) {
                                                uppercaseString = true
                                            }
                                            else if(medicineData.get(i).description!![j + 1].isLowerCase()) {
                                                uppercaseString = false
                                                holdIndex = j
                                            }
                                        }
                                        j++
                                    } while(uppercaseString && j < medicineData.get(i).description!!.length)

                                    medicineData.get(i).description =
                                        medicineData.get(i).description?.substring(holdIndex)
                                }
                                else{
                                    medicineData.get(i).description = "No information available"
                                }

                                if(!medicineData.get(i).indicationsAndUsage.isNullOrEmpty()) {
                                    var uppercaseString = false
                                    var holdIndex = 0
                                    var j = 0

                                    do {
                                        if(medicineData.get(i).indicationsAndUsage!![j].isUpperCase()) {
                                            if(medicineData.get(i).indicationsAndUsage!![j + 1].isUpperCase()) {
                                                uppercaseString = true
                                            }
                                            else if(medicineData.get(i).indicationsAndUsage!![j + 1].isLowerCase()) {
                                                uppercaseString = false
                                                holdIndex = j
                                            }
                                        }

                                        j++
                                    } while(uppercaseString && j < medicineData.get(i).indicationsAndUsage!!.length)

                                    medicineData.get(i).indicationsAndUsage =
                                        medicineData.get(i).indicationsAndUsage?.substring(holdIndex)
                                }
                                else{
                                    medicineData.get(i).indicationsAndUsage = "No information available"
                                }

                                if(medicineData.get(i).storageAndHandling.isNullOrEmpty()){
                                   medicineData.get(i).storageAndHandling = "No information available"
                                }

                                if(medicineData.get(i).manufacturerName.isNullOrEmpty()){
                                    medicineData.get(i).manufacturerName = "No information available"
                                }

                                if(medicineData.get(i).substancesName.isEmpty()){
                                    medicineData.get(i).substancesName[0]= "No information available"
                                }
                            }

                            val medicineListSend: ArrayList<Medicine>

                            if(medicineData.size < show) {
                                medicineListSend = medicineData
                            }
                            else {
                                medicineListSend =
                                    medicineData.subList(0, show).toCollection(ArrayList())
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                val gridViewAdapter =
                                    GridViewAdapter(this@SearchActivity, medicineListSend)
                                val gridView = findViewById<GridView>(R.id.gridViewSearch)

                                gridView.adapter = gridViewAdapter
                                gridView.setOnItemClickListener {adapterView, view, i, l ->
                                    val detailsIntent = Intent(
                                        applicationContext,
                                        MedicineDetailsActivity::class.java
                                    ).apply {
                                        putExtra(MainActivity.EXT_MEDICINE_NAME, medicineData[i].name)
                                        putExtra(
                                            MainActivity.EXT_NEED_PRESCRIPTION,
                                            medicineData[i].needPrescription
                                        )
                                        putExtra(MainActivity.EXT_DESCRIPTION, medicineData[i].description)
                                        putExtra(
                                            MainActivity.EXT_INDICATION_USAGE,
                                            medicineData[i].indicationsAndUsage
                                        )
                                        putExtra(
                                            MainActivity.EXT_STORAGE_HANDLING,
                                            medicineData[i].storageAndHandling
                                        )
                                        putExtra(MainActivity.EXT_MANUFACTURER, medicineData[i].manufacturerName)
                                        putExtra(MainActivity.EXT_ROUTE, medicineData[i].route)
                                        putExtra(MainActivity.EXT_SUBSTANCES, medicineData[i].substancesName)
                                        putExtra(MainActivity.EXT_PHOTO_URL, medicineData[i].photoUrl)
                                    }

                                    startActivity(detailsIntent)
                                }
                            }
                        }
                    }
                }
            })
        }

        val showMoreButton = findViewById<Button>(R.id.showMoreButton)

        showMoreButton.setOnClickListener {
            show += 10

            val medicineListSend : ArrayList<Medicine>

            if(medicineData.size < show) {
                medicineListSend = medicineData
            }
            else{
                medicineListSend = medicineData.subList(0, (show - 1)).toCollection(ArrayList())
            }

            val gridViewAdapter = GridViewAdapter(this@SearchActivity, medicineListSend)
            val gridView = findViewById<GridView>(R.id.gridViewSearch)

            gridView.adapter = gridViewAdapter
            gridView.setOnItemClickListener { adapterView, view, i, l ->
                val detailsIntent = Intent(applicationContext, MedicineDetailsActivity::class.java).apply{
                    putExtra(MainActivity.EXT_MEDICINE_NAME, medicineData[i].name)
                    putExtra(
                        MainActivity.EXT_NEED_PRESCRIPTION,
                        medicineData[i].needPrescription
                    )
                    putExtra(MainActivity.EXT_DESCRIPTION, medicineData[i].description)
                    putExtra(
                        MainActivity.EXT_INDICATION_USAGE,
                        medicineData[i].indicationsAndUsage
                    )
                    putExtra(
                        MainActivity.EXT_STORAGE_HANDLING,
                        medicineData[i].storageAndHandling
                    )
                    putExtra(MainActivity.EXT_MANUFACTURER, medicineData[i].manufacturerName)
                    putExtra(MainActivity.EXT_ROUTE, medicineData[i].route)
                    putExtra(MainActivity.EXT_SUBSTANCES, medicineData[i].substancesName)
                    putExtra(MainActivity.EXT_PHOTO_URL, medicineData[i].photoUrl)
                }

                startActivity(detailsIntent)
            }
        }
    }
}