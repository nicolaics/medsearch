package edu.skku.cs.final_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MedicineDetailsPage1 : Fragment() {
    private lateinit var descriptionTextView: TextView
    private lateinit var indicationsAndUsageTextView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val generatedView: View = inflater.inflate(R.layout.medicine_details_page1, container, false)

        descriptionTextView = generatedView.findViewById<View>(R.id.descriptionTextView) as TextView
        indicationsAndUsageTextView = generatedView.findViewById<View>(R.id.indicationsTextView) as TextView

        val bundle = arguments
        val description = bundle!!.getString(MainActivity.EXT_DESCRIPTION)
        val indicationsAndUsage = bundle!!.getString(MainActivity.EXT_INDICATION_USAGE)

        descriptionTextView.text = description
        indicationsAndUsageTextView.text = indicationsAndUsage

        return generatedView
    }
}