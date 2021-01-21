package com.yemen.oshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.model.ActivityItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Activities_Fragment: Fragment() {
    var url: String = "http://192.168.1.108/oshopping_api/"

    private lateinit var oShoppingViewModel: OshoppingViewModel
    private lateinit var showActivitiesRecyclerView: RecyclerView

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        oShoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        showActivitiesRecyclerView = view.findViewById(R.id.recycler_view_activities)
        showActivitiesRecyclerView.layoutManager = GridLayoutManager(context, 1)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oShoppingViewModel.activityItemLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { activItem ->
                Log.d("activityItemLiveData", "activity Item Live Data")
                updateui(activItem)
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

        private lateinit var activItem: ActivityItem

        private val itemQuantity = itemView.findViewById(R.id.item_quantity) as TextView
        private val itemName = itemView.findViewById(R.id.item_name) as TextView
        private val itemPrice = itemView.findViewById(R.id.item_price) as TextView

        fun bind(activityItem: ActivityItem) {
            activItem = activityItem
            itemQuantity.text = activityItem.quantity.toString()
           // itemName.text = activityItem.
            itemPrice.text = activityItem.totalPrice.toString()
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    private inner class ShowActivitiesAdapter(private val activItem: List<ActivityItem>) :
        RecyclerView.Adapter<ShowActivitiesHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowActivitiesHolder {
            var activityItem: ActivityItem
            val view = if(activityItem.activityType =="buy"){
                 LayoutInflater.from(parent.context)
                    .inflate(R.layout.active_item_buy, parent, false)
            } else if (activityItem.activityType =="sell"){
                 LayoutInflater.from(parent.context)
                    .inflate(R.layout.active_item_sell, parent, false)
            } else {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.active_item_sell, parent, false)
            }
            return ShowActivitiesHolder(view)
        }

        override fun getItemCount(): Int = activItem.size

        override fun onBindViewHolder(holder: ShowActivitiesHolder, position: Int) {
            val activityItem = activItem[position]
            holder.bind(activityItem)
        }
    }


    companion object {
        fun newInstance() = Activities_Fragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
