package com.yemen.oshopping

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.yemen.oshopping.model.ActivityItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import java.text.SimpleDateFormat
import java.util.*


const val PICK_PDF_FILE = 2
private val STORAGE_CODE: Int = 99

class Activities_Fragment : Fragment() {
    var url: String = MainActivity.LOCAL_HOST_URI

    private lateinit var oShoppingViewModel: OshoppingViewModel
    private lateinit var showActivitiesRecyclerView: RecyclerView
    private lateinit var saveAsPDF: ImageButton
    private lateinit var activityItemList: List<ActivityItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        oShoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        saveAsPDF = view.findViewById(R.id.save_pdf)
        showActivitiesRecyclerView = view.findViewById(R.id.recycler_view_activities)
        showActivitiesRecyclerView.layoutManager = GridLayoutManager(context, 1)


        return view

    }

    private fun savePDF() {

        val mFile = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFile + ".pdf"

        if (activityItemList.size != 0) {
            try {
                val pdfDocument = PdfDocument(PdfWriter(mFilePath))
                val document = Document(pdfDocument)

                val header =
                    Paragraph("No----|-------- Product name---------|--Quantity--|--Total price--|---Activity type--|")
                val title =
                    Paragraph("------------------------------------List of Your activities---------------------------------").setMarginLeft(
                        30.0F
                    )
                document.add(title)
                document.add(Paragraph())
                document.add(header)

                for (i in 0 until activityItemList.size) {

                    val number = "      $i"
                    var name = activityItemList[i].productName.toString()
                    var length = name.length
                    for (j in 14 downTo length) {
                        name += "_"
                    }


                    var quantity = activityItemList[i].quantity.toString()


                    var price = activityItemList[i].totalPrice.toString()
                    length = price.length
                    for (j in 14 downTo length) {
                        price += "_"
                    }

                    var type = activityItemList[i].activityType
                    length = type.length
                    for (j in 10 downTo length) {
                        type += "_"
                    }

                    val item =
                        Paragraph("$number        |               $name|        $quantity        |    $price |       $type  ")
                    document.add(item)
                }
                document.close()
                val muri = mFilePath
                //   openFile(Uri.parse(muri))

                   toastIconSuccess( "File Successfully saved")

            } catch (e: Exception) {
                toastIconError("error  " + e.message)
            }
        } else {
            toastIconError("There is no data")

        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oShoppingViewModel.apply {
            loadActivities(getStoredUserId())
        }
        oShoppingViewModel.activityItemLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { activityItem ->
                Log.d("fetchActivity", "activity Item Live Data: $activityItem")
                activityItemList = activityItem
                showActivitiesRecyclerView.adapter = ShowActivitiesAdapter(activityItem)
                updateui(activityItem)
            })
    }

    private fun updateui(activityItem: List<ActivityItem>) {
        showActivitiesRecyclerView.adapter = ShowActivitiesAdapter(activityItem)
    }

    override fun onStart() {
        super.onStart()

        saveAsPDF.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                if (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                } else {
                    savePDF()
                }
            } else {
                savePDF()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePDF()
                } else {
                    Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


    private inner class ShowActivitiesHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }

        private lateinit var activItems: ActivityItem
        var mainLayout = itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.translate_anim)

        private val itemQuantity = itemView.findViewById(R.id.item_quantity) as TextView
        private val itemName = itemView.findViewById(R.id.item_name) as TextView
        private val itemPrice = itemView.findViewById(R.id.item_price) as TextView

        fun bind(activityItem: ActivityItem) {
            mainLayout.startAnimation(translateAnimation)
            activItems = activityItem
            itemQuantity.text = activityItem.quantity.toString()
            itemName.text = activityItem.productName
            itemPrice.text = activityItem.totalPrice.toString()

        }


        override fun onClick(v: View?) {

        }
    }

    private inner class ShowActivitiesAdapter(private val activItem: List<ActivityItem>) :
        RecyclerView.Adapter<ShowActivitiesHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowActivitiesHolder {

            when (viewType) {
                1 -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.active_item_buy, parent, false)
                    return ShowActivitiesHolder(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.active_item_sell, parent, false)
                    return ShowActivitiesHolder(view)
                }
            }


        }

        override fun getItemCount(): Int = activItem.size

        override fun onBindViewHolder(holder: ShowActivitiesHolder, position: Int) {
            val activityItem = activItem[position]
            holder.bind(activityItem)
        }

        override fun getItemViewType(position: Int): Int {
            val x: Int
            if (activItem[position].activityType.equals("buy"))
                x = 1
            else
                x = 0
            return x
        }
    }

    fun openFile(pickerInitialUri: Uri) {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"


            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }.also { intent ->
            val chooserIntent =
                Intent.createChooser(intent, "choose pdf reader ")
            startActivityForResult(chooserIntent, PICK_PDF_FILE)
        }

        // startActivity(intent)
    }

    companion object {
        fun newInstance() = Activities_Fragment()
    }
    private fun toastIconError(message:String) {
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG

        //inflate view
        val custom_view =
            layoutInflater.inflate(R.layout.toast_icon_text, null)
        (custom_view.findViewById<View>(R.id.message) as TextView).text = message
        (custom_view.findViewById<View>(R.id.icon) as ImageView).setImageResource(
            R.drawable.ic_close
        )
        (custom_view.findViewById<View>(R.id.parent_view) as CardView).setCardBackgroundColor(
            resources.getColor(R.color.red)
        )
        toast.view = custom_view
        toast.show()

    }
    fun toastIconSuccess(message:String) {
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG
        val custom_view: View =
            layoutInflater.inflate(R.layout.toast_icon_text, null)
        (custom_view.findViewById<View>(R.id.message) as TextView).text = message
        (custom_view.findViewById<View>(R.id.icon) as ImageView).setImageResource(R.drawable.ic_done)
        (custom_view.findViewById<View>(R.id.parent_view) as CardView).setCardBackgroundColor(
            resources.getColor(R.color.green_500)
        )
        toast.view = custom_view
        toast.show()
    }
}
