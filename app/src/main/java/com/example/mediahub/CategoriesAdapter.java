package com.example.mediahub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        }

        Category category = categories.get(position);
        TextView titleText = convertView.findViewById(R.id.titleText);

        titleText.setText(category.getTitle());

        if (category.isSelected()) {
            convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.category_item_selected_bg));
        } else {
            convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.category_item_default_bg));
        }

        return convertView;
    }
}
