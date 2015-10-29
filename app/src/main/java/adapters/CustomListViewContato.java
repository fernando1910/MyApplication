package adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import domain.Contatos;
import extras.RoundImage;
import project.myapplication.R;

public class CustomListViewContato extends BaseAdapter {

    LayoutInflater inflater;
    List<Contatos> items;
    Context context;
    boolean cbContatoVisivel;
    private CheckBox cbContato;

    public CustomListViewContato(Activity context, List<Contatos> items, boolean cbContatoVisivel) {
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        this.context = context;
        this.cbContatoVisivel = cbContatoVisivel;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contatos item = items.get(position);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
        RoundImage roundedImage  = new RoundImage(bm);

        View view =  convertView;

        if (cbContatoVisivel) {
            view = inflater.inflate(R.layout.item_menu_contato_convidar, null);
            cbContato = (CheckBox) view.findViewById(R.id.cbContato);
            cbContato.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cbContato.isClickable()) {
                                if (item.getSelecionado())
                                    item.setSelecionado(false);
                                else
                                    item.setSelecionado(true);
                            }

                        }
                    }
            );
        }
        else{
            view = inflater.inflate(R.layout.item_menu_contato, null);
        }

        final ImageView img = (ImageView) view.findViewById(R.id.imgThumbnail);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        final String url = context.getString(R.string.caminho_foto_perfil) + String.valueOf(item.getCodigoContato()) + ".png";
        img.setImageDrawable(roundedImage);


        Thread thread = new Thread(){
            public void run()
            {
                try {
                    Drawable drawable = (Drawable.createFromStream((InputStream) new URL(url).getContent(), "src"));
                    RoundImage roundedImage  = new RoundImage(((BitmapDrawable)drawable).getBitmap());
                    img.setImageDrawable(roundedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        //TextView txtSubTitle = (TextView) vi.findViewById(R.id.txtSubTitle);



        //img.setImageResource(item.t);
        txtTitle.setText(item.getNomeContato());
        //txtSubTitle.setText(item.SubTitle);


        return view;
    }

    public int getValue(int position)
    {
        Contatos objContatos = items.get(position);
        return objContatos.getCodigoContato();
    }

    public boolean isChecked(int position)
    {
        Contatos objContatos = items.get(position);
        return objContatos.getSelecionado();
    }

}
