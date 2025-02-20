
package com.manager.filemanager.activity


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.manager.filemanager.R
import com.manager.filemanager.fragment.HomeFragment


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleOpenWithIntent(intent)
    }

    private fun handleOpenWithIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri != null) {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
                if (fragment is HomeFragment) {
                    fragment.onNewIntent(uri)
                } else {
                    val homeFragment = HomeFragment.newInstance(uri)
                    startNewFragment(homeFragment)
                }
            }
        }
    }


    fun startNewFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment)
            .addToBackStack(null).commit()
    }
}