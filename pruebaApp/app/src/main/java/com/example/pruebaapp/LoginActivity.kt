package com.example.pruebaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.pruebaapp.fragments.LoginFragment
import com.example.pruebaapp.fragments.MenorLoginFragment
import com.google.android.material.tabs.TabLayout

/**
 * The number of pages (wizard steps) to show in this demo.
 */
private const val NUM_PAGES = 2

class LoginActivity : FragmentActivity() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ScreenSlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.loginViewPager)
        tabLayout = findViewById(R.id.indicador)

        // The pager adapter, which provides the pages to the view pager widget.
        adapter = ScreenSlidePagerAdapter(supportFragmentManager, lifecycle)


        //val tutor = tabLayout.addTab(tabLayout.newTab().setText("                         Tutor                         "))
        //val menor = tabLayout.addTab(tabLayout.newTab().setText("                         Menor                         "))

        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }

        })

        /*val registroButton = findViewById<Button>(R.id.registroButton)
    registroButton.setOnClickListener {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    val inicioButton = findViewById<ButtonR.id.inicioButton)
    inicioButton.setOnClickListener {
        val intent1 = Intent(this, MainActivity::class.java)
        startActivity(intent1)
    }*/

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle
            // the Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentManager, li: Lifecycle) :
        FragmentStateAdapter(fa, li) {
        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            return if (position == 0)
                LoginFragment()
            else
                MenorLoginFragment()
        }
    }

}