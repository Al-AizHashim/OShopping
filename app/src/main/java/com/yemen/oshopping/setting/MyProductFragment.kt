package com.yemen.oshopping.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yemen.oshopping.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyProductFragment : Fragment() {
    lateinit var fab:FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_my_product, container, false)
        fab=view.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_myProductFragment_to_addProduct2)
        }
        return view
    }


}