
package com.manager.filemanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.manager.filemanager.R
import com.manager.filemanager.activity.MainActivity

class StartedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_started, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    fun initClick() {
        val btnStarted = requireView().findViewById<Button>(R.id.btn_started)
        val recentFragment = RecentFragment()
        btnStarted.setOnClickListener {
            (requireActivity() as MainActivity).startNewFragment(recentFragment)
        }
    }

}