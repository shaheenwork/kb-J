package com.shaheen.kbj.adapter

import android.content.Context
import com.shaheen.kbj.model.ImageModel
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.shaheen.kbj.R
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.shaheen.kbj.activity.JerseyEditActivity
import androidx.cardview.widget.CardView
import java.util.ArrayList

class ImagesAdapter     // RecyclerView recyclerView;
    (private val listdata: ArrayList<ImageModel>, private val context: Context) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.item_image, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myListData = listdata[position]
        holder.imageView.setImageResource(myListData.display_image_id)
        holder.root.setOnClickListener {
            val intent = Intent(context, JerseyEditActivity::class.java)
            intent.putExtra("item", myListData)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var root: CardView

        init {
            imageView = itemView.findViewById<View>(R.id.image) as ImageView
            this.root = itemView.findViewById<View>(R.id.root) as CardView
        }
    }
}