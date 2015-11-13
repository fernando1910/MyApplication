package adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import domain.Evento;
import project.myapplication.R;

public class CustomListViewRanking extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Evento> mItems;
    private int ind_ranking;
    private Context context;
    /*
        int_ranking
            0 - Top Classificação
            1 - Top Convidados
            2 - Top Comentarios

    */

    public CustomListViewRanking(Context context, List<Evento> items, int ind_ranking) {
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems = items;
        this.ind_ranking = ind_ranking;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento mItem = mItems.get(position);
        View view = convertView;

        if (convertView == null)
            view = mInflater.inflate(R.layout.item_menu_ranking, null);


        ImageView ivCapaEvento = (ImageView) view.findViewById(R.id.ivCapaEvento);
        TextView mTextView = (TextView) view.findViewById(R.id.tvTituloEvento);
        ImageView ivICone = (ImageView) view.findViewById(R.id.ivIcone);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);

        String url = ivCapaEvento.getContext().getString(R.string.caminho_foto_capa_evento)
                + String.valueOf(mItem.getCodigoEvento())
                + ".png";
        Picasso.with(ivCapaEvento.getContext())
                .load(url)
                .placeholder(R.drawable.ic_placeholder_evento)
                .into(ivCapaEvento);

        //mImageView.setImageResource(mItem.getCodigoEvento());
        mTextView.setText(mItem.getTituloEvento());
        if (ind_ranking == 0) {
            tvInfo.setText(String.valueOf(mItem.getClassificacao()));
            ivICone.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
        }
        else if(ind_ranking == 1){
            ivICone.setImageDrawable(context.getResources().getDrawable(R.drawable.people));
        }
        else if(ind_ranking == 2){
            ivICone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_comentarios_painel));
        }


        return view;
    }

    public int getCodigoEvento(int postion) {
        Evento objEvento = mItems.get(postion);
        return objEvento.getCodigoEvento();
    }
}
