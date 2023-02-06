package com.he_ami.kalkulatorplata

import com.he_ami.kalkulatorplata.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.text.DecimalFormat


class BrutoNeto : AppCompatActivity() {

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
        setContentView(R.layout.activity_bruto_neto)

        // Initialize Google Ads
        MobileAds.initialize(this) { }
        // Load banner ads and set the adUnitId (defined in values/strings.xml)
        loadBannerAds()

        result = findViewById<TextView>(R.id.rezultat_neto)
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
                    racunajBrutoNeto(izabrani_entitet)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // u slucaju da nije izabrano nista
                }
            }
        }
    }

    fun racunajBrutoNeto(entitet: String) {
        println("Računa se za entitet: $entitet")

        var Neto_calc: Button = findViewById<Button>(R.id.Neto_calc)

        Neto_calc.setOnClickListener {
            var bruto_unos = findViewById<EditText>(R.id.bruto_unos).text.toString()
            var p_olaksica = findViewById<EditText>(R.id.p_olaksica).text.toString()
            var minuli = findViewById<EditText>(R.id.minuli).text.toString()

            minuli_fbih = getString(R.string.minuli_fbih)
            minuli_rs_bd = getString(R.string.minuli_rs_bd)
            // . . .

            if (bruto_unos != "") {
                var bruto_ulaz = bruto_unos.toDouble()

                if (p_olaksica != "") {
                    var poreska_olaksica = p_olaksica.toInt()
                } else {
                    var poreska_olaksica = 0
                }

                if (minuli != "") {
                    var godine_staza = minuli.toInt()
                } else {
                    var godine_staza = 0
                }

                // . . .

                if (entitet == "FBIH") {
                    var koef_fbih = getString(R.string.koef_fbih)
                    porez_proc_fbih = getString(R.string.porez_proc_fbih)
                    //var porezTest = (neto_oporezivo - poreska_olaksica)
                    var porezTest: Int = 0

                    if (porezTest < 0) {
                        var porez = 0.00
                    } else {
                        var porez = porezTest * // . . .
                    }
                    // neto_na_ruke = . . .

                } else if (entitet == "RS") {
                    // . . .

                // else if . . .
                    // . . .

                } else {
                    println("Javila se greska pri izboru.")
                }
                result?.setText( (bhFormat.format(neto_na_ruke)).toString() + " KM" )

            } else {
                result?.setText("Molimo unesite Bruto platu.")
            }
        }
    }
} //end of class
