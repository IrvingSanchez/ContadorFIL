package com.example.irvingsanchez.contadorfil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by irvingsanchez on 26/09/17.
 */

public class PersonasAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Personas> items;

    public PersonasAdapter(Activity activity, ArrayList<Personas> items) {
        this.activity = activity;
        this.items = items;
    }

    public void addAll(ArrayList<Personas> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.list_item, null);
        }

        Personas dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.titulo);
        title.setText(dir.getTitulo());

        TextView description = (TextView) v.findViewById(R.id.descripcion);
        description.setText(String.valueOf(dir.getTotal()));

        ImageView icono = (ImageView) v.findViewById(R.id.imageLeft);
        //icono.setImageDrawable(activity.getDrawable(R.drawable.ic_tableta));


        return v;
    }
}
