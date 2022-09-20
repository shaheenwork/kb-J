package com.example.j


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*


class ImageListActivity : AppCompatActivity() {
    private lateinit var options: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            "kbfc",
            Context.MODE_PRIVATE
        )

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("ad", false)
        editor.apply()
        editor.commit()




        setContentView(R.layout.activity_imag_list)



        val mAdView = findViewById<View>(R.id.adview) as AdView
        val adRequest = AdRequest.Builder()
          //  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build()

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setActionBar(toolbar)
        // supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        options = findViewById(R.id.options)

        options.setOnClickListener {
            showPopup()
        }

        val arrayList: ArrayList<ImageModel> = ArrayList<ImageModel>()

        arrayList.add(ImageModel(R.drawable.s, "ladder", 0.4f, 0f, 0f, 0f, 0))
        arrayList.add(ImageModel(R.drawable.s, "ladder", 0.4f, 0f, 0f, 0f, 0))
        arrayList.add(ImageModel(R.drawable.s, "ladder", 0.4f, 0f, 0f, 0f, 0))
        arrayList.add(ImageModel(R.drawable.s, "ladder", 0.4f, 0f, 0f, 0f, 0))

        val recyclerView = findViewById<View>(R.id.rv) as RecyclerView
        val adapter = ImagesAdapter(arrayList, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


    }

    private fun showPopup() {
        val popupMenu = PopupMenu(this@ImageListActivity, options)

        // Inflating popup menu from popup_menu.xml file

        // Inflating popup menu from popup_menu.xml file
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem -> // Toast message on menu item clicked

            if (menuItem.itemId == R.id.more_apps) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://search?q=pub:SHN+Apps")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/search?q=pub:SHN+Apps")
                        )
                    )
                }
            } else if (menuItem.itemId == R.id.rate_us) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.shnapps.kbfc.fan")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.shnapps.kbfc.fan")
                        )
                    )
                }
            }


            true
        }
        // Showing the popup menu
        // Showing the popup menu
        popupMenu.show()
    }


}