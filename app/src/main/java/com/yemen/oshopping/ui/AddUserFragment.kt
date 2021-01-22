package com.yemen.oshopping.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.MainScreen
import com.yemen.oshopping.R
import com.yemen.oshopping.model.User
import com.yemen.oshopping.splashScreen2
import com.yemen.oshopping.uploadImage.*
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_login_screen.*
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


class AddUserFragment : Fragment() {
    private var selectedImageUri: Uri? = null
    var imageName:String?=null
    lateinit var fNameEditText: EditText
    lateinit var lNameEditText: EditText
    lateinit var addressEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var detailsEditText: EditText
    lateinit var saveProfileBTN: Button
    lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var userImage:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_user, container, false)
        fNameEditText = view.findViewById(R.id.first_name_edit_text)
        lNameEditText = view.findViewById(R.id.last_name_edit_text)
        addressEditText = view.findViewById(R.id.address_edit_text)
        phoneNumberEditText = view.findViewById(R.id.phone_edit_text)
        detailsEditText = view.findViewById(R.id.details_edit_text)
        saveProfileBTN = view.findViewById(R.id.save_profile_btn)
        userImage = view.findViewById(R.id.user_image)
        userImage.setOnClickListener {
            openImageChooser()



        }
        val intent=Intent(requireContext(),MainScreen::class.java)
        saveProfileBTN.setOnClickListener {
            val user = User(
                first_name = fNameEditText.text.toString(),
                last_name = lNameEditText.text.toString(),
                email = oshoppingViewModel.getStoredEmail().toString(),
                address = addressEditText.text.toString(),
                phone_number = phoneNumberEditText.text.toString(),
                details = detailsEditText.text.toString(),
                image = imageName
            )
            oshoppingViewModel.apply {
                pushUser(user)


            }
            oshoppingViewModel.getUserByEmail(oshoppingViewModel.getStoredEmail().toString())
            oshoppingViewModel.userItemLiveDataByEmail.observe(viewLifecycleOwner, Observer { userdata ->
                userdata.get(0).user_id?.let { userId -> oshoppingViewModel.setUserId(userId) }
            })

            oshoppingViewModel.userItemLiveDataByEmail.observe(
                viewLifecycleOwner, Observer { userdata ->
                    userdata.get(0).user_id?.let { userId -> oshoppingViewModel.setUserId(userId) }
                })



            startActivity(intent)





        }



        return view
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
                    userImage.setImageURI(selectedImageUri)
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
        const val REQUEST_CODE_PICK_IMAGE = 10
    }
}