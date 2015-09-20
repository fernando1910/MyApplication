package project.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import adapters.CustomListViewRanking;
import domain.Evento;

public class VisualizarTopFestas extends Fragment {
    private ListView mListView;
    private List<Evento> mItems;
    private CustomListViewRanking mAdapter;
    private ProgressDialog mProgressDialog;
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
            //mProgressDialog = new ProgressDialog(getContext());
            //mProgressDialog = ProgressDialog.show(getContext(), "Carregando...",
            //        "Por favor aguarde...", false, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                mProgressBar.setVisibility(View.INVISIBLE);
                //mProgressDialog.dismiss();
                mListView.setAdapter(mAdapter);
            }catch (Exception ex){

            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try{
                    mItems  = objEvento.selecionarTopFestas(getContext());
                    mAdapter = new CustomListViewRanking(getContext(),mItems);
                }catch (Exception e){
                    //mProgressDialog.dismiss();

                }
            }
            return null;
        }
    }
}
