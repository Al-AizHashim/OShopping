package com.yemen.oshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yemen.oshopping.model.*

import com.yemen.oshopping.retrofit.DeleteData

import com.yemen.oshopping.retrofit.FetchData
import com.yemen.oshopping.retrofit.PushData
import com.yemen.oshopping.retrofit.UpdateData


class OshoppingViewModel : ViewModel() {

    val productItemLiveData: LiveData<List<ProductItem>>
    val categoryItemLiveData: LiveData<List<Category>>
    var productLiveData = MutableLiveData<Int>()
    val mutableSearchTerm = MutableLiveData<String>()
    val reportItemLiveData: LiveData<List<Report>>

    init {
        productItemLiveData = FetchData().fetchProduct()
        categoryItemLiveData = FetchData().fetchCategory()
        reportItemLiveData=FetchData().fetchReport()
    }

    var productItemLiveDataByCategory: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { category_id ->
            FetchData().fetchProductByCategory(category_id)
        }

    fun loadProductByCategory(category_id: Int) {
        productLiveData.value = category_id
    }

    var productItemLiveDataByID: LiveData<ProductItem> =
        Transformations.switchMap(productLiveData) { product_id ->
            FetchData().fetchProductById(product_id)
        }
    var productItemLiveDataByVendorID: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { vendor_id ->
            FetchData().fetchProductByVendorId (vendor_id)
        }
    fun getProductByVendorId(vendor_id: Int) {
        productLiveData.value = vendor_id
    }

    fun getProductById(product_id: Int) {
        productLiveData.value = product_id
    }

    var searchLiveData: LiveData<List<ProductItem>> =
        Transformations.switchMap(mutableSearchTerm) { query ->
            FetchData().searchProduct(query)
        }

    fun search(query: String) {
        mutableSearchTerm.value = query
    }

    fun pushcat(category: Category) = PushData().pushCategory(category)
    fun pushProduct(product: ProductDetails) = PushData().pushProduct(product)
    fun pushUser(user: User) = PushData().pushUser(user)
    fun pushReport(report: Report) = PushData().pushReport(report)


    //update data in database
    fun updateCategory(category: Category) = UpdateData().updateCategory(category)

    fun updatUser(user: User) = UpdateData().updateUser(user)


    fun deleteCategory(category: Category) = DeleteData().deleteCategory(category)


}