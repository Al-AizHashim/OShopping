package com.yemen.oshopping.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yemen.oshopping.model.*
import com.yemen.oshopping.retrofit.DeleteData
import com.yemen.oshopping.retrofit.FetchData
import com.yemen.oshopping.retrofit.PushData
import com.yemen.oshopping.retrofit.UpdateData
import com.yemen.oshopping.sharedPreferences.UserSharedPreferences


class OshoppingViewModel(private val app: Application) : AndroidViewModel(app) {

    var productItemLiveData: LiveData<List<ProductItem>>
    var categoryItemLiveData: LiveData<List<Category>>
    var productLiveData = MutableLiveData<Int>()
    var productReportLiveData = MutableLiveData<Int>()
    var userLiveDataById = MutableLiveData<Int>()
    var userLiveDataByBlook = MutableLiveData<Int?>()
    var reportDetailsLiveData = MutableLiveData<List<Int?>>()
    var reportDetailsLiveData2 = MutableLiveData<Int>()
    val mutableSearchTerm = MutableLiveData<String>()
    val activityLiveData = MutableLiveData<Int>()
    var cartLiveData = MutableLiveData<Int>()
    var reportItemLiveData: LiveData<List<Report>>
    val userLiveData = MutableLiveData<String>()
    val searchLiveData: LiveData<List<ProductItem>>
    var productColorLiveData = MutableLiveData<String>()
    val searchTerm: String get() = mutableSearchTerm.value ?: ""

    init {
        productItemLiveData = FetchData().fetchProduct()
        categoryItemLiveData = FetchData().fetchCategory()
        reportItemLiveData = FetchData().fetchReport()
        mutableSearchTerm.value = getQuery()
        searchLiveData =
            Transformations.switchMap(mutableSearchTerm) { searchTerm ->
                if (searchTerm.isBlank()) {
                    FetchData().fetchProduct()
                } else {
                    FetchData().searchProduct(searchTerm)
                }
            }
        // cartLiveData=FetchData().fetchCart()
    }

    //fetch data
    var productItemLiveDataByCategory: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { category_id ->
            FetchData().fetchProductByCategory(category_id)
        }
    var userItemLiveData: LiveData<List<User>> =
        Transformations.switchMap(userLiveDataByBlook) { block ->
            FetchData().fetchUsers(block!!)
        }
    var reportsDetailsLiveData: LiveData<List<ReportsDetails>> =
        Transformations.switchMap(reportDetailsLiveData) {
            FetchData().fetchReportsDetails(it[0]!!, it[1]!!)
        }
    var ProductReportsDetailsLiveData: LiveData<List<ProductReportsDetailsF>> =
        Transformations.switchMap(reportDetailsLiveData) {
            FetchData().fetchProductReportsDetails(it[0]!!, it[1]!!)
        }
    var activityItemLiveData: LiveData<List<ActivityItem>> =
        Transformations.switchMap(activityLiveData) { user_id ->
            FetchData().fetchActivity(user_id)
        }
    var cartItemLiveData: LiveData<List<Cart>> =
        Transformations.switchMap(cartLiveData) { user_id ->
            FetchData().fetchCart(user_id)
        }
    var productItemLiveDataByID: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { product_id ->
            FetchData().fetchProductById(product_id)
        }
    var userLiveDataByID: LiveData<User> =
        Transformations.switchMap(userLiveDataById) { user_id ->
            FetchData().fetchUserById(user_id)
        }
    var fetchReportDetailsByUserId: LiveData<List<ReportDetails>> =
        Transformations.switchMap(reportDetailsLiveData2) { against ->
            FetchData().fetchReportDetailsByUserId(against)
        }
    var fetchProductReportLiveDataByProductId: LiveData<List<ProductReportDetailsF>> =
        Transformations.switchMap(productReportLiveData) { product_id ->
            FetchData().fetchProductReportDetailsByProductId(product_id)
        }
    var productItemLiveDataByVendorID: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { vendor_id ->
            FetchData().fetchProductByVendorId(vendor_id)
        }
    var productItemLiveDataByHide: LiveData<List<ProductItem>> =
        Transformations.switchMap(productLiveData) { hide ->
            FetchData().fetchProductByHide(hide)
        }
    var productItemLiveDataByColor: LiveData<List<ProductItem>> =
        Transformations.switchMap(productColorLiveData) { color ->
            FetchData().fetchProductByColor(color)
        }

    var userItemLiveDataByEmail: LiveData<List<User>> =
        Transformations.switchMap(userLiveData) { email ->
            FetchData().fetchUserByEmail(email)
        }

    fun loadCart(user_id: Int) {
        cartLiveData.value = user_id
    }

    fun loadProductByCategory(category_id: Int) {
        productLiveData.value = category_id
    }

    fun loadProductByColor(color: String) {
        productColorLiveData.value = color
    }

    fun loadActivities(user_id: Int) {
        activityLiveData.value = user_id
    }

    fun getUserByEmail(email: String) {
        userLiveData.value = email
    }

    fun getProductByVendorId(vendor_id: Int) {
        productLiveData.value = vendor_id
    }

    fun getProductByHide(hide: Int) {
        productLiveData.value = hide
    }

    fun getProductById(product_id: Int) {
        productLiveData.value = product_id
    }

    fun getProductReportByReportId(product_id: Int) {
        productReportLiveData.value = product_id
    }

    fun getReportDetailsByUserId(against: Int) {
        reportDetailsLiveData2.value = against
    }

    fun getReportsDetails(reportList: List<Int?>) {
        reportDetailsLiveData.value = reportList
    }

    fun getProductReportsDetails(reportList: List<Int?>) {
        reportDetailsLiveData.value = reportList
    }

    fun getUserById(user_id: Int) {
        userLiveDataById.value = user_id
    }

    fun getUserByBlock(block: Int?) {
        userLiveDataByBlook.value = block
    }

    fun search(query: String) {
        setQuery(query)
        mutableSearchTerm.value = query
    }

    // push data
    fun pushcat(category: Category) {
        PushData().pushCategory(category)
        categoryItemLiveData = FetchData().fetchCategory()
    }

    fun pushProduct(product: ProductDetails) {
        PushData().pushProduct(product)
        productItemLiveData = FetchData().fetchProduct()
    }

    fun pushUser(user: User) = PushData().pushUser(user)
    fun pushCart(cart: Cart) = PushData().pushCart(cart)
    fun pushActivity(activity: ActivityItem) = PushData().pushActivity(activity)
    fun pushReportDetails(reportDetails: PostReportDetails) =
        PushData().pushReportDetails(reportDetails)

    fun pushProductReportDetails(prodcutReportDetails: ProductReportDetails) =
        PushData().pushProductReportDetails(prodcutReportDetails)

    fun pushReport(report: Report) {
        PushData().pushReport(report)
        reportItemLiveData = FetchData().fetchReport()
    }

    fun pushRating(rating: Rating) = PushData().pushRating(rating)


    //update data in database
    fun updateCategory(category: Category) = UpdateData().updateCategory(category)

    fun blockUser(blockUser: BlockUser) {
        UpdateData().blockUser(blockUser)
    }

    fun hideProduct(product: HideProduct) = UpdateData().hideProduct(product)
    fun updateUser(user: User) {
        UpdateData().updateUser(user)
        Log.d("updateUser", "OshoppingViewModel $user")
        userItemLiveData = FetchData().fetchUser()
    }

    fun updateReport(report: Report) {
        UpdateData().updateReport(report)
        reportItemLiveData = FetchData().fetchReport()
    }

    fun updateProduct(product: ProductDetails) {
        UpdateData().updateProduct(product)
        Log.d("updateProduct", "OshoppingViewModel $product")
        productItemLiveData = FetchData().fetchProduct()
    }

    //delete functions
    fun deleteCategory(category: Category) {
        DeleteData().deleteCategory(category)
        categoryItemLiveData = FetchData().fetchCategory()
    }

    fun deleteReport(report: Report) {
        DeleteData().deleteReport(report)
        reportItemLiveData = FetchData().fetchReport()
    }

    fun deleteCart(cart: Cart) {
        DeleteData().deleteCart(cart)
        cartItemLiveData = FetchData().fetchCart(getStoredUserId())
    }


    ////////
    //shared preferences
    fun setUserId(userId: Int = -1) {
        UserSharedPreferences.setStoredUserId(app, userId)
    }

    fun getStoredUserId(): Int {
        return UserSharedPreferences.getStoredUserId(app)
    }

    fun setUserEmail(userEmail: String = "none") {
        UserSharedPreferences.setStoredEmail(app, userEmail)
    }

    fun getStoredEmail(): String? {
        return UserSharedPreferences.getStoredUserEmail(app)
    }

    fun setQuery(query: String = "") {
        UserSharedPreferences.setStoredQuery(app, query)
    }

    fun getQuery(): String? {
        return UserSharedPreferences.getStoredQuery(app)
    }

    fun setUserBlock(block: Int = 0) {
        UserSharedPreferences.setStoredUserBlock(app, block)
    }

    fun getStoredUserBlock(): Int {
        return UserSharedPreferences.getStoredUserBlock(app)
    }

}