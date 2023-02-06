package com.he_ami.kalkulatorplata

import com.he_ami.kalkulatorplata.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.DecimalFormat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class NetoBruto : AppCompatActivity() {

    // Google AdMob (Banner) =========================
    lateinit var mAdView1 : AdView
    lateinit var mAdView2 : AdView
    //================================================

    var result: TextView? = null
    //val dec = DecimalFormat(".00")
    val bhFormat = DecimalFormat("#,###.00")

    var porez_proc_fbih = ""
    // . . .

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neto_bruto)

        // Initialize Google Ads
        MobileAds.initialize(this) { }
        // Load banner ads and set the adUnitId (defined in values/strings.xml)
        loadBannerAds()

        result = findViewById<TextView>(R.id.rezultat_bruto)
        listaEntiteta()
    }

    fun loadBannerAds() {
        mAdView1 = findViewById(R.id.adView1)
        val adRequest1 = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest1)

        // . . .
    }

    fun listaEntiteta() {
        var izabrani_entitet: String = ""
        var entitet = resources.getStringArray(R.array.entitet)
        var dropdown = findViewById<Spinner>(R.id.dropdown)

        if (dropdown != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, entitet)
            dropdown.adapter = adapter

            dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    izabrani_entitet = entitet[position]
                    racunajNetoBruto(izabrani_entitet)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // u slucaju da nije izabrano nista
                }
            }
        }
    }

    fun racunajNetoBruto(entitet: String) {
        println("Racuna se za entitet: $entitet")

        var Bruto_calc: Button = findViewById<Button>(R.id.Bruto_calc)

        Bruto_calc.setOnClickListener {
            var neto_unos = findViewById<EditText>(R.id.neto_unos).text.toString()
            var p_olaksica = findViewById<EditText>(R.id.p_olaksica).text.toString()
            var minuli = findViewById<EditText>(R.id.minuli).text.toString()

            var poreska_olaksica: Int
            var godine_staza: Int
            // . . .

            if (neto_unos != "") {
                var neto_ulaz = neto_unos.toDouble()

                if (p_olaksica != "") {
                    poreska_olaksica = p_olaksica.toInt()
                } else {
                    poreska_olaksica = 0
                }

                if (minuli != "") {
                    godine_staza = minuli.toInt()
                } else {
                    godine_staza = 0
                }

                neto_input = neto_ulaz

                if (entitet == "FBIH") {
                    var koef_fbih = getString(R.string.koef_fbih)
                    porez_proc_fbih = getString(R.string.porez_proc_fbih)
                    var minuli_fbih = getString(R.string.minuli_fbih)

                    if ((neto_input - poreska_olaksica) > 0) {
                        //var bruto = ((neto_input * koef_fbih.toDouble()) - . . .

                    } else {
                        var bruto = neto_input * // . . .

                    }
                    //bruto_bez_staza = bruto - (bruto * godine_staza * . . .

                } else if (entitet == "RS") {
                    // . . .

                // else if . . .
                    // . . .

                } else {
                    println("Javila se greska pri izboru.")
                }
                result?.setText( (bhFormat.format(bruto_bez_staza)).toString() + " KM" )

            } else {
                result?.setText("Molimo unesite Neto platu.")
            }
        }
    }
} //end of class
