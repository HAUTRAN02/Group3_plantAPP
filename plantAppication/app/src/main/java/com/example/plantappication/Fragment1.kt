package com.example.plantappication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.plantappication.databinding.ActivityHomepageBinding
import com.example.plantappication.databinding.Fragment1Binding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
//private lateinit var binding: Fragment1Binding
private var _binding: Fragment1Binding? = null
// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

class Fragment1 : Fragment(R.layout.fragment_1) {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = Fragment1Binding.inflate(inflater, container, false)
            val view = binding.root
            process()
            return view
        }



    private fun process() {
        val list = mutableListOf<outData>()
        list.add(outData(R.drawable.al1,"The Intelligent Plant | awkward botany"))
        list.add(outData(R.drawable.al2,"Glimpse the Giant Water Lily"))
        list.add(outData(R.drawable.al3,"Plant Identification Online"))
        list.add(outData(R.drawable.al4,"Air Purifying Plants | Air Oasisy"))
        list.add(outData(R.drawable.al5,"Sensitive plant | botany | Britannica"))
        list.add(outData(R.drawable.al6,"Easiest-Ever Homemade Plant Food "))


        binding.rvFragment1.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val itemAdapter = RvAdapter2(list, object :rvInterface{
            override fun onClick(pos: Int) {

            }

        })
        binding.rvFragment1.adapter = itemAdapter
    }


}