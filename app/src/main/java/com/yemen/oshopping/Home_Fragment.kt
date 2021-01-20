package com.yemen.oshopping

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.ui.ShowProductFragment
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import java.text.SimpleDateFormat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home_Fragment: Fragment(), SearchView.OnQueryTextListener{
    var url: String = "http://192.168.1.4/oshopping_api/"

    private lateinit var trendBtn:Button
    private lateinit var categoryBtn:Button
    private lateinit var colorBtn:Button
    private lateinit var vendorBtn:Button
    private lateinit var highestRateBtn:Button
    private lateinit var popupMenu:PopupMenu
    private lateinit var   searchView :SearchView



    interface Callbacks {
        fun onProductSelected(product_id: Int)
    }

    private var callbacks: Callbacks? = null

    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var showProductRecyclerView: RecyclerView
    private var showProductByCategory: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showProductByCategory = it.getString(com.yemen.oshopping.ARG_PARAM1)

        }

        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        showProductRecyclerView = view.findViewById(R.id.show_product_recycler_view)
        showProductRecyclerView.layoutManager = GridLayoutManager(context, 1)
        trendBtn=view.findViewById(R.id.trend_btn)
        categoryBtn=view.findViewById(R.id.category_btn)
        colorBtn=view.findViewById(R.id.color_btn)
        vendorBtn=view.findViewById(R.id.vendor_btn)
        highestRateBtn=view.findViewById(R.id.highest_rate_btn)
        searchView = view.findViewById(R.id.search_view)
        //searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        popupMenu= PopupMenu(requireContext(),categoryBtn)
        oshoppingViewModel.categoryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { categories ->
                Log.d("fetchCategoryMenu", "Category fetched successfully ${categories}")
                var count=0
                for(i  in categories ){

                    categories[count].cat_id?.let {
                        popupMenu.menu.add(Menu.NONE,
                            it,count,categories[count].cat_name)
                    }

                    count++

                }

            })



        val popupColorMenuBtn = view.findViewById<Button>(R.id.menu_button)

        popupColorMenuBtn.setOnClickListener { v: View ->
            popupColorMenuBtn.isSelected=true
            showMenu(v, R.menu.popup_color_menu)
        }




        return view
    }





private fun showMenu(v: View, @MenuRes menuRes: Int) {
    val popup = PopupMenu(requireContext(), v)
    popup.menuInflater.inflate(menuRes, popup.menu)

    popup.setOnMenuItemClickListener { menuItem: MenuItem ->
        // Respond to menu item click.
        when(menuItem.itemId){
            R.id.option_1 -> {
                Toast.makeText(requireContext(),"Color ${menuItem.title} is clicked",Toast.LENGTH_LONG).show()

            }
            R.id.option_2 -> {
                Toast.makeText(requireContext(),"Color ${menuItem.title} is clicked",Toast.LENGTH_LONG).show()

            }
            R.id.option_3 -> {
                Toast.makeText(requireContext(),"Color ${menuItem.title} is clicked",Toast.LENGTH_LONG).show()

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




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trendBtn.isSelected=true
        oshoppingViewModel.productItemLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { productItems ->
                Log.d("productItemLiveData", "product Item Live Data")
                updateui(productItems)
            })
        //trend is the default
        trendBtn.setOnClickListener {
            vendorBtn.isSelected = false
            categoryBtn.isSelected=false
            trendBtn.isSelected=true
            colorBtn.isSelected=false
            highestRateBtn.isSelected=false
            oshoppingViewModel.productItemLiveData.observe(
                viewLifecycleOwner, androidx.lifecycle.Observer
                { productItems ->
                    Log.d("productItemLiveData", "product Item Live Data")
                    updateui(productItems)
                })
        }
        vendorBtn.setOnClickListener {
            vendorBtn.isSelected = true
            categoryBtn.isSelected=false
            trendBtn.isSelected=false
            colorBtn.isSelected=false
            highestRateBtn.isSelected=false

            oshoppingViewModel.getProductByVendorId(2)
            oshoppingViewModel.productItemLiveDataByVendorID.observe(
                viewLifecycleOwner, androidx.lifecycle.Observer
                { productItems ->
                    Log.d("productItemLiveData", "product Item Live Data")
                    updateui(productItems)
                })


        }
        categoryBtn.setOnClickListener {
            categoryBtn.isSelected=true
            vendorBtn.isSelected=false
            trendBtn.isSelected=false
            colorBtn.isSelected=false
            highestRateBtn.isSelected=false
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {menuItem: MenuItem? ->
                if (menuItem != null) {
                    oshoppingViewModel.loadProductByCategory(menuItem.itemId)
                }
                oshoppingViewModel.productItemLiveDataByCategory.observe(
                    viewLifecycleOwner, androidx.lifecycle.Observer
                    { productItemsByCategory ->
                        Log.d("productItemLiveData", "product Item Live Data")
                        updateui(productItemsByCategory)
                    }
                )
                return@setOnMenuItemClickListener true

            }

        }
        colorBtn.setOnClickListener {
            categoryBtn.isSelected=false
            vendorBtn.isSelected=false
            trendBtn.isSelected=false
            colorBtn.isSelected=true
            highestRateBtn.isSelected=false
        }
        highestRateBtn.setOnClickListener {
            categoryBtn.isSelected=false
            vendorBtn.isSelected=false
            trendBtn.isSelected=false
            colorBtn.isSelected=false
            highestRateBtn.isSelected=true
        }

    }


    private fun updateui(productItems: List<ProductItem>) {
        showProductRecyclerView.adapter = ShowProductAdapter(productItems)
    }

    private inner class ShowProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener  {
        init {
            itemView.setOnClickListener(this)

        }

        private lateinit var productItemss: ProductItem
        private val productName = itemView.findViewById(R.id.product_nameTv) as TextView
        private val productDate = itemView.findViewById(R.id.product_category) as TextView
        private val productImage = itemView.findViewById(R.id.product_img) as ImageView
        private val productRatingNo = itemView.findViewById(R.id.rating_bar_text_view_show_prodcut) as TextView
        private val productRating = itemView.findViewById(R.id.rating_Bar_Show_product)  as RatingBar


        fun bind(productItems: ProductItem) {
            var compositeProductUrl = url + productItems.product_img
            var conditionString = "string" + productItems.product_img
            if (!conditionString.equals("stringnull"))
                Picasso.get().load(compositeProductUrl).into(productImage)
            productItemss = productItems
            productName.text = productItems.product_name
            productDate.text = productItems.product_date
            productRatingNo.setText(productItems.number_of_ratings.toString()+" votes")
            productRating.rating=productItems.rating_average
        }


        override fun onClick(v: View?) {
            Toast.makeText(
                requireContext(),
                "The id: ${productItemss.product_id} and title ${productItemss.product_name} is clicked",
                Toast.LENGTH_LONG
            ).show()
            callbacks?.onProductSelected(productItemss.product_id)
        }


    }

    private inner class ShowProductAdapter(private val productItems: List<ProductItem>) :
        RecyclerView.Adapter<ShowProductHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowProductHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.show_product_list_item, parent, false)
            return ShowProductHolder(view)
        }

        override fun getItemCount(): Int = productItems.size

        override fun onBindViewHolder(holder: ShowProductHolder, position: Int) {
            val productItems = productItems[position]
            holder.bind(productItems)

        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    companion object {
        fun newInstance() = Home_Fragment().apply {
            arguments = Bundle().apply {

            }
        }

        fun newInstance(productCategory: String) = Home_Fragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, productCategory)

            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
       searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }

        return true
    }
    private fun searchThroughDatabase(query: String) {
        oshoppingViewModel.search(query)
        oshoppingViewModel.searchLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { searchResultItems ->
                Log.d("searchResultItems", "search result is received")
                updateui(searchResultItems)
            })

    }
}