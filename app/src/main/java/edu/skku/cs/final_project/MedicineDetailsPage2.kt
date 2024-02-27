package edu.skku.cs.final_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MedicineDetailsPage2 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val generatedView: View = inflater.inflate(R.layout.medicine_details_page2, container, false)

        val needPrescriptionTextView = generatedView.findViewById<View>(R.id.prescriptionTextView) as TextView
        val storageAndHandlingTextView = generatedView.findViewById<View>(R.id.storageTextView) as TextView
        val manufacturerNameTextView = generatedView.findViewById<View>(R.id.manufacturerTextView) as TextView
        val routeTextView = generatedView.findViewById<View>(R.id.routeTextView) as TextView
        val substancesNameTextView = generatedView.findViewById<View>(R.id.substancesNameTextView) as TextView

        val bundle = arguments

        val needPrescription = bundle!!.getString(MainActivity.EXT_NEED_PRESCRIPTION)
        val storageAndHandling = bundle.getString(MainActivity.EXT_STORAGE_HANDLING)
        val manufacturerName = bundle.getString(MainActivity.EXT_MANUFACTURER)
        val route = bundle.getString(MainActivity.EXT_ROUTE)
        val substancesName = bundle.getStringArrayList(MainActivity.EXT_SUBSTANCES)


        needPrescriptionTextView.text = needPrescription
        storageAndHandlingTextView.text = storageAndHandling
        manufacturerNameTextView.text = manufacturerName
        routeTextView.text = route

        var substances = ""

        for(i in 0 until substancesName!!.size){
            if(i == (substancesName.size - 1)){
                substances += "- ${substancesName.get(i)}"
            }
            else{
                substances += "- ${substancesName.get(i)}\n"
            }
        }

        substancesNameTextView.text = substances

        return generatedView
    }
}