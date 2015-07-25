package project.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewEvento extends BaseAdapter {

    LayoutInflater inflater;
    List<menuEvento> evento;

    public CustomListViewEvento(Activity context, List<menuEvento> evento) {
        this.evento = evento;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return evento.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        menuEvento eventos = evento.get(position);
        View view =convertView;

        if(convertView==null)
            view = inflater.inflate(R.layout.item_menu_evento, null);
        ImageView img = (ImageView) view.findViewById(R.id.imgThumbnail);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);

        //img.setImageResource(eventos.get);
        txtTitle.setText(eventos.getTitulo());
        //txtSubTitle.setText(eventos.);


        return view;

    }

    public int getValue(int position)
    {
        menuEvento eventos = evento.get(position);
        return eventos.getValue();
    }
}
