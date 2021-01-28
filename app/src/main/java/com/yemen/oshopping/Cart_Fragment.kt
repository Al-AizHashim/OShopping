package com.yemen.oshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.yemen.oshopping.model.ActivityItem
import com.yemen.oshopping.model.Cart
import com.yemen.oshopping.model.Rating
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.fragment_cart.*

private const val TAG = "Category"

class Cart_Fragment : Fragment() {
    var url: String = MainActivity.LOCAL_HOST_URI
    private lateinit var cartViewModel: OshoppingViewModel
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartItems: Cart
    lateinit var activityItem: ActivityItem
    val delim = ":"
    var list:List<String> =ArrayList()
    private lateinit var back: ImageButton
    lateinit var ratingBar2: RatingBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cart_recyclerview)
        cartRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //submitRatingBTN = view.findViewById(R.id.submit_rating_button)






        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.loadCart(cartViewModel.getStoredUserId())
        cartViewModel.cartItemLiveData.observe(
            viewLifecycleOwner,
            Observer { carts ->

                Log.d(
                    "fetchCart2",
                    "Cart fetched successfullycarts ${carts} \n"
                )
                cart_recyclerview.adapter = CartAdapter(carts)


            })

    }

    private inner class CartHolder(itemTextView: View) : RecyclerView.ViewHolder(itemTextView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }

        //private lateinit var cartItems: Cart


        private val productName = itemView.findViewById(R.id.product_nameTv) as TextView
        private val productDate = itemView.findViewById(R.id.product_category) as TextView
        private val productImage = itemView.findViewById(R.id.product_img) as CarouselView
        private val ratingBar2 = itemView.findViewById(R.id.rating_Bar_2_product_details) as RatingBar
        private val cartDelete = itemView.findViewById(R.id.delete) as Button
        private val buyBtn = itemView.findViewById(R.id.buy_btn) as Button
        private val productPrice=itemView.findViewById(R.id.product_price) as TextView

        fun bind(cartItem: Cart) {
            cartItems=cartItem
            list = cartItem.product_img?.split(delim)!!
            if (list.size==1)
                productImage.pageCount = 1
            else
                productImage.pageCount = list.size-1
            var imageListener =
                ImageListener { position, imageView ->
                    Picasso.get().load(url+list[position]).into(imageView)
                }
            productImage.setImageListener(imageListener)

            cartItems = cartItem
            productName.text = cartItem.product_name
            productDate.text = cartItem.product_date
            productPrice.text=cartItem.dollar_price.toString()+" $"

            cartDelete.setOnClickListener {
                 cartViewModel.deleteCart(cartItem)
                cartViewModel.loadCart(cartViewModel.getStoredUserId())

            }

            ratingBar2.setOnRatingBarChangeListener { ratingBar: RatingBar, fl: Float, b: Boolean ->
                Log.d("ratingBarlog","ratingBarlog ")
                val ratingBarValue = ratingBar2.rating.toString()
                Toast.makeText(
                    requireContext(),
                    "Rating is: " + ratingBarValue, Toast.LENGTH_LONG
                ).show()
                var rating = Rating(
                    product_id = cartItems.fk_product_id,
                    user_id = cartViewModel.getStoredUserId(),
                    rating = ratingBar2.rating.toInt()
                )
                cartViewModel.pushRating(rating)

            }

            buyBtn.setOnClickListener {
                    val activ= ActivityItem(
                        productId = cartItems.fk_product_id,
                        productName = cartItems.product_name,
                        totalPrice = cartItems.dollar_price ,
                        activityType = "buy",
                        quantity = 1,
                        yrial_price = cartItems.yrial_price,
                        dollar_price = cartItems.dollar_price
                    )
                    Log.d("pushToActivity","the contint of Activity is :$activ")
                cartViewModel.pushActivity(activ)
                    Toast.makeText(
                        requireContext(),
                        "Added to Activity successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }


        }

        override fun onClick(v: View?) {
            Toast.makeText(
                requireContext(),
                "The id: ${cartItems.fk_product_id} and title ${cartItems.product_name} is clicked",
                Toast.LENGTH_LONG
            ).show()
            // callbacks?.onProductSelected(cartItems.product_id)
        }
    }

    private inner class CartAdapter(private val cartItem: List<Cart>)

        : RecyclerView.Adapter<CartHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CartHolder {
            val View = LayoutInflater.from(parent.context)
                .inflate(R.layout.show_product_in_cart_list_item, parent, false)
            return CartHolder(View)
        }

        override fun getItemCount(): Int = cartItem.size

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            val cart_Item = cartItem[position]
            holder.bind(cart_Item)
        }
    }


    companion object {
        fun newInstance(): Cart_Fragment {
            return Cart_Fragment()
        }
    }
}
