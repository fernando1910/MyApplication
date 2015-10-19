package adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import domain.Evento;
import project.myapplication.R;

public class CustomListViewRanking extends BaseAdapter {
    LayoutInflater mInflater;
    List<Evento> mItems;

    public CustomListViewRanking(Context context, List<Evento> items) {
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        this.mItems = items;
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
                view = mInflater.inflate(R.layout.item_menu_ranking,null);

        ImageView mImageView = (ImageView) view.findViewById(R.id.ivCapaEvento);
        TextView mTextView = (TextView) view.findViewById(R.id.tvTituloEvento);
        RatingBar mRatingBar = (RatingBar) view.findViewById(R.id.rbClassificacao);

        //mImageView.setImageResource(mItem.getCodigoEvento());
        mTextView.setText(mItem.getTituloEvento());
        mRatingBar.setRating(mItem.getClassificacao());

        return view;
    }

    public int getCodigoEvento(int postion){
        Evento objEvento = mItems.get(postion);
        return objEvento.getCodigoEvento();
    }
}
