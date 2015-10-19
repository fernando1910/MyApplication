package project.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import adapters.CustomListViewRanking;
import domain.Evento;

public class VisualizarTopFestas extends Fragment {
    private final String TAG = "LOG";
    private ListView mListView;
    private List<Evento> mItems;
    private CustomListViewRanking mAdapter;
    private ProgressBar mProgressBar;
    private Evento objEvento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_padrao_top_festas, container, false);
        mListView = (ListView) view.findViewById(R.id.lvTopFestas);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pbFooterLoading);
        objEvento = new Evento();

        new listarEventos().execute();
        return view;
    }

    public class listarEventos extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                mProgressBar.setVisibility(View.INVISIBLE);
                //mProgressDialog.dismiss();
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), VisualizarEvento.class);
                        intent.putExtra("codigoEvento", mAdapter.getCodigoEvento(position));
                        startActivity(intent);

                    }
                });
            }catch (Exception ex){
                Log.i(TAG, ex.getMessage());
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try{
                    mItems  = objEvento.selecionarTopFestas(getContext());
                    mAdapter = new CustomListViewRanking(getContext(),mItems);
                }catch (Exception ex){
                    Log.i(TAG, ex.getMessage());
                }
            }
            return null;
        }
    }
}
