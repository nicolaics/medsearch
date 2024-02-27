package edu.skku.cs.final_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class GridViewAdapter(val context: Context, val medicineList : ArrayList<Medicine>) : BaseAdapter() {
    override fun getCount(): Int {
        return medicineList.size
    }

    override fun getItem(p0: Int): Any {
        return medicineList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val generatedView = inflater.inflate(R.layout.grid_cell, null)

        val medicineImageView = generatedView.findViewById<ImageView>(R.id.medicineImageView)
        val medicineNameTextView = generatedView.findViewById<TextView>(R.id.medicineNameTextView)

        medicineNameTextView.setText(medicineList.get(p0).name)

        val apiKeySerp = "4ebf61152628510bc8461d471b09e08905d13ca8e946738873d7372221e6c268"
//        val apiKeySerp = "e48904edd8f5f4a9a55f745d1f20ce0f2cb99170871857adc7995b0702ba3593"
        val pictureHost = "https://serpapi.com/search.json?api_key=$apiKeySerp&engine=google_images&ijn=0&q="

        val photoClient = OkHttpClient()
        val photoReq = Request.Builder().url(pictureHost + medicineList.get(p0).name).build()

        photoClient.newCall(photoReq).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val str = response.body!!.string()

                    val photoData = Gson().fromJson(str, PhotoResult::class.java)

                    val photoUrl = photoData.images_results[0].original

                    medicineList.get(p0).photoUrl = photoUrl

                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(context).load(photoUrl).into(medicineImageView)
                    }
                }
            }
        })

        return generatedView
    }
}