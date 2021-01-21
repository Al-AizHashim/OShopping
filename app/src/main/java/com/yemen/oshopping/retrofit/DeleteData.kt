package com.yemen.oshopping.retrofit

import android.app.Application
import android.util.Log
import com.yemen.oshopping.model.Cart
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.model.DefaultResponse
import com.yemen.oshopping.model.Report
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteData {


    fun deleteCategory(category: Category){
        return deleteMetaData(RetrofitClient().oshoppingApi.deleteCategory(category.cat_id))
    }

    fun deleteReport(report: Report) {
        return deleteMetaData(RetrofitClient().oshoppingApi.deleteReport(report.report_id))
    }

    fun deleteMetaData(deleteRequest: Call<DefaultResponse>){

        deleteRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("deleteData", "Failed to delete item", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("deleteData", "Item deleted successfully")

            }
        })

    }


    fun deleteCart(cart: Cart) {
       // return deleteMetaData(RetrofitClient().oshoppingApi.deleteCart(cart.cart_id))
    }

}