package com.yemen.oshopping.retrofit

import android.util.Log
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.model.DefaultResponse
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateData {

    fun updateReport(report: Report){
        updateMetaData( RetrofitClient().oshoppingApi
            .updateReport(report.report_id, report.report_name))
    }
    fun updateCategory(category: Category){
        updateMetaData( RetrofitClient().oshoppingApi
            .updateCategory(category.cat_id, category.cat_name))
    }



    fun updateMetaData(updateRequest: Call<DefaultResponse>) {

        updateRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("updateItem", "Failed to update Item", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("updateItem", "Item updated successfully ${response.message()}")

            }
        })
    }

    fun updateUser(user: User) {
        val updateUserRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi
            .updateUser(
                user.user_id,
                user.first_name,
                user.last_name,
                user.phone_number,
                user.details,
                user.address
            )

        updateUserRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("updateCategory", "Failed to update Category", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("updateCategory", "Category updated successfully")

            }
        })
    }
}