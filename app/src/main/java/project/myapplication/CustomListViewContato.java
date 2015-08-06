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

public class CustomListViewContato extends BaseAdapter {

    LayoutInflater inflater;
    List<clsContatos> items;

    public CustomListViewContato(Activity context, List<clsContatos> items) {
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        return items.size();
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
        clsContatos item = items.get(position);


        View view =  convertView;

        if(convertView==null)
            view = inflater.inflate(R.layout.item_menu_contato, null);
        ImageView img = (ImageView) view.findViewById(R.id.imgThumbnail);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        //TextView txtSubTitle = (TextView) vi.findViewById(R.id.txtSubTitle);

        //img.setImageResource(item.t);
        txtTitle.setText(item.getNomeContato());
        //txtSubTitle.setText(item.SubTitle);


        return view;
    }
}
