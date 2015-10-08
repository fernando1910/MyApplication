package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import domain.Comentario;
import project.myapplication.R;

public class CustomListViewComentario extends BaseAdapter{
    private Context mContext;
    private List<Comentario> mComentarios;
    private LayoutInflater mLayoutInflater;

    public CustomListViewComentario(Context mContext, List<Comentario> mComentarios) {
        this.mContext = mContext;
        this.mComentarios = mComentarios;
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mComentarios.size();
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
        View view = convertView;
        Comentario mComentario = mComentarios.get(position);

        if (convertView == null)
            view = mLayoutInflater.inflate(R.layout.item_lista_comentario, null);

        TextView tvUsuario = (TextView) view.findViewById(R.id.tvUsuario);
        TextView tvComentario = (TextView) view.findViewById(R.id.tvComentario);

        tvUsuario.setText(mComentario.getUsuarioComentario());
        tvComentario.setText(mComentario.getComentario());

        return view;
    }

    public int getCodigoComentario(int position){
        Comentario mComentario = mComentarios.get(position);
        return mComentario.getCodigoComentario();
    }
}
