package adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import domain.Evento;
import project.myapplication.R;

public class CustomListViewEvento extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Evento> mEventos;
    private Context mContext;

    public CustomListViewEvento(Context mContext, List<Evento> mEventos) {
        this.mContext = mContext;
        this.mEventos = mEventos;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mEventos.size();
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
        Evento mEvento = mEventos.get(position);
        View view = convertView;

        if (convertView == null)
            view = mLayoutInflater.inflate(R.layout.item_menu_evento_card, null);

        final ImageView ivEvento = (ImageView) view.findViewById(R.id.ivEvento);
        TextView tvEvento = (TextView) view.findViewById(R.id.tvEvento);
        TextView tvConvidados = (TextView) view.findViewById(R.id.tvConvidados);
        TextView tvComentarios = (TextView) view.findViewById(R.id.tvComentarios);

        tvEvento.setText(mEvento.getTituloEvento());
        tvConvidados.setText(String.valueOf(mEvento.getConvidados()));
        tvComentarios.setText(String.valueOf(mEvento.getComentarios()));

        if (mEvento.getImagemFotoCapa() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(mEvento.getImagemFotoCapa(), 0, mEvento.getImagemFotoCapa().length);
            ivEvento.setImageBitmap(bitmap);
        } else {
            final String url = mContext.getString(R.string.caminho_foto_capa_evento) + mEvento.getCodigoEvento() + ".png";
            Picasso.with(ivEvento.getContext()).load(url).placeholder(R.drawable.ic_placeholder_evento).into(ivEvento);
        }

        return view;
    }

    public int getCodigoEvento(int position) {
        Evento mEvento = mEventos.get(position);
        return mEvento.getCodigoEvento();
    }
}
