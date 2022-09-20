package com.example.j


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
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
import com.google.android.gms.ads.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    private var x_name = 0;
    private var x_num = 0;
    private var y_name = 0;
    private var y_num = 0;
    private var movable = false
    lateinit var root: ConstraintLayout

    private var item: ImageModel? = null
    private lateinit var backBTN:ImageView
    private lateinit var saveBTN:ImageView
    private lateinit var shareBTN:ImageView
    final var TAG = "MainActivity"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mAdView2:AdView = findViewById(R.id.adview)
        val adRequest2:AdRequest = AdRequest.Builder().build()
        mAdView2.loadAd(adRequest2)


        root = findViewById(R.id.root)
        nameTextView = findViewById(R.id.nameTextView)
        numberTextView = findViewById(R.id.numberTextView)
        saveBTN = findViewById(R.id.save)
        shareBTN = findViewById(R.id.share)
        backBTN = findViewById(R.id.back)
        moveFAB = findViewById(R.id.move_fab)



        backBTN.setOnClickListener{
            super.onBackPressed()
        }

        saveBTN.setOnClickListener{




        }

        shareBTN.setOnClickListener{
            generateImage(root,true)
        }


        name_margin = findViewById(R.id.v1)
        number_margin = findViewById(R.id.v2)

        imageView = findViewById(R.id.imageView)



        item = intent.getParcelableExtra("item")


        imageView.setImageResource(item!!.image_id)


        val lp = name_margin.layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentHeight = item!!.top_percent
        name_margin.layoutParams = lp



        nameTextView.setOnClickListener {

            showNameDialog()

        }
        numberTextView.setOnClickListener {

            showNumberDialog()

        }

       /* val buttonsave: Button = findViewById(R.id.buttonSave)

        buttonsave.setOnClickListener {

            nameTextView.setBackgroundResource(R.drawable.no_rectangle)
            numberTextView.setBackgroundResource(R.drawable.no_rectangle)
            generateImage(root)

        }*/

        moveFAB.setOnClickListener {
            movable = !movable
            movable()
        }









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

    private fun generateImage(root: ConstraintLayout,share:Boolean) {
        val vto: ViewTreeObserver = root.viewTreeObserver
        /*vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                root.viewTreeObserver.removeGlobalOnLayoutListener(this)*/
        val bitmap = getScreenShotFromView(root)
        if (bitmap != null) {
            saveMediaToStorage(bitmap,share)
        }
        /*   }
       })*/
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
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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

            if (number.text.isEmpty()){
                numberTextView.text = "12"
            }
            else{
                numberTextView.text = number.text
            }
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.show()

    }

    fun share_bitMap_to_Apps(bitmap: Bitmap) {
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
        // create a bitmap object
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


    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap, share: Boolean) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            if (!share) {
                Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (share){
            share_bitMap_to_Apps(bitmap)
        }

    }
}