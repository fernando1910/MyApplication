package project.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Fernando on 07/06/2015.
 */
public class clsUtil {

    public String RetornaDataHoraMinuto()
    {
        String DataHora;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        DataHora=  dateFormat.format(calendar.getTime());
        return DataHora;
    }


    public Drawable retornarIcone(Drawable drawable, Resources resources)
    {

        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        Drawable d = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap,60,60,true));
        return d;

    }

    public clsUtil() {
    }

}
