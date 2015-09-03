package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import domain.Endereco;
import project.myapplication.R;

public class CustomListViewEndereco extends BaseAdapter{

    LayoutInflater inflater;
    List<Endereco> items;
    Geocoder geocoder;
    Context context;

    public CustomListViewEndereco(Activity context, List<Endereco> items) {
        super();
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.geocoder = new Geocoder(context);
        this.context = context;
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
        View view =convertView;

        final Endereco endereco = items.get(position);
        final String url = endereco.getUrlIcon();


        if(convertView==null)
            view = inflater.inflate(R.layout.item_menu_endereco, null);

        final ImageView img = (ImageView) view.findViewById(R.id.imgThumbnail);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);

        Thread thread = new Thread(){
          public void run()
          {
              try {
                  img.setImageDrawable(Drawable.createFromStream((InputStream)new URL(url).getContent(), "src"));
              } catch (Exception e) {
                  e.printStackTrace();
                  Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
              }
          }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        txtTitle.setText(endereco.getNome());
        txtSubTitle.setText(endereco.getEndereco());

        return view;
    }

    public double getLatitude(int postion)
    {
        Endereco item = items.get(postion);
        return item.getLatitude();
    }
}
