package com.example.j

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity


class ImageListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imag_list)




        val gridView:GridView = findViewById<View>(R.id.idGV) as GridView
        val arrayList: ArrayList<ImageModel> = ArrayList<ImageModel>()

        arrayList.add(ImageModel(R.drawable.s,"ladder",0.4f,0f,0f,0f,0))

        val adapter = ImagesAdapter(this, arrayList)
        gridView.setAdapter(adapter)

        gridView.onItemClickListener =
            OnItemClickListener { parent, v, position, id ->
                val intent = Intent(this@ImageListActivity,MainActivity::class.java)
                intent.putExtra("item",arrayList.get(position))
                startActivity(intent)
            }




    }
}