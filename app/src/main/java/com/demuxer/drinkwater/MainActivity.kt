package com.demuxer.drinkwater

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import com.demuxer.drinkwater.util.CONGRATULATIONS
import com.demuxer.drinkwater.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var viewModel: MainViewModel
    private lateinit var tts: TextToSpeech

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        // text to speech
        tts = TextToSpeech(applicationContext
        ) { status ->
            if (status != TextToSpeech.ERROR)
                tts.language = Locale.UK
        }

        // view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.percent.observe(this, Observer {percent ->
            "$percent%".also { percentText.text = it }

            if (percent >= 100F)
            {
                congratulationsTxt.text = CONGRATULATIONS
                tts.speak(CONGRATULATIONS, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            else
                congratulationsTxt.text = null
        })

        viewModel.totalWater.observe(this, Observer { total ->
            "Quantidade total: $total L".also { qtToDrink.text = it }
        })

        // settings activity
        if (viewModel.getTotalWater() == 0F) {
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

            R.id.notificationMenuItem -> {
                NotificationActivity.start(this)
                true
            }

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