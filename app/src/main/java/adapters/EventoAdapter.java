package adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import domain.Evento;
import interfaces.RecyclerViewOnClickListenerHack;
import project.myapplication.R;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.MyViewHolder> {

    private List<Evento> listEvento;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public EventoAdapter(Context context, List<Evento> listEvento) {
        this.listEvento = listEvento;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_menu_evento_card, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        myViewHolder.tvEvento.setText(listEvento.get(i).getTituloEvento());
        final String url = listEvento.get(i).getUrlFoto();

        Thread thread = new Thread(){
            public void run()
            {
                try {
                    myViewHolder.ivEvento.setImageDrawable(Drawable.createFromStream((InputStream) new URL(url).getContent(),"src"));
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listEvento.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivEvento;
        public TextView tvEvento, tvEndereco;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivEvento = (ImageView) itemView.findViewById(R.id.ivEvento);
            tvEvento = (TextView) itemView.findViewById(R.id.tvEvento);
            tvEndereco = (TextView) itemView.findViewById(R.id.tvEndereco);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v,getPosition());
            }
        }
    }

    public int getCodigoEvento(int position){
        Evento evento = listEvento.get(position);
        return  evento.getCodigoEvento();
    }



}
