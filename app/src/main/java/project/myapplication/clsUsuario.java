package project.myapplication;

/**
 * Created by Fernando on 24/04/2015.
 */
public class clsUsuario {
    private int cd_usuario;
    private String ds_nome;
    private String ds_telefone;

    public String getNome() {
        return ds_nome;
    }

    public void setNome(String ds_nome) {
        this.ds_nome = ds_nome;
    }

    public String getTelefone() {
        return ds_telefone;
    }

    public void setTelefone(String ds_telefone) {
        this.ds_telefone = ds_telefone;
    }

    public int getCodigoUsuario() {
        return cd_usuario;
    }

    public void setCodigoUsuario(int cd_usuario) {
        this.cd_usuario = cd_usuario;
    }

    //teste 2

}
