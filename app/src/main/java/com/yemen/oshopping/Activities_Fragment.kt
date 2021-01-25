package com.yemen.oshopping

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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




class Activities_Fragment: Fragment() {
    var url: String = "http://192.168.1.108/oshopping_api/"

    private lateinit var oShoppingViewModel: OshoppingViewModel
    private lateinit var showActivitiesRecyclerView: RecyclerView
    private lateinit var saveAsPDF: ImageButton


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

        saveAsPDF.setOnClickListener {
            savePDF()
        }

        return view

    }

    private fun savePDF(){

        val mFile = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString()+"/"+ mFile + ".pdf"

        try {
            val pdfDocument = PdfDocument(PdfWriter( mFilePath))
            val document = Document(pdfDocument)

            val text = Paragraph("My Text")
            document.add(text)

            document.close()

            Toast.makeText(context,"Success"+ mFile+".pdf is saved to "+ mFilePath,Toast.LENGTH_LONG).show()

        }
        catch ( e: Exception){
            Toast.makeText(context,"error  "+ e.message ,Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 ->
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                savePDF()
            }else{
                Toast.makeText(context,"parmission denied .. ",Toast.LENGTH_LONG).show()
            }
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
                Log.d("activityItemLiveData", "activity Item Live Data: $activityItem")
                updateui(activityItem)
            })


    }

    private fun updateui(activityItem: List<ActivityItem>) {
        showActivitiesRecyclerView.adapter = ShowActivitiesAdapter(activityItem)
    }

    private inner class ShowActivitiesHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }

        private lateinit var activItems: ActivityItem

        private val itemQuantity = itemView.findViewById(R.id.item_quantity) as TextView
        private val itemName = itemView.findViewById(R.id.item_name) as TextView
        private val itemPrice = itemView.findViewById(R.id.item_price) as TextView

        fun bind(activityItem: ActivityItem) {
            activItems = activityItem
            itemQuantity.text = activityItem.quantity.toString()
           // itemName.text = activityItem.
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

            when(viewType){
                1 ->{
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.active_item_buy, parent, false)
                    return ShowActivitiesHolder(view)
                }
                else -> {
                    val view =LayoutInflater.from(parent.context)
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
            val x:Int
             if (activItem[position].activityType.equals("buy"))
                x=1
            else
                x=0
            return x
        }
    }


    companion object {
        fun newInstance() = Activities_Fragment()
    }
}
