package edu.skku.cs.final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MedicineDetailsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_details)

        val description = intent.getStringExtra(MainActivity.EXT_DESCRIPTION)
        val indicationsAndUsage = intent.getStringExtra(MainActivity.EXT_INDICATION_USAGE)
        val medicineName = intent.getStringExtra(MainActivity.EXT_MEDICINE_NAME)
        val needPrescription = intent.getStringExtra(MainActivity.EXT_NEED_PRESCRIPTION)
        val storageAndHandling = intent.getStringExtra(MainActivity.EXT_STORAGE_HANDLING)
        val manufacturerName = intent.getStringExtra(MainActivity.EXT_MANUFACTURER)
        val route = intent.getStringExtra(MainActivity.EXT_ROUTE)
        val substancesName = intent.getStringArrayListExtra(MainActivity.EXT_SUBSTANCES)
            ?.toCollection(ArrayList())
        val photoUrl = intent.getStringExtra(MainActivity.EXT_PHOTO_URL)

        val medicineNameTextView = findViewById<TextView>(R.id.medicineNameTextViewDetails)
        val medicineImageView = findViewById<ImageView>(R.id.medicineImageViewDetails)

        val reportButton = findViewById<Button>(R.id.reportButton)
        val pageOneButton = findViewById<Button>(R.id.pageOneButton)
        val pageTwoButton = findViewById<Button>(R.id.pageTwoButton)
        val backButton = findViewById<Button>(R.id.backButton)

        medicineNameTextView.text = medicineName!!.uppercase()
        Glide.with(applicationContext).load(photoUrl).into(medicineImageView)

        val fragmentManagerDefault = supportFragmentManager
        val fragmentTransactionDefault = fragmentManagerDefault.beginTransaction()
        val fragmentDefault = MedicineDetailsPage1()
        val bundleDefault = Bundle()

        bundleDefault.putString(MainActivity.EXT_DESCRIPTION, description)
        bundleDefault.putString(MainActivity.EXT_INDICATION_USAGE, indicationsAndUsage)

        fragmentDefault.arguments = bundleDefault
        fragmentTransactionDefault.addToBackStack(null)
        fragmentTransactionDefault.replace(R.id.fragmentContainerView, fragmentDefault).commit()

        reportButton.setOnClickListener {
            val reportIntent = Intent(applicationContext, ReportActivity::class.java)
            startActivity(reportIntent)
        }

        pageOneButton.setOnClickListener {
            val fragmentManagerOne = supportFragmentManager
            val fragmentTransactionOne = fragmentManagerOne.beginTransaction()
            val fragmentOne = MedicineDetailsPage1()
            val bundleOne = Bundle()

            bundleOne.putString(MainActivity.EXT_DESCRIPTION, description)
            bundleOne.putString(MainActivity.EXT_INDICATION_USAGE, indicationsAndUsage)

            fragmentOne.arguments = bundleOne
            fragmentTransactionOne.addToBackStack(null)
            fragmentTransactionOne.replace(R.id.fragmentContainerView, fragmentOne).commit()
        }

        pageTwoButton.setOnClickListener {
            val fragmentManagerTwo = supportFragmentManager
            val fragmentTransactionTwo = fragmentManagerTwo.beginTransaction()
            val fragmentPageTwo = MedicineDetailsPage2()
            val bundle = Bundle()

            bundle.putStringArrayList(MainActivity.EXT_SUBSTANCES, substancesName)
            bundle.putString(MainActivity.EXT_STORAGE_HANDLING, storageAndHandling)
            bundle.putString(MainActivity.EXT_NEED_PRESCRIPTION, needPrescription)
            bundle.putString(MainActivity.EXT_ROUTE, route)
            bundle.putString(MainActivity.EXT_MANUFACTURER, manufacturerName)

            fragmentPageTwo.arguments = bundle

            fragmentTransactionTwo.addToBackStack(null)
            fragmentTransactionTwo.replace(R.id.fragmentContainerView, fragmentPageTwo).commit()
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}