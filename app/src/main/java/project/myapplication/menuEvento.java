package project.myapplication;

import java.io.Serializable;

public class menuEvento implements Serializable{
    private String ds_titulo;
    private int cd_evento;

    public String getTitulo() {
        return ds_titulo;
    }

    public void setTitulo(String ds_titulo) {
        this.ds_titulo = ds_titulo;
    }

    @Override
    public String toString()
    {
        return String.valueOf(cd_evento);
    }

    public int getValue()
    {
        return cd_evento;
    }

    public int getCodigoEvento() {
        return cd_evento;
    }

    public void setCodigoEvento(int cd_evento) {
        this.cd_evento = cd_evento;
    }
}
