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
                .updateCategory(category.cat_id, category.cat_name)
        )
    }

    fun blockUser(blockUser: BlockUser) {
        updateMetaData(
            RetrofitClient().oshoppingApi
                .blockUser(blockUser.user_id, blockUser.block, blockUser.admin_id)
        )
    }

    fun hideProduct(product: HideProduct) {
        updateMetaData(
            RetrofitClient().oshoppingApi
                .hideProduct(product.product_id,product.hide,product.user_id)
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


    fun updateCart(cart: Cart) {
        //  updateMetaData( RetrofitClient().oshoppingApi
        //  .updateCart(cart.cart_id,
        //   cart.fk_product_id,
        //   cart.fk_user_id,
        //  cart.cart_statuse))
    }

    fun updateProduct(p: ProductDetails) {
        val updateProductRequest: Call<DefaultResponse> = RetrofitClient().oshoppingApi.updateProduct(
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