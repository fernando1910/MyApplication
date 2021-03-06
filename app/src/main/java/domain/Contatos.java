package domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.ContatoDAO;
import project.myapplication.R;

public class Contatos {

    //region Variaveis
    private int cd_contato;
    private String ds_contato;
    private byte[] img_contato;
    private boolean fg_selecionado;
    //endregion

    //region Propriedades

    public int getCodigoContato() {
        return cd_contato;
    }

    public void setCodigoContato(int cd_contato) {
        this.cd_contato = cd_contato;
    }

    public String getNomeContato() {
        return ds_contato;
    }

    public void setNomeContato(String ds_contato) {
        this.ds_contato = ds_contato;
    }

    public byte[] getImagemContato() {
        return img_contato;
    }

    public void setImagemContato(byte[] img_contato) {
        this.img_contato = img_contato;
    }

    public boolean getSelecionado() {
        return fg_selecionado;
    }

    public void setSelecionado(boolean fg_selecionado) {
        this.fg_selecionado = fg_selecionado;
    }

    //endregion

    //region Métodos

    public void atualizarContatos(ContentResolver contentResolver, String url, Context context) {
        String phoneNumber;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        StringBuffer output = new StringBuffer();

        List<String> arrayNumeros = new ArrayList<>();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);
                    final String finalName = name;

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);

                        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
                            phoneNumber = phoneNumber
                                    .replace("-", "")
                                    .replace(" ", "")
                                    .replace("+", "")
                                    .replace("/", "")
                                    .replace("(", "")
                                    .replace(")", "");

                            if (phoneNumber.length() > 7) {
                                String finalPhoneNumber = phoneNumber.substring((phoneNumber.length() - 8), phoneNumber.length());
                                arrayNumeros.add(finalPhoneNumber);
                            }
                        }
                    }

                    phoneCursor.close();
                }
                output.append("\n");
            }

            Set<String> stringSet = new HashSet<>();
            stringSet.addAll(arrayNumeros);
            arrayNumeros.clear();
            arrayNumeros.addAll(stringSet);

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < arrayNumeros.size(); i++) {
                    JSONObject aux = new JSONObject();
                    aux.put("nr_telefone", arrayNumeros.get(i));
                    jsonArray.put(aux);
                }

                jsonObject.put("numeros", jsonArray);

                Util util = new Util();
                String jsonString;
                jsonString = util.enviarServidor(url, jsonObject.toString(), "atualizarContatos");
                JSONArray jsonArrayResultado = new JSONArray(jsonString);
                JSONObject jsonObjectResultado;
                ContatoDAO contatoDAO = new ContatoDAO(context);
                contatoDAO.DeletarTudo();
                for (int i = 0; i < jsonArrayResultado.length(); i++) {
                    jsonObjectResultado = new JSONObject(jsonArrayResultado.getString(i));
                    Contatos objContatos = new Contatos();
                    objContatos.setCodigoContato(Integer.parseInt(jsonObjectResultado.getString("cd_contato")));
                    objContatos.setNomeContato(jsonObjectResultado.getString("ds_contato"));
                    contatoDAO.Salvar(objContatos);

                }

            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public List<Contatos> retonarContatos(Context context) {
        List<Contatos> list = new ArrayList<>();
        Usuario objUsuario = new Usuario();
        objUsuario.carregar(context);
        ContatoDAO contatoDAO = new ContatoDAO(context);
        list = contatoDAO.Carregar(objUsuario.getCodigoUsuario());
        return list;
    }

    public List<Contatos> buscarConvidados(Context context, int cd_evento, Util util) throws Exception {
        List<Contatos> mListaContatos = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cd_evento", cd_evento);
        String mResposta = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonObject.toString(), "buscarConvidados");
        JSONArray jsonArray = new JSONArray(mResposta);
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = new JSONObject(jsonArray.getString(i));
            Contatos mContato = new Contatos();
            mContato.setCodigoContato(jsonObject.getInt("cd_contato"));
            mContato.setNomeContato(jsonObject.getString("ds_contato"));
            mListaContatos.add(mContato);
        }
        return mListaContatos;
    }

    public List<Contatos>buscarPossiveisConvidados(Context context, int cd_evento,Util util) throws Exception{
        List<Contatos> mListaContatos = new ArrayList<>();

        return mListaContatos;
    }

    //endregion
}
