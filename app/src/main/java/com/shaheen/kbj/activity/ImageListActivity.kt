package com.shaheen.kbj.activity


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.shaheen.kbj.model.ImageModel
import com.shaheen.kbj.adapter.ImagesAdapter
import com.shaheen.kbj.R
import com.shaheen.kbj.Utils.AppClass
import java.util.*


class ImageListActivity : AppCompatActivity() {
    private lateinit var options: ImageView
    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imag_list)

        // banner Ad
        val mAdView: AdView = findViewById(R.id.adview)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setActionBar(toolbar)
        options = findViewById(R.id.options)
        options.setOnClickListener {
            showPopup()
        }

        // jersey list
        val arrayList: ArrayList<ImageModel> = ArrayList<ImageModel>()
        dummyData(arrayList)
        val recyclerView = findViewById<View>(R.id.rv) as RecyclerView
        val adapter = ImagesAdapter(arrayList, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


    }

    private fun dummyData(arrayList: ArrayList<ImageModel>) {
        arrayList.add(
            ImageModel(
                R.drawable.s,
                "ladder",
                0.4f,
                0f,
                0f,
                0f,
                0
            )
        )
        arrayList.add(
            ImageModel(
                R.drawable.s,
                "ladder",
                0.4f,
                0f,
                0f,
                0f,
                0
            )
        )
        arrayList.add(
            ImageModel(
                R.drawable.s,
                "ladder",
                0.4f,
                0f,
                0f,
                0f,
                0
            )
        )
        arrayList.add(
            ImageModel(
                R.drawable.s,
                "ladder",
                0.4f,
                0f,
                0f,
                0f,
                0
            )
        )
    }

    private fun showPopup() {
        val popupMenu = PopupMenu(this@ImageListActivity, options)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            if (menuItem.itemId == R.id.more_apps) {
                moreApps()
            } else if (menuItem.itemId == R.id.rate_us) {
                rateUs()
            }


            true
        }
        popupMenu.show()
    }

    private fun rateUs() {
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

    private fun moreApps() {
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
    }


}