package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import domain.Configuracoes;
import project.myapplication.R;

public class CustomListViewAdapter extends BaseAdapter
{

    LayoutInflater inflater;
    List<Configuracoes.MenuConfiguracao> items;

    public CustomListViewAdapter(Activity context, List<Configuracoes.MenuConfiguracao> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Configuracoes.MenuConfiguracao item = items.get(position);


        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.item_row, null);
            ImageView img = (ImageView) vi.findViewById(R.id.imgThumbnail);
            TextView txtTitle = (TextView) vi.findViewById(R.id.txtTitle);
           //TextView txtSubTitle = (TextView) vi.findViewById(R.id.txtSubTitle);

        img.setImageResource(item.codigo);
        txtTitle.setText(item.Title);
        //txtSubTitle.setText(item.SubTitle);


        return vi;
    }

    public int getValue(final int position)
    {
        Configuracoes.MenuConfiguracao item = items.get(position);
        return item.codigo;
    }

}