package com.yemen.oshopping.api


import com.yemen.oshopping.model.DefaultResponse
import com.yemen.oshopping.model.ProductReportsDetailsF
import retrofit2.Call
import retrofit2.http.*


interface OshoppingApi {
    //post
    @FormUrlEncoded
    @POST("oshopping_api/api/category_api.php")
    fun postCategory(
        @Field("cat_name") cat_name: String,
        @Field("category_image") category_image: String?): Call<DefaultResponse>
    @FormUrlEncoded
    @POST("oshopping_api/api/report_api.php")
    fun postReport(@Field("report_name") report_name: String): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/product_api.php")
    fun pushProduct(
        @Field("product_id") product_id: Int?,
        @Field("product_name") product_name: String,
        @Field("yrial_price") yrial_price: Double,
        @Field("dollar_price") dollar_price: Double,
        @Field("vendor_id") vendor_id: Int,
        @Field("cat_id") cat_id: Int,
        @Field("product_details") product_details: String,
        @Field("product_img") product_img: String?,
        @Field("product_date") product_date: String?,
        @Field("product_quantity") product_quantity: Int,
        @Field("product_discount") product_discount: Int,
        @Field("color") color: String

    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/user_api.php")
    fun pushUser(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("phone_number") phone_number: String?,
        @Field("details") details: String?,
        @Field("address") address: String?,
        @Field("image") image: String?
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/report_details_api.php")
    fun pushReportDetails(
        @Field("report_id") report_id: Int?,
        @Field("sender_id") sender_id: Int,
        @Field("against_id") against_id: Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/product_report_details_api.php")
    fun pushProductReportDetails(
        @Field("product_id") product_id: Int?,
        @Field("product_r_id") product_r_id: Int,
        @Field("sender_id") sender_id: Int
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("oshopping_api/api/rating_api.php")
    fun pushRating(
        @Field("product_id") product_id: Int,
        @Field("user_id") user_id: Int,
        @Field("rating") rating: Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/cart_api.php")
    fun pushCart(
        @Field("cart_id") cart_id: Int?,
        @Field("fk_product_id") product_id: Int,
        @Field("fk_user_id") user_id: Int,
        @Field("cart_statuse") cart_statuse: Int,
        @Field("product_name") product_name: String,
        @Field("yrial_price") yrial_price: Double,
        @Field("dollar_price") dollar_price: Double,
        @Field("vendor_id") vendor_id: Int,
        @Field("cat_id") cat_id: Int,
        @Field("product_details") product_details: String?,
        @Field("product_img") product_img: String?,
        @Field("product_date") product_date: String?,
        @Field("product_quantity") product_quantity: Int,
        @Field("product_discount") product_discount: Int,
        @Field("color") color: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("oshopping_api/api/activity_api.php")
    fun pushActivity(
        @Field("productId") fk_product_id: Int?,
        @Field("productName") product_name: String,
        @Field("yrial_price") yrial_price: Int,
        @Field("dollar_price") dollar_price: Int,
        @Field("quantity") quantity: Int,
        @Field("totalPrice") total_price: Double,
        @Field("activityType") activity_type: String
    ): Call<DefaultResponse>

    //get
    @GET("oshopping_api/api/product_api.php")
    fun fetchProduct(): Call<ProductResponse>

    @GET("oshopping_api/api/report_details_api.php")
    fun fetchReportsDetails(): Call<ReportsDetailsResponce>



    @GET("oshopping_api/api/user_api.php")
    fun fetchUser(): Call<UserResponse>


    @GET("oshopping_api/api/product_api.php")
    fun fetchProductByCategory(@Query("cat_id") category_id: Int): Call<ProductResponse>
    @GET("oshopping_api/api/product_api.php")
    fun fetchProductByVendorId(@Query("vendor_id") vendor_id: Int): Call<ProductResponse>
    @GET("/oshopping_api/api/product_api.php")
    fun fetchProductById(@Query("product_id") product_id: Int): Call<ProductResponse>

    @GET("oshopping_api/api/report_details_api.php")
    fun fetchReportDetailsByUserId(@Query("against") against: Int): Call<ReportDetailsResponce>

    @GET("oshopping_api/api/product_report_details_api.php")
    fun fetchProductReportByProductId(@Query("product_id") product_id: Int): Call<ProductReportDetailsResponse>

    //fun fetchProductById(@Query("product_id") product_id: Int): Call<SingleProductResponse>
    @GET("oshopping_api/api/user_api.php")
    fun fetchUserById(@Query("user_id") user_id: Int): Call<SingleUserResponse>

    @GET("oshopping_api/api/product_api.php")
    fun searchProduct(@Query("query") query: String): Call<ProductResponse>

    @GET("oshopping_api/api/product_api.php")
    fun fetchProductByColor(@Query("color") color: String): Call<ProductResponse>

    @GET("oshopping_api/api/product_report_details_api.php")
    fun fetchCategory(): Call<CategoryResponse>

    @GET("oshopping_api/api/product_report_details_api.php")
    fun fetchProductReportsDetails(): Call<ProductReportsDetailsResponse>

    @GET("oshopping_api/api/user_api.php")
    fun fetchUsers(): Call<UserResponse>

    @GET("oshopping_api/api/user_api.php")
    fun fetchUser(@Query("user_id") user_id: Int): Call<UserResponse>

    @GET("oshopping_api/api/report_api.php")
    fun fetchReport(): Call<ReportResponse>

    @GET("oshopping_api/api/activity_api.php")
    fun fetchActivities(@Query("fk_user_id") fk_user_id: Int): Call<ActivityResponse>

    @GET("oshopping_api/api/cart_api.php")
    fun fetchCart(@Query("user_id") user_id: Int): Call<CartResponse>

    @GET("oshopping_api/api/user_api.php")
    fun fetchUserByEmail(@Query("email") email: String): Call<UserResponse>




    //put
    @FormUrlEncoded
    @PUT("oshopping_api/api/category_api.php")
    fun updateCategory(
        @Field("cat_id") cat_id: Int?,
        @Field("cat_name") cat_name: String,
        @Field("category_image") category_image: String?
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("oshopping_api/api/report_api.php")
    fun updateReport(
        @Field("report_id") report_id: Int?, @Field("report_name") report_name: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("oshopping_api/api/user_api.php")
    fun BlockUser(
        @Field("user_id") user_id: Boolean?, @Field("block") block: Boolean
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @PUT("oshopping_api/api/user_api.php")
    fun updateUser(
        @Field("user_id") user_id: Int?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("phone_number") phone_number: String?,
        @Field("details") details: String?,
        @Field("address") address: String?
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @PUT("oshopping_api/api/user_api.php")
    fun blockUser(
        @Field("user_id") user_id: Int, @Field("block") block: Int,
        @Field("admin_id") admin_id: Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("oshopping_api/api/product_api.php")
    fun hideProduct(
        @Field("product_id") product_id: Int, @Field("hide") hide: Int,
        @Field("user_id") user_id: Int
    ): Call<DefaultResponse>


    @DELETE("oshopping_api/api/category_api.php")
    fun deleteCategory(@Query("cat_id") cat_id: Int?
    ): Call<DefaultResponse>

    @DELETE("oshopping_api/api/report_api.php")
    fun deleteReport(@Query("report_id") report_id: Int?
    ): Call<DefaultResponse>

    @DELETE("oshopping_api/api/cart_api.php")
    fun deleteCart(@Query("cart_id") cart_id: Int?
    ): Call<DefaultResponse>



    @FormUrlEncoded
    @PUT("oshopping_api/api/product_api.php")
    fun updateProduct(
        @Field("product_id") product_id: Int?,
        @Field("product_name") product_name: String,
        @Field("yrial_price") yrial_price: Double,
        @Field("dollar_price") dollar_price: Double,
        @Field("vendor_id") vendor_id: Int,
        @Field("cat_id") cat_id: Int,
        @Field("product_details") product_details: String,
        @Field("product_img") product_img: String?,
       // @Field("product_date") product_date: String?,
        @Field("product_quantity") product_quantity: Int,
        @Field("product_discount") product_discount: Int,
        @Field("color") color: String

    ): Call<DefaultResponse>

}