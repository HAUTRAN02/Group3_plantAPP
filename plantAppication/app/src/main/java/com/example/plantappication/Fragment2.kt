package com.example.plantappication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.plantappication.databinding.Fragment1Binding
import com.example.plantappication.databinding.Fragment2Binding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: Fragment2Binding? = null
// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

class Fragment2 : Fragment(R.layout.fragment_2) {
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var articleList: ArrayList<articleModule>
    private lateinit var articleAdapter: articleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment2Binding.inflate(inflater, container, false)
        val view = binding.root
        process()
        return view
    }

    private fun process() {
        val list = mutableListOf<outData1>()
        list.add(outData1(R.drawable.pic10))
        list.add(outData1(R.drawable.pic9))
        list.add(outData1(R.drawable.pic1))
        list.add(outData1(R.drawable.pic3))
        list.add(outData1(R.drawable.pic4))
        list.add(outData1(R.drawable.pic5))
        list.add(outData1(R.drawable.pic6))
        list.add(outData1(R.drawable.pic7))
        list.add(outData1(R.drawable.pic8))

        binding.rvFragment2.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val itemAdapter = RvAdapter4(list, object :rvInterface{
            override fun onClick(pos: Int) {


            }

        })
        binding.rvFragment2.adapter = itemAdapter
    }


}