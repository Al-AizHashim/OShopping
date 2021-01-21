package com.yemen.oshopping.vendor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductDetails
import com.yemen.oshopping.retrofit.TAG
import com.yemen.oshopping.uploadImage.*
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_upload_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream



class AddProductFragment : Fragment() {
    private var selectedImageUri: Uri? = null
    var imageName:String?=null
    private var counter: Int = 0
    lateinit var productNameET: EditText
    lateinit var productDetailsEditText: EditText
    lateinit var productDollarPriceEditText: EditText
    lateinit var productYeRialPriceEditText: EditText
    lateinit var productQuantityEditText: TextView
    lateinit var chosenCategoryTV: TextView
    lateinit var chosenColorTV: TextView
    lateinit var chooseCategoryBtn: Button
    lateinit var chooseColorBtn: Button
    lateinit var addProductBtn: Button
    lateinit var categoryTitle: String
    lateinit var productDiscountET: EditText
    var categoryId: Int = 0
    private lateinit var popupMenu: PopupMenu
    lateinit var buttonImage: Button
    lateinit var oshoppingViewModel: OshoppingViewModel
    private var mUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        productNameET = view.findViewById(R.id.name_product_et)
        productDetailsEditText = view.findViewById(R.id.DetailsProduct)
        productDollarPriceEditText = view.findViewById(R.id.PriceInDolar)
        productYeRialPriceEditText = view.findViewById(R.id.PriceInRial)
        productQuantityEditText = view.findViewById(R.id.Quantity)
        addProductBtn = view.findViewById(R.id.addProduct)
        chosenCategoryTV = view.findViewById(R.id.chosen_category_tv)
        chosenColorTV = view.findViewById(R.id.chosen_color_tv)
        chooseCategoryBtn = view.findViewById(R.id.category_btn)
        chooseColorBtn = view.findViewById(R.id.choose_color_btn)
        productDiscountET = view.findViewById(R.id.discount)
        fillCategoryMenu()

        buttonImage = view.findViewById(R.id.addImage)
        buttonImage.setOnClickListener {
            openImageChooser()

            //  showDialog("Choose Image")
           // inttent = Intent(this.requireContext(), UploadImageActivity::class.java)
           // startActivityForResult(inttent, 919)

        }
        addProductBtn.setOnClickListener {

            val product = ProductDetails(
                product_name = productNameET.text.toString(),
                product_details = productDetailsEditText.text.toString(),
                dollar_price = productDollarPriceEditText.text.toString().toDouble(),
                yrial_price = productYeRialPriceEditText.text.toString().toDouble(),
                product_quantity = productQuantityEditText.text.toString().toInt(),
                vendor_id = oshoppingViewModel.getStoredUserId(),
                cat_id = categoryId,
                product_img = imageName,
                product_discount = productDiscountET.text.toString().toInt(),
                color = chosenColorTV.text.toString()
            )
            oshoppingViewModel.pushProduct(product)
            Navigation.findNavController(view)
                .navigate(R.id.action_addProductFragment_to_myProductFragment)

        }


        val popupColorMenuBtn = view.findViewById<Button>(R.id.choose_color_btn)

        popupColorMenuBtn.setOnClickListener { v: View ->
            //popupColorMenuBtn.isSelected=true
            showMenu(v, R.menu.popup_color_menu)
        }
        chooseCategoryBtn.setOnClickListener {
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (menuItem != null) {
                    categoryTitle = menuItem.title as String
                    categoryId = menuItem.itemId
                    chosenCategoryTV.setText(menuItem.title)
                }

                return@setOnMenuItemClickListener true

            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        minus.setOnClickListener {
            if (counter != 0)
                counter--
            Quantity.text = counter.toString()

        }

        plus.setOnClickListener {
            counter++
            Quantity.text = counter.toString()

        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this.requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when (menuItem.itemId) {
                R.id.option_1 -> {
                    chosenColorTV.setText(menuItem.title)

                }
                R.id.option_2 -> {
                    chosenColorTV.setText(menuItem.title)

                }
                R.id.option_3 -> {
                    chosenColorTV.setText(menuItem.title)

                }

            }
            return@setOnMenuItemClickListener true

        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    fun fillCategoryMenu() {
        popupMenu = PopupMenu(this.requireContext(), chooseCategoryBtn)
        oshoppingViewModel.categoryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { categories ->
                Log.d("fetchCategoryMenu", "Category fetched successfully ${categories}")
                var count = 0
                for (i in categories) {

                    categories[count].cat_id?.let {
                        popupMenu.menu.add(
                            Menu.NONE,
                            it, count, categories[count].cat_name
                        )
                    }

                    count++

                }

            })
    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
               REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                
                   Log.d("imageUrl", "the name of image in data of data is: ${data} \n ")

                   //Log.d("imageUrl", "the name of image is: ${selectedImageUri}")
                    mImageView.setImageURI(selectedImageUri)
                    uploadImage()
                }
            }
        }
    }
    private fun uploadImage() {
        if (selectedImageUri == null) {
            layout_root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =requireContext().contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        val body = UploadRequestBody(file, "image" )
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layout_root.snackbar(t.message!!)

            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layout_root.snackbar(it.message)
                }
                imageName=response.body()?.image
                //Toast.makeText(requireContext(), "the image name is $imageName", Toast.LENGTH_SHORT).show()


            }
        })


    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 9
    }


}