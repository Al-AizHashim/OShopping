package com.yemen.oshopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.model.User
import com.yemen.oshopping.ui.ShowProductReportsDialog
import com.yemen.oshopping.ui.ShowVendorActivity
import com.yemen.oshopping.ui.ShowVendorFragment
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val TAG = "User"

class ShowUsersfragment: Fragment()  {
    private lateinit var userViewModel: OshoppingViewModel
    private lateinit var userRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.catagory_fragment, container, false)
        userRecyclerView = view.findViewById(R.id.category_recycler_view22)
        userRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.userItemLiveData.observe(
            viewLifecycleOwner,
            Observer { users ->
                Log.d("fetchUser", "User fetched successfully ${users}")
                userRecyclerView.adapter = UserAdapter(users)

            })
    }

    private inner class UserHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView),View.OnClickListener{
        var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)

        val userNameTV = itemTextView.findViewById(R.id.user_full_name) as TextView
        val userStatusTV = itemTextView.findViewById(R.id.user_type) as TextView
        lateinit var user:User
        fun bind(user: User){
            this.user=user
            var userStatus="purchaer"
            userNameTV.text=user.first_name+" "+user.last_name
            if (user.vendor==1){
                userStatus="Vendor"
            }
            userStatusTV.text=userStatus
            mainLayout.startAnimation(translateAnimation)
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val intent = Intent(itemView.context, ShowVendorActivity::class.java)
            intent.putExtra("VENDORID", user.vendor)
            itemView.context.startActivity(intent)
        }


        }


    private inner class UserAdapter(private val users: List<User>)

        : RecyclerView.Adapter<UserHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): UserHolder {
            val View = LayoutInflater.from(parent.context).inflate(R.layout.users_item_for_admin,parent,false)
            return UserHolder(View)
        }
        override fun getItemCount(): Int = users.size

        override fun onBindViewHolder(holder: UserHolder, position: Int) {
            val category = users[position]
            holder.bind(category)
        }
    }

    companion object {
        fun newInstance(): ShowUsersfragment {
            return ShowUsersfragment()
        }
    }
}
