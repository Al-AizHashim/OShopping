package com.yemen.oshopping.vendor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductDetails
import com.yemen.oshopping.uploadImage.UploadImageActivity
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.activity_add_product.*


class AddProductFragment : Fragment() {

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
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    lateinit var add_product: ProductDetails
    lateinit var oshoppingViewModel: OshoppingViewModel
    private var mUri: Uri? = null
    lateinit var inttent: Intent
    private  var imagename: String?=null
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
            //  showDialog("Choose Image")
            inttent = Intent(this.requireContext(), UploadImageActivity::class.java)
            startActivityForResult(inttent, 919)

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
                product_img = imagename,
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


}