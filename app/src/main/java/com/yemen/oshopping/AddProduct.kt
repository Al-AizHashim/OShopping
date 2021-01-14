package com.yemen.oshopping

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.model.ProductDetails
import com.yemen.oshopping.uploadImage.UploadImageActivity
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.File
import java.util.*

class AddProduct : AppCompatActivity(), View.OnClickListener {
    private var counter: Int = 0
    lateinit var productNameET: EditText
    lateinit var productDetailsEditText: EditText
    lateinit var productDollarPriceEditText: EditText
    lateinit var productYeRialPriceEditText: EditText
    lateinit var productQuantityEditText: TextView
    private lateinit var addProductBtn: Button
    private lateinit var productDetails: String
    private lateinit var productName: String
    private var productPriceDollar: Double = 0.0
    private var productPriceRial: Double = 0.0
    private lateinit var buttonImage: Button
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    lateinit var add_product: ProductDetails
    lateinit var oshoppingViewModel: OshoppingViewModel
    private var mUri: Uri? = null
    lateinit var inttent :Intent
    private lateinit var imagename:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        productNameET = findViewById(R.id.name_product_et)
        productDetailsEditText = findViewById(R.id.DetailsProduct)
        productDollarPriceEditText = findViewById(R.id.PriceInDolar)
        productYeRialPriceEditText = findViewById(R.id.PriceInRial)
        productQuantityEditText = findViewById(R.id.Quantity)
        addProductBtn = findViewById(R.id.addProduct)
        minus.setOnClickListener(this)
        plus.setOnClickListener(this)
        buttonImage = findViewById(R.id.addImage)
        buttonImage.setOnClickListener {
            //  showDialog("Choose Image")
            inttent=Intent(this, UploadImageActivity::class.java)
            startActivityForResult(inttent,919)

        }
        addProductBtn.setOnClickListener {
            val product = ProductDetails(
                product_name = productNameET.text.toString(),
                product_details = productDetailsEditText.text.toString(),
                dollar_price = productDollarPriceEditText.text.toString().toDouble(),
                yrial_price = productYeRialPriceEditText.text.toString().toDouble(),
                product_quantity = productQuantityEditText.text.toString().toInt(),
                vendor_id = 1,
                cat_id = 1,
                product_img = imagename,
                product_discount = 0
            )
            oshoppingViewModel.pushProduct(product)

        }
        supportActionBar?.setTitle(R.string.AddNewProduct)


    }

    override fun onClick(v: View?) {
        val i = v!!.id

        if (i == R.id.minus) {
            if (counter != 0)
                counter--
            Quantity.text = counter.toString()

        } else if (i == R.id.plus) {

            counter++
            Quantity.text = counter.toString()

        }
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val gallery = dialog.findViewById(R.id.btnChoose) as Button

        val capture = dialog.findViewById(R.id.btnCapture) as Button
        val noBtn = dialog.findViewById(R.id.close) as Button
        capture.setOnClickListener { capturePhoto() }
        gallery.setOnClickListener {
            //check permission at runtime
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            } else {
                openGallery()
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }


    private fun show(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun capturePhoto() {
        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
        if (capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if (Build.VERSION.SDK_INT >= 24) {

            /* FileProvider.getUriForFile(this, "info.camposha.kimagepicker.fileprovider",
                 capturedImage)*/
            FileProvider.getUriForFile(
                Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", capturedImage
            );
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    private fun renderImage(imagePath: String?) {
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            mImageView?.setImageBitmap(bitmap)

        } else {
            show("ImagePath is null")
        }
    }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selsetion
                )
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse(
                        "content://downloads/public_downloads"
                    ), java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri.path
        }
        renderImage(imagePath)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>
        , grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(mUri)
                    )
                    mImageView!!.setImageBitmap(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                    }
                }
            919 ->{
                if (data != null) {
                    imagename=data.getStringExtra("image_name")
                }
                Toast.makeText(applicationContext,"the image name is : $imagename",Toast.LENGTH_LONG).show()

            }
        }
    }
}