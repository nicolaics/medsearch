package edu.skku.cs.final_project

data class MedicineDataModel (val results : ArrayList<Data>) {
    data class Data(val version : String ?= null,
                    val effective_time : String,
                    val storage_and_handling : ArrayList<String> ?= null,
                    val description : ArrayList<String> ?= null,
                    val indications_and_usage : ArrayList<String> ?= null,
                    val dosage_and_administration : ArrayList<String> ?= null,
                    val dosage_forms_and_strengths : ArrayList<String> ?= null,
                    val openfda : OpenFDA) {}
}

data class PhotoResult(val images_results : ArrayList<PhotoData>){
    data class PhotoData(val position: Int, val original : String){}
}

data class PostUsername(var username : String ?= null, var password : String ?= null)

data class UserStatus(var success : String ?= null)