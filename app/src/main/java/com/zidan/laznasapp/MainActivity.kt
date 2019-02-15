package com.zidan.laznasapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zidan.laznasapp.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private var userName = ""
    private var userEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userName = intent.getStringExtra("name")
        userEmail = intent.getStringExtra("email")

        bottom_nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_home -> {
                    loadHomeFragment(savedInstanceState)
                }
                R.id.btn_profile -> {
                    loadProfileFragment(savedInstanceState)
                }
            }
            true
        }
        bottom_nav.selectedItemId = R.id.btn_home
    }

    private fun loadHomeFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_view,
                    HomeFragment(), HomeFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun loadProfileFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){
            val bundle = Bundle()
            bundle.putString("name", userName)
            bundle.putString("email", userEmail)
            val profileFragment = ProfileFragment()
            profileFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_view,
                    profileFragment, ProfileFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
