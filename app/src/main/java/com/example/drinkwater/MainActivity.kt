package com.example.drinkwater

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.drinkwater.util.DiaryHelper
import com.example.drinkwater.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var viewModel: MainViewModel

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        // view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.percent.observe(this, Observer {
            percentText.text = "$it%"
        })

        if (DiaryHelper.getTotalWater(this) == 0F) {
            SettingsActivity.start(this)
        }

        // toolbar
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav_drawer, R.string.close_nav_drawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // nav view
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // drink water button
        val drinkWaterBtn = findViewById<Button>(R.id.drinkWaterBtn)
        drinkWaterBtn.setOnClickListener {
            viewModel.incrementWater()

            Toast.makeText(this, getString(R.string.water_drank), Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.aboutMenuItem -> {
                AboutActivity.start(this)
                true
            }

            R.id.notificationMenuItem -> true

            R.id.historyMenuItem -> {
                HistoryActivity.start(this)
                true
            }

            R.id.homeMenuItem -> {
                viewModel.clearValues()
                SettingsActivity.start(this)
                true
            }

            else -> true
        }
    }
}