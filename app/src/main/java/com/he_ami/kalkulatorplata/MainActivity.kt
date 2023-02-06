package com.he_ami.kalkulatorplata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import java.util.*

import com.he_ami.kalkulatorplata.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    // Google AdMob ====================================
    // === Banner ===
    //lateinit var mAdView : AdView
    // === Interstitial ===
    private var interstitialAd: InterstitialAd? = null
    private val TAG = "MainActivity"
    //==================================================

    // Default values ==================================
    var zenMastersBannerUrl: String = ""
    var buttonTimerEnablesButtons: Boolean = false
    var buttonTimerWaitingTime: Int = 0
    var TOAST_TEXT = "Loading..."
    var currentActivity: String = ""
    var adShown: Boolean = false
    private lateinit var brutoButton: Button
    private lateinit var netoButton: Button
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Google Ads
        MobileAds.initialize(this) { }
        // Load the InterstitialAd and set the adUnitId (defined in values/strings.xml)
        loadInterstitialAd()

        // Button logic
        fnButtons()

        /*val buttonClick = findViewById<Button>(R.id.NetoBruto)
        buttonClick.setOnClickListener {
            val Neto_bruto_i = Intent(this, NetoBruto::class.java)
            startActivity(Neto_bruto_i)
        }*/
    }

    fun showMessage(message: String) {
        println(message)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun fnButtons() {
        brutoButton = binding.BrutoNeto
        netoButton = binding.NetoBruto
        brutoButton.isEnabled = false
        netoButton.isEnabled = false

        brutoButton.setOnClickListener {
            currentActivity = "bruto"
            showInterstitial(currentActivity)
        }
        netoButton.setOnClickListener {
            currentActivity = "neto"
            showInterstitial(currentActivity)
        }
        buttonTimerWaitingTime = getString(R.string.button_timer_waiting_time).toInt()
        buttonTimer(buttonTimerWaitingTime)
    }

    fun buttonTimer(s: Int) {
        var sec: Int = s * 1000
        buttonTimerEnablesButtons = getString(R.string.button_timer_enables_buttons).toBoolean()
        if (buttonTimerEnablesButtons) {
            brutoButton.postDelayed(Runnable { brutoButton.isEnabled = true }, sec.toLong())
            netoButton.postDelayed(Runnable { netoButton.isEnabled = true }, sec.toLong())
        }
    }

    fun loadingTimer(s: Int) = runBlocking<Unit> {
        launch {
            var sec: Int = s * 1000
            delay(sec.toLong())
            Toast.makeText(
                this@MainActivity,
                "isteklo $s sekundi", Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun fnGoToUrl (view: View) {
        zenMastersBannerUrl = getString(R.string.zenmasters_banner_url)
        var uriUrl: Uri = Uri.parse(zenMastersBannerUrl)
        var launchBrowser: Intent = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        if (!adShown) showMessage(TOAST_TEXT)
        adShown = true

        InterstitialAd.load(this, getString(R.string.interstitial_ad_unit_id), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    // The interstitialAd reference will be null until an ad is loaded.
                    interstitialAd = ad

                    brutoButton.isEnabled = true
                    netoButton.isEnabled = true

                    //showMessage("onAdLoaded()")

                    ad.setFullScreenContentCallback(
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitialAd = null
                                Log.d(TAG, "The ad was dismissed.")

                                loadInterstitialAd()
                                showInterstitial(currentActivity)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitialAd = null
                                Log.d(TAG, "The ad failed to show.")

                                showInterstitial(currentActivity)
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d(TAG, "The ad was shown.")

                                loadInterstitialAd()
                            }
                        })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.message)
                    interstitialAd = null
                    brutoButton.setEnabled(true)
                    netoButton.setEnabled(true)
                    val error: String = String.format(
                        Locale.ENGLISH,
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain,
                        loadAdError.code,
                        loadAdError.message
                    )
                    println("onAdFailedToLoad() with error: $error")
                    Toast.makeText(
                        this@MainActivity,
                        "onAdFailedToLoad() with error: $error", Toast.LENGTH_SHORT
                    )
                        .show()

                    loadInterstitialAd()
                }
            })
    }

    private fun showInterstitial(operacija: String) {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (interstitialAd != null) {
            interstitialAd!!.show(this)
        } else {
            if (operacija == "bruto") {
                startActivity(Intent(this, BrutoNeto::class.java))
            } else if (operacija == "neto") {
                startActivity(Intent(this, NetoBruto::class.java))
            }
        }
    }
} //end of class
