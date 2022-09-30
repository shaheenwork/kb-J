package com.shnapps.kbfcjersey.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shnapps.kbfcjersey.Utils.PermissionHelper
import com.shnapps.kbfcjersey.model.ImageModel
import com.wooplr.spotlight.SpotlightConfig
import com.wooplr.spotlight.utils.SpotlightSequence
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import com.shnapps.kbfcjersey.R


class JerseyEditActivity : AppCompatActivity(), OnUserEarnedRewardListener {
    private lateinit var nameTextView: TextView
    private lateinit var numberTextView: TextView
    private lateinit var name_top_margin: View
    private lateinit var number_top_margin: View
    private lateinit var number_left_margin: View
    private lateinit var name_left_margin: View
    private lateinit var moveFAB: FloatingActionButton
    private lateinit var imageView: ImageView
    private var x_name = 0;
    private var x_num = 0;
    private var y_name = 0;
    private var y_num = 0;

    private var movable = false

    lateinit var root: ConstraintLayout

    private var item: ImageModel? = null
    private lateinit var backBTN: ImageView
    private lateinit var saveBTN: ImageView
    private lateinit var shareBTN: ImageView
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    private lateinit var permissions: Array<String>
    private var isShare: Boolean = false

    private var PERMISSION_CODE = 100
    lateinit var mAdView2: AdView
    lateinit var toolbar: Toolbar

    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    private var reward: Boolean = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadRewAd()

        loadInterstitial()

        interstitialCallbacks()


        // Banner Ad
        mAdView2 = findViewById(R.id.adview)
        val adRequest2: AdRequest = AdRequest.Builder().build()
        mAdView2.loadAd(adRequest2)

        initViews()

        permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


        onClicks()



        item = intent.getParcelableExtra("item")
        imageView.setImageResource(item!!.image_id)

        setNameAndNumber(item!!.imageNAme)


    }

    private fun setNameAndNumber(imageNAme: String) {


        val num_typeface: Typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.f_num)
        } else {
            ResourcesCompat.getFont(this@JerseyEditActivity, R.font.f_num)!!
        }
        val name_typeface: Typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.name_font)
        } else {
            ResourcesCompat.getFont(this@JerseyEditActivity, R.font.name_font)!!
        }

        when (imageNAme) {
            "1" -> {
                nameTextView.setTextColor(resources.getColor(R.color.black))
                numberTextView.setTextColor(resources.getColor(R.color.black))
            }
            "2" -> {
                nameTextView.setTextColor(resources.getColor(R.color.white))
                numberTextView.setTextColor(resources.getColor(R.color.white))

                numberTextView.typeface = num_typeface
                nameTextView.typeface = name_typeface

                nameTextView.textSize = 22f
                numberTextView.textSize = 90f
            }
            "3" -> {
                nameTextView.setTextColor(resources.getColor(R.color.blue))
                numberTextView.setTextColor(resources.getColor(R.color.blue))

                numberTextView.typeface = num_typeface
                nameTextView.typeface = name_typeface

                nameTextView.textSize = 22f
                numberTextView.textSize = 90f
            }
            "4" -> {
                nameTextView.setTextColor(resources.getColor(R.color.blue))
                numberTextView.setTextColor(resources.getColor(R.color.blue))

                numberTextView.typeface = num_typeface
                nameTextView.typeface = name_typeface

                nameTextView.textSize = 22f
                numberTextView.textSize = 90f
            }
        }

        val lp_name_top = name_top_margin.layoutParams as ConstraintLayout.LayoutParams
        lp_name_top.matchConstraintPercentHeight = item!!.name_top
        name_top_margin.layoutParams = lp_name_top

        /* val lp_name_left = name_left_margin.layoutParams as ConstraintLayout.LayoutParams
         lp_name_left.matchConstraintPercentWidth = item!!.name_left
         name_left_margin.layoutParams = lp_name_left*/


        val lp_number_top = number_top_margin.layoutParams as ConstraintLayout.LayoutParams
        lp_number_top.matchConstraintPercentHeight = item!!.number_top
        number_top_margin.layoutParams = lp_number_top


        /*  val lp_number_left = number_left_margin.layoutParams as ConstraintLayout.LayoutParams
          lp_number_left.matchConstraintPercentWidth = item!!.number_left
          number_left_margin.layoutParams = lp_number_left*/
    }


    private fun loadRewAd() {
        RewardedInterstitialAd.load(this, "ca-app-pub-2416033626083993/7833095973",
            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedInterstitialAd = ad

                    rewardedInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.")

                                if (reward) {
                                    generateImage(root, isShare)
                                    rewardedInterstitialAd = null
                                }
                                loadRewAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                rewardedInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.")
                            }
                        }


                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    rewardedInterstitialAd = null
                }

            })
    }


    private fun onClicks() {
        backBTN.setOnClickListener {
            super.onBackPressed()
        }

        saveBTN.setOnClickListener {

            isShare = false

            reward = false

            if (checkPermissions(permissions)) {

                if (rewardedInterstitialAd != null) {
                    saveDialog()
                } else {
                    generateImage(root, false)
                }

            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
            }

        }

        shareBTN.setOnClickListener {

            isShare = true
            reward = false

            if (checkPermissions(permissions)) {
                if (rewardedInterstitialAd != null) {
                    saveDialog()
                } else {
                    generateImage(root, true)
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
            }
        }

        nameTextView.setOnClickListener {

            showNameDialog()

        }
        numberTextView.setOnClickListener {

            showNumberDialog()

        }


        moveFAB.setOnClickListener {
            movable = !movable
            movable()
        }
    }

    private fun saveDialog() {

        var msg = ""
        var title = ""
        var positive = ""
        var negetive = ""
        if (isShare) {
            msg = "Watch an Ad to share this jersey"
            title = "Share Jersey ?"

            positive = "Share"
            negetive = "Don't Share"


        } else {

            msg = "Watch an Ad to save this jersey"
            title = "Save Jersey ?"
            positive = "Save"
            negetive = "Don't Save"


        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton(positive) { dialog, id ->
                if (rewardedInterstitialAd != null) {
                    rewardedInterstitialAd!!.show(this, this)
                } else {
                    loadRewAd()
                    generateImage(root, isShare)
                }
            }
            .setNegativeButton(
                negetive
            ) { dialog, id -> dialog.cancel() }
            .create()
            .show()
    }

    fun checkPermissions(permissions: Array<String>): Boolean {
        var mRuntimePermissions = PermissionHelper(permissions, this)
        var gotPermissions = mRuntimePermissions.hasPermissions()

        return gotPermissions
    }

    private fun initViews() {
        root = findViewById(R.id.root)
        toolbar = findViewById(R.id.toolbar)
        nameTextView = findViewById(R.id.nameTextView)
        numberTextView = findViewById(R.id.numberTextView)
        saveBTN = findViewById(R.id.save)
        shareBTN = findViewById(R.id.share)
        backBTN = findViewById(R.id.back)
        moveFAB = findViewById(R.id.move_fab)
        name_top_margin = findViewById(R.id.name_top)
        name_left_margin = findViewById(R.id.name_left)
        number_top_margin = findViewById(R.id.number_top)
        number_left_margin = findViewById(R.id.number_left)
        imageView = findViewById(R.id.imageView)
    }

    private fun interstitialCallbacks() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null

                showSpotlight()


            }


            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    private fun showSpotlight() {
        val config = SpotlightConfig()
        config.headingTvColor = resources.getColor(R.color.colorText)
        config.subHeadingTvColor = resources.getColor(R.color.white)
        config.lineAndArcColor = resources.getColor(R.color.colorText)
        config.maskColor = resources.getColor(R.color.colorMask)
        config.isPerformClick = false
        SpotlightSequence.getInstance(this@JerseyEditActivity, config)
            .addSpotlight(nameTextView, "Name", "Tap to change name ", "1")
            .addSpotlight(numberTextView, "Number ", "Tap to change number", "2")
            .addSpotlight(moveFAB, "Move", "Tap to change position of name and number", "3")
            .addSpotlight(saveBTN, "Save", "Save created jersey", "4")
            .addSpotlight(shareBTN, "Share", "Share created jersey", "5")
            .startSequence();
    }

    private fun loadInterstitial() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-2416033626083993/9766190409",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    mInterstitialAd = null
                    showSpotlight()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    interstitialCallbacks()

                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@JerseyEditActivity)
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }

            })

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun movable() {
        if (movable) {
            moveFAB.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.grey));
            moveFAB.setImageResource(R.drawable.ic_edit)

            if (item!!.imageNAme.equals("1")) {
                nameTextView.setBackgroundResource(R.drawable.rectangle)
                numberTextView.setBackgroundResource(R.drawable.rectangle)
            } else if (item!!.imageNAme.equals("2")) {
                nameTextView.setBackgroundResource(R.drawable.rectangle_white)
                numberTextView.setBackgroundResource(R.drawable.rectangle_white)
            } else if (item!!.imageNAme.equals("3")) {
                nameTextView.setBackgroundResource(R.drawable.rectangle)
                numberTextView.setBackgroundResource(R.drawable.rectangle)
            } else if (item!!.imageNAme.equals("4")) {
                nameTextView.setBackgroundResource(R.drawable.rectangle)
                numberTextView.setBackgroundResource(R.drawable.rectangle)
            }

            nameTextView.setOnTouchListener(OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x_name = (v.x - event.rawX).toInt()
                        y_name = (v.y - event.rawY).toInt()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        nameTextView.animate()
                            .x(event.rawX + x_name)
                            .y(event.rawY + y_name)
                            .setDuration(0)
                            .start()
                        true
                    }
                    else -> true
                }
            })

            numberTextView.setOnTouchListener(OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x_num = (v.x - event.rawX).toInt()
                        y_num = (v.y - event.rawY).toInt()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        numberTextView.animate()
                            .x(event.rawX + x_num)
                            .y(event.rawY + y_num)
                            .setDuration(0)
                            .start()
                        true
                    }
                    else -> true
                }
            })


        } else {

            moveFAB.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.fab));
            moveFAB.setImageResource(R.drawable.ic_move)
            nameTextView.setBackgroundResource(R.drawable.no_rectangle)
            numberTextView.setBackgroundResource(R.drawable.no_rectangle)


            nameTextView.setOnTouchListener(null)
            numberTextView.setOnTouchListener(null)
        }
    }

    private fun generateImage(root: ConstraintLayout, share: Boolean) {

        makeOthersInvisible()

        val bitmap = getScreenShotFromView(root)
        if (bitmap != null) {
            saveMediaToStorage(bitmap, share)
        }
        makeOthersvisible()
    }

    private fun makeOthersInvisible() {
        moveFAB.visibility = View.GONE
        mAdView2.visibility = View.GONE
        toolbar.visibility = View.GONE
    }

    private fun makeOthersvisible() {
        moveFAB.visibility = View.VISIBLE
        mAdView2.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
    }


    fun showNameDialog() {
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
            .create()
        val view = layoutInflater.inflate(R.layout.name_dialog, null)
        val button = view.findViewById<Button>(R.id.ok_button)
        val name: EditText = view.findViewById<EditText>(R.id.nameEditText)
        name.setText(nameTextView.text)
        name.setSelectAllOnFocus(true)
        name.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        builder.setView(view)
        button.setOnClickListener {
            if (name.text.isEmpty()) {
                nameTextView.text = "NAME"
            } else {
                nameTextView.text = name.text.toString()
            }
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.show()

    }

    fun showNumberDialog() {
        val builder = AlertDialog.Builder(this, R.style.dialog_theme)
            .create()
        val view = layoutInflater.inflate(R.layout.number_dialog, null)
        val button = view.findViewById<Button>(R.id.ok_button)
        val number = view.findViewById<EditText>(R.id.nameEditText)
        number.setText(numberTextView.text)
        number.setSelectAllOnFocus(true)
        number.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        builder.setView(view)
        button.setOnClickListener {

            if (number.text.isEmpty()) {
                numberTextView.text = "12"
            } else {
                numberTextView.text = number.text
            }
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.show()

    }

    fun shareBitmapToApps(bitmap: Bitmap) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "image/*"
        i.putExtra(
            Intent.EXTRA_STREAM,
            getImageUri(this@JerseyEditActivity, bitmap)
        )
        i.putExtra(Intent.EXTRA_TEXT, "app url");
        try {
            startActivity(Intent.createChooser(i, "Share"))
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "KBFC",
            "null"
        )
        return Uri.parse(path)
    }


    private fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {

            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }

        return screenshot
    }


    private fun saveMediaToStorage(bitmap: Bitmap, share: Boolean) {

        val filename = "${System.currentTimeMillis()}.jpg"

        var fos: OutputStream? = null


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->


                val contentValues = ContentValues().apply {

                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }


                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            if (!share) {
                Toast.makeText(this, "Your Jersey Saved to Gallery", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (share) {
            shareBitmapToApps(bitmap)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                checkPermissions(permissions)
            }
        }

        if ((permissions.isNotEmpty()) && (!grantResults.contains(PackageManager.PERMISSION_DENIED))) {

            if (rewardedInterstitialAd!=null){
                saveDialog()
            }
            else {
                generateImage(root, isShare)
            }
        }
    }

    override fun onUserEarnedReward(p0: RewardItem) {

        reward = true
        loadRewAd()

    }

}