package com.yemen.oshopping.retrofit

import android.util.Log
import com.yemen.oshopping.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateData {

    fun updateReport(report: Report) {
        updateMetaData(
            RetrofitClient().oshoppingApi
                .updateReport(report.report_id, report.report_name)
        )
    }

    fun updateCategory(category: Category) {
        updateMetaData(
            RetrofitClient().oshoppingApi
                .updateCategory(category.cat_id, category.cat_name, category.category_image)
        )
    }

    fun blockUser(blockUser: BlockUser) {
        updateMetaData(
            RetrofitClient().oshoppingApi
                .blockUser(
                    blockUser.user_id,
                    blockUser.block,
                    blockUser.admin_id,
                    blockUser.checked
                )
        )
    }

    fun hideProduct(product: HideProduct) {
        Log.d("TTT", "hideProduct:$product ")
        updateMetaData(
            RetrofitClient().oshoppingApi
                .hideProduct(product.product_id, product.hide, product.user_id, product.checked)
        )
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
                Log.e("updateUser", "Failed to update user", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("updateUser", "Update updated successfully")

            }
        })
    }




    fun updateProduct(p: ProductDetails) {
        val updateProductRequest: Call<DefaultResponse> =
            RetrofitClient().oshoppingApi.updateProduct(
                p.product_id,
                p.product_name,
                p.yrial_price,
                p.dollar_price,
                p.vendor_id,
                p.cat_id,
                p.product_details,
                p.product_img,
                // p.product_date,
                p.product_quantity,
                p.product_discount,
                p.color
            )

        updateProductRequest.enqueue(object : Callback<DefaultResponse> {

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Log.d("updateProduct", "on Failed, Failed to update Product", t)

            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                Log.d("updateProduct", " on response Product updated successfully")

            }
        })


    }


}