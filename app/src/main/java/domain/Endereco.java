package domain;

public class Endereco {
    //region variaveis

    private String ds_nome;
    private String ds_endereco;
    private double nr_latitude;
    private double nr_longitude;
    private String ds_url_icon;

    //endregion

    //region propriedades

    public String getNome() {
        return ds_nome;
    }

    public void setNome(String ds_nome) {
        this.ds_nome = ds_nome;
    }

    public String getEndereco() {
        return ds_endereco;
    }

    public void setEndereco(String ds_endereco) {
        this.ds_endereco = ds_endereco;
    }

    public double getLatitude() {
        return nr_latitude;
    }

    public void setLatitude(double nr_latitude) {
        this.nr_latitude = nr_latitude;
    }

    public double getLongitude() {
        return nr_longitude;
    }

    public void setLongitude(double nr_longitude) {
        this.nr_longitude = nr_longitude;
    }

    public String getUrlIcon() {
        return ds_url_icon;
    }

    public void setUrlIcon(String ds_url_icon) {
        this.ds_url_icon = ds_url_icon;
    }

    //endregion

}
