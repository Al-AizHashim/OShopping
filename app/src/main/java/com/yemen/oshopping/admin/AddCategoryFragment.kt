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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.setting.SettingActivity
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


class AddCategoryFragment : Fragment() {
    private val settingActivity=SettingActivity()
    private var selectedImageUri: Uri? = null
    var imageName:String?=null
    lateinit var addCategoryBtn: Button
    lateinit var addCategoryBtn2: ImageButton
    lateinit var addCategoryEditText: TextInputLayout
    lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var addCategoryImg:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_add_category, container, false)
        addCategoryBtn=view.findViewById(R.id.add_cat_btn)
        addCategoryBtn2=view.findViewById(R.id.add_cat_btn2)
        addCategoryImg=view.findViewById(R.id.add_category_img)
        addCategoryEditText=view.findViewById(R.id.add_cat_editText)
        addCategoryBtn.setOnClickListener {
            addCategory()
        }
        addCategoryBtn2.setOnClickListener {
            addCategory()
        }
        addCategoryImg.setOnClickListener {
            openImageChooser()
        }

        return view
    }
    fun addCategory(){
        val cat= Category(cat_name= addCategoryEditText.getEditText()?.getText().toString().trim(),category_image=imageName)
        oshoppingViewModel.pushcat(cat)
        addCategoryEditText.clearFocus()
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_addCategoryFragment_to_showCategoryFragment)
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

                    Log.d("imageUrl", "the name of image in data of data is: ${data} \n ")

                    //Log.d("imageUrl", "the name of image is: ${selectedImageUri}")
                    addCategoryImg.setImageURI(selectedImageUri)
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
                linear_layout_root.snackbar(t.message!!)

            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    linear_layout_root.snackbar(it.message)
                }
                imageName=response.body()?.image
                Toast.makeText(requireContext(), "the image name is $imageName", Toast.LENGTH_SHORT).show()


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