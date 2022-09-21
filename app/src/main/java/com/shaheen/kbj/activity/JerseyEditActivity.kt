package com.shaheen.kbj.activity


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
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shaheen.kbj.R
import com.shaheen.kbj.Utils.PermissionHelper
import com.shaheen.kbj.model.ImageModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class JerseyEditActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var numberTextView: TextView
    private lateinit var name_margin: View
    private lateinit var number_margin: View
    private lateinit var moveFAB: FloatingActionButton
    private lateinit var imageView: ImageView
    private lateinit var permissionHelper: PermissionHelper
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        loadInterstitial()

        interstitialCallbacks()


        // Banner Ad
        val mAdView2: AdView = findViewById(R.id.adview)
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

        val lp = name_margin.layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentHeight = item!!.top_percent
        name_margin.layoutParams = lp


        showCaseName()

    }

    private fun showCaseName() {
        val sequence = TapTargetSequence(this)
            .targets( // This tap target will target the back button, we just need to pass its containing toolbar
                TapTarget.forView(nameTextView, "Tap to change name")
                    .dimColor(R.color.colorPrimaryDark)
                    .outerCircleColor(R.color.colorPrimary)
                    .targetCircleColor(R.color.white)
                    .transparentTarget(true)
                    .textColor(R.color.white)
                    .id(1),  // Likewise, this tap target will target the search button

                TapTarget.forView(
                    numberTextView,
                    "Tap to change number",
                )
                    .dimColor(R.color.colorPrimaryDark)
                    .outerCircleColor(R.color.colorPrimary)
                    .targetCircleColor(R.color.white)
                    .transparentTarget(true)
                    .textColor(R.color.white)
                    .id(2),  // You can also target the overflow button in your toolbar
                TapTarget.forView(
                    moveFAB,
                    "Tap to reposition name or number",
                    "Use it if you are not satisfied with the name or number position"
                )
                    .dimColor(R.color.colorPrimaryDark)
                    .outerCircleColor(R.color.colorPrimary)
                    .targetCircleColor(R.color.white)
                    .transparentTarget(true)
                    .textColor(R.color.white)
                    .id(3),  // This tap target will target our droid buddy at the given target rect
                TapTarget.forView(
                    saveBTN,
                    "Save your jersey",
                )
                    .dimColor(R.color.colorPrimaryDark)
                    .outerCircleColor(R.color.colorPrimary)
                    .targetCircleColor(R.color.white)
                    .transparentTarget(true)
                    .textColor(R.color.white)
                    .id(4),
                TapTarget.forView(
                    shareBTN,
                    "Share your jersey",

                )
                    .dimColor(R.color.colorPrimaryDark)
                    .outerCircleColor(R.color.colorPrimary)
                    .targetCircleColor(R.color.white)
                    .transparentTarget(true)
                    .textColor(R.color.white)
                    .id(3),  // This tap target will target our droid buddy at the given target rect

            )

            .listener(object : TapTargetSequence.Listener {
                // This listener will tell us when interesting(tm) events happen in regards
                // to the sequence
                override fun onSequenceFinish() {
                   /* (findViewById<View>(R.id.educated) as TextView).text =
                        "Congratulations! You're educated now!"*/
                }

                override fun onSequenceStep(lastTarget: TapTarget, targetClicked: Boolean) {
                    Log.d("TapTargetView", "Clicked on " + lastTarget.id())
                }

                override fun onSequenceCanceled(lastTarget: TapTarget) {
                    val dialog = AlertDialog.Builder(this@JerseyEditActivity)
                        .setTitle("Uh oh")
                        .setMessage("You canceled the sequence")
                        .setPositiveButton("Oops", null).show()
                    TapTargetView.showFor(dialog,
                        TapTarget.forView(
                            dialog.getButton(DialogInterface.BUTTON_POSITIVE),
                            "Uh oh!",
                            "You canceled the sequence at step " + lastTarget.id()
                        )
                            .cancelable(false)
                            .tintTarget(false), object : TapTargetView.Listener() {
                            override fun onTargetClick(view: TapTargetView) {
                                super.onTargetClick(view)
                                dialog.dismiss()
                            }
                        })
                }
            })

        sequence.start()
    }

    private fun showCaseNumber() {
        TapTargetView.showFor(
            this,  // `this` is an Activity
            TapTarget.forView(
                numberTextView, "Tap to change number", ""
            ) // All options below are optional
                .outerCircleColor(R.color.colorPrimary) // Specify a color for the outer circle
                .outerCircleAlpha(0.4f) // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white) // Specify a color for the target circle
                .titleTextSize(20) // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white) // Specify the color of the title text
                .descriptionTextSize(10) // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.white) // Specify the color of the description text
                // .textColor(R.color.fab) // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                .dimColor(R.color.black) // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true) // Whether to draw a drop shadow or not
                .cancelable(false) // Whether tapping outside the outer circle dismisses the view
                .tintTarget(true) // Whether to tint the target view's color
                .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
                // .icon(Drawable) // Specify a custom drawable to draw as the target
                .targetRadius(60)
        )  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view) // This call is optional
                showCaseMove()
            }
        }
    }

    private fun showCaseMove() {
        TapTargetView.showFor(
            this,  // `this` is an Activity
            TapTarget.forView(
                moveFAB, "Tap to change position of name and number", ""
            ) // All options below are optional
                .outerCircleColor(R.color.colorPrimary) // Specify a color for the outer circle
                .outerCircleAlpha(0.4f) // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white) // Specify a color for the target circle
                .titleTextSize(20) // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white) // Specify the color of the title text
                .descriptionTextSize(10) // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.white) // Specify the color of the description text
                // .textColor(R.color.fab) // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                .dimColor(R.color.black) // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true) // Whether to draw a drop shadow or not
                .cancelable(false) // Whether tapping outside the outer circle dismisses the view
                .tintTarget(true) // Whether to tint the target view's color
                .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
                // .icon(Drawable) // Specify a custom drawable to draw as the target
                .targetRadius(60)
        )  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view) // This call is optional
                showCaseSave()
            }
        }
    }


    private fun showCaseSave() {
        TapTargetView.showFor(
            this,  // `this` is an Activity
            TapTarget.forView(
                saveBTN, "Save created jersey", ""
            ) // All options below are optional
                .outerCircleColor(R.color.colorPrimary) // Specify a color for the outer circle
                .outerCircleAlpha(0.4f) // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white) // Specify a color for the target circle
                .titleTextSize(20) // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white) // Specify the color of the title text
                .descriptionTextSize(10) // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.white) // Specify the color of the description text
                // .textColor(R.color.fab) // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                .dimColor(R.color.black) // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true) // Whether to draw a drop shadow or not
                .cancelable(false) // Whether tapping outside the outer circle dismisses the view
                .tintTarget(true) // Whether to tint the target view's color
                .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
                // .icon(Drawable) // Specify a custom drawable to draw as the target
                .targetRadius(60)
        )  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view) // This call is optional
                showCaseShare()
            }
        }
    }


    private fun showCaseShare() {
        TapTargetView.showFor(
            this,  // `this` is an Activity
            TapTarget.forView(
                shareBTN, "Share created jersey", ""
            ) // All options below are optional
                .outerCircleColor(R.color.colorPrimary) // Specify a color for the outer circle
                .outerCircleAlpha(0.4f) // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white) // Specify a color for the target circle
                .titleTextSize(20) // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white) // Specify the color of the title text
                .descriptionTextSize(10) // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.white) // Specify the color of the description text
                // .textColor(R.color.fab) // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                .dimColor(R.color.black) // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true) // Whether to draw a drop shadow or not
                .cancelable(false) // Whether tapping outside the outer circle dismisses the view
                .tintTarget(true) // Whether to tint the target view's color
                .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
                // .icon(Drawable) // Specify a custom drawable to draw as the target
                .targetRadius(60)
        )  // Specify the target radius (in dp)
        object : TapTargetView.Listener() {
            // The listener can listen for regular clicks, long clicks or cancels
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view) // This call is optional
            }
        }
    }





    private fun onClicks() {
        backBTN.setOnClickListener {
            super.onBackPressed()
        }

        saveBTN.setOnClickListener {

            isShare = false

            if (checkPermissions(permissions)) {
                generateImage(root, isShare)
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
            }

        }

        shareBTN.setOnClickListener {

            isShare = true

            if (checkPermissions(permissions)) {
                generateImage(root, isShare)
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

    fun checkPermissions(permissions: Array<String>): Boolean {
        var mRuntimePermissions = PermissionHelper(permissions, this)
        var gotPermissions = mRuntimePermissions.hasPermissions()

        return gotPermissions
    }

    private fun initViews() {
        root = findViewById(R.id.root)
        nameTextView = findViewById(R.id.nameTextView)
        numberTextView = findViewById(R.id.numberTextView)
        saveBTN = findViewById(R.id.save)
        shareBTN = findViewById(R.id.share)
        backBTN = findViewById(R.id.back)
        moveFAB = findViewById(R.id.move_fab)
        name_margin = findViewById(R.id.v1)
        number_margin = findViewById(R.id.v2)
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

    private fun loadInterstitial() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd

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
            nameTextView.setBackgroundResource(R.drawable.rectangle)
            numberTextView.setBackgroundResource(R.drawable.rectangle)

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
            nameTextView.setBackgroundResource(R.drawable.no_rectangle)
            numberTextView.setBackgroundResource(R.drawable.no_rectangle)


            nameTextView.setOnTouchListener(null)
            numberTextView.setOnTouchListener(null)
        }
    }

    private fun generateImage(root: ConstraintLayout, share: Boolean) {
        val bitmap = getScreenShotFromView(root)
        if (bitmap != null) {
            saveMediaToStorage(bitmap, share)
        }
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
                nameTextView.text = name.text
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
                Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT)
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

            generateImage(root,isShare)
        }
    }

}