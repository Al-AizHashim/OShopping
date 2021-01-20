package com.yemen.oshopping.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yemen.oshopping.api.*
import com.yemen.oshopping.model.Cart
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.model.Report
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val TAG = "fetchProduct"

class FetchData {


    fun fetchProduct(): LiveData<List<ProductItem>> {
        return fetchProductMetaData(RetrofitClient().oshoppingApi.fetchProduct())
    }
    fun fetchProductById(product_id: Int): LiveData<List<ProductItem>> {
        return fetchProductMetaData(RetrofitClient().oshoppingApi.fetchProductById(product_id))
    }
/*
    fun fetchProductById2(product_id: Int): LiveData<ProductItem> {
        val responseLiveData: MutableLiveData<ProductItem> = MutableLiveData()
        val NewsRequest =
            RetrofitClient().oshoppingApi.fetchProductById(product_id)
        NewsRequest.enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<SingleProductResponse>, t: Throwable) {
                Log.e("fetch Product details", "Failed to fetch product details", t)
            }

            override fun onResponse(
                call: Call<SingleProductResponse>,
                response: Response<SingleProductResponse>
            ) {
                Log.d(TAG, "Response received successfully")

                val singleProductResponse: SingleProductResponse? = response.body()
                val productItem: ProductItem? = singleProductResponse?.productItem
                responseLiveData.value = productItem

            }
        })

        return responseLiveData
    }

 */

    fun fetchCart(user_id:Int): LiveData<List<Cart>> {
        val responseLiveData: MutableLiveData<List<Cart>> = MutableLiveData()
        val NewsRequest =
            RetrofitClient().oshoppingApi.fetchCart(user_id)
        NewsRequest.enqueue(object : Callback<CartResponse> {
            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Log.e("fetch cart ", "Failed to fetch cart", t)
            }

            override fun onResponse(
                call: Call<CartResponse>,
                response: Response<CartResponse>
            ) {
                Log.d(TAG, "Response received successfully")

                val cartResponse: CartResponse? = response.body()
                val cart: List<Cart> = cartResponse?.cartItem ?: mutableListOf()
                responseLiveData.value = cart

            }
        })

        return responseLiveData
    }


/*
    fun fetchCategory(): LiveData<List<Category>> {
        val responseLiveData: MutableLiveData<List<Category>> = MutableLiveData()
        var categoryRequest: Call<CategoryResponse> = RetrofitClient().oshoppingApi.fetchCategory()
        categoryRequest.enqueue(object : Callback<CategoryResponse> {

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch Product", t)
            }

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                Log.d(TAG, "Response received successfully")
                val categoryResponse: CategoryResponse? = response.body()
                val categoryItems: List<Category> = categoryResponse?.categoryItem
                    ?: mutableListOf()

                responseLiveData.value = categoryItems
            }
    })
        return responseLiveData
}
 */


    fun fetchProductByCategory(category_id: Int): LiveData<List<ProductItem>> {
        return fetchProductMetaData(RetrofitClient().oshoppingApi.fetchProductByCategory(category_id))
    }
    fun fetchProductByVendorId(vendorId: Int): LiveData<List<ProductItem>> {
        return fetchProductMetaData(RetrofitClient().oshoppingApi.fetchProductByVendorId(vendorId))
    }

    fun searchProduct(query: String): LiveData<List<ProductItem>> {
        return fetchProductMetaData(RetrofitClient().oshoppingApi.searchProduct(query))
    }

    fun fetchProductMetaData(productRequest: Call<ProductResponse>): LiveData<List<ProductItem>> {

        val responseLiveData: MutableLiveData<List<ProductItem>> = MutableLiveData()

        productRequest.enqueue(object : Callback<ProductResponse> {

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch Product", t)
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                Log.d(TAG, "Response received successfully")
                val productResponse: ProductResponse? = response.body()
                val productItems: List<ProductItem> = productResponse?.productItem
                    ?: mutableListOf()
                responseLiveData.value = productItems
                Log.d("onResponse", "onResponse: $productItems")
            }
        })

        return responseLiveData
    }


    fun fetchCategory(): LiveData<List<Category>> {
        val responseLiveData: MutableLiveData<List<Category>> = MutableLiveData()
        var categoryRequest: Call<CategoryResponse> = RetrofitClient().oshoppingApi.fetchCategory()
        categoryRequest.enqueue(object : Callback<CategoryResponse> {

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch Category", t)
            }

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                Log.d(TAG, "Response received successfully")
                val categoryResponse: CategoryResponse? = response.body()
                val categoryItems: List<Category> = categoryResponse?.categoryItem
                    ?: mutableListOf()
                responseLiveData.value = categoryItems
            }
        })

        return responseLiveData
    }

    fun fetchReport(): LiveData<List<Report>> {
        val responseLiveData: MutableLiveData<List<Report>> = MutableLiveData()
        val reportRequest: Call<ReportResponse> = RetrofitClient().oshoppingApi.fetchReport()
        reportRequest.enqueue(object : Callback<ReportResponse> {

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                Log.d("fetchReport", "Failed to fetch Reports", t)
            }

            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                Log.d("fetchReport", "Reports received successfully")
                val reportResponse: ReportResponse? = response.body()
                val reportItems: List<Report> = reportResponse?.reportItem
                    ?: mutableListOf()
                responseLiveData.value = reportItems
            }
        })

        return responseLiveData
    }


}