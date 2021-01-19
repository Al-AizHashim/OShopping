package com.yemen.oshopping.retrofit

import android.util.Log
import com.yemen.oshopping.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PushData {
    fun pushCategory(category: Category) {
        val pushCategoryRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi
            .postCategory(category.cat_name)

        pushCategoryRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("pushCategory", "Failed to push Category", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("pushCategory", "Category pushed successfully")

            }
        })


    }

    fun pushReport(report: Report) {
        val pushReportRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi
            .postReport(report.report_name)

        pushReportRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("pushReport", "Failed to push Report", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("pushReport", "Report pushed successfully")

            }
        })


    }

    fun pushRating(rating: Rating) {
        val pushRatingRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi
            .pushRating(
                rating.product_id,
                rating.user_id, rating.rating
            )

        pushRatingRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.e("pushReport", "Failed to push rating", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("pushReport", "rating pushed successfully")

            }
        })


    }

    fun pushProduct(p: ProductDetails) {
        val pushProductRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi.pushProduct(
            p.product_id,
            p.product_name,
            p.yrial_price,
            p.dollar_price,
            p.vendor_id,
            p.cat_id,
            p.product_details,
            p.product_img,
            p.product_date,
            p.product_quantity,
            p.product_discount
        )

        pushProductRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.d("pushProduct", "Failed to push Product", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("pushProduct", "Product pushed successfully")

            }
        })


    }

    fun pushUser(user: User) {
        Log.d("pushUser", "pushUser:$user ")
        val pushUserRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi.
        pushUser(
            user.first_name,
            user.last_name,
            user.email,
            user.phone_number,
            user.details,
            user.address,
            "default image link"
        )

        pushUserRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.d("pushUser", "Failed to push User", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("pushUser", "User pushed successfully")

            }
        })


    }

}