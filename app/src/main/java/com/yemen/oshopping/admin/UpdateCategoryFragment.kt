package com.yemen.oshopping.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.text.set
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.ui.AddUserFragment
import com.yemen.oshopping.uploadImage.*
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.fragment_add_category.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class UpdateCategoryFragment : Fragment() {
    private var selectedImageUri: Uri? = null
    var imageName: String? = null
    private var categoryId = 0
    lateinit var close: ImageButton

    private lateinit var updateCategoryTextView: TextInputLayout
    private lateinit var categoryViewModel: OshoppingViewModel
    lateinit var updateCategoryImage: ImageView
    private val categoryArgs: UpdateCategoryFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_category, container, false)
        categoryId = categoryArgs.categoryIdArg
        val categoryName = categoryArgs.categoryNameArg
        updateCategoryImage = view.findViewById(R.id.update_category_img)
        updateCategoryTextView = view.findViewById(R.id.update_cat_editText)
        val updateCategoryBtn = view.findViewById(R.id.update_cat_btn) as Button
        val updateCategoryBtn2 = view.findViewById(R.id.update_cat_btn2) as ImageButton
        close=view.findViewById(R.id.bt_close)
        updateCategoryBtn.setOnClickListener {
            updateCategory()

        }
        updateCategoryBtn2.setOnClickListener {
            updateCategory()

        }
        close.setOnClickListener {
            activity?.onBackPressed()
        }

        updateCategoryImage.setOnClickListener {

            openImageChooser()
        }
        return view
    }

    private fun updateCategory() {
        val category = Category(
            cat_id = categoryId,
            cat_name = updateCategoryTextView.editText?.text.toString().trim(),
            category_image = imageName
        )
        categoryViewModel.updateCategory(category)
        updateCategoryTextView.clearFocus()
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_updateCategoryFragment_to_showCategoryFragment)
        }
        toastIconSuccess(requireContext())
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            //it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(it, AddUserFragment.REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AddUserFragment.REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data

                    updateCategoryImage.setImageURI(selectedImageUri)
                    uploadImage()
                }
            }
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            linear_layout_root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            requireContext().contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
                ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(selectedImageUri!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        val body = UploadRequestBody(file, "image")
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                linear_layout_root.snackbar(t.message!!)

            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    linear_layout_root.snackbar(it.message)
                }
                imageName = response.body()?.image

            }
        })


    }

    fun toastIconSuccess(context: Context) {
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        val custom_view: View =
            layoutInflater.inflate(R.layout.toast_icon_text, null)
        (custom_view.findViewById<View>(R.id.message) as TextView).text = "Success!"
        (custom_view.findViewById<View>(R.id.icon) as ImageView).setImageResource(R.drawable.ic_done)
        (custom_view.findViewById<View>(R.id.parent_view) as CardView).setCardBackgroundColor(
            resources.getColor(R.color.green_500)
        )
        toast.view = custom_view
        toast.show()
    }

}