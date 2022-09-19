package com.example.j;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ImagesAdapter extends ArrayAdapter<ImageModel> {

    public ImagesAdapter(@NonNull Context context, ArrayList<ImageModel> imagesModelArrayList) {
        super(context, 0, imagesModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
        }

        ImageModel imageModel = getItem(position);
        ImageView courseIV = listitemView.findViewById(R.id.image);

        courseIV.setImageResource(imageModel.getImage_id());
        return listitemView;
    }
}