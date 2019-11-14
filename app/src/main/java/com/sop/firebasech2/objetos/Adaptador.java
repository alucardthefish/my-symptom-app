package com.sop.firebasech2.objetos;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sop.firebasech2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Occurence> occurencesList;

    public Adaptador(Context context, ArrayList<Occurence> occurencesList) {
        this.context = context;
        this.occurencesList = occurencesList;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return occurencesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.occurence_element_list, null);
        TextView tv_titulo = vista.findViewById(R.id.tvTitulo);
        TextView tv_descripcion = vista.findViewById(R.id.tv_description);
        TextView tv_datetime = vista.findViewById(R.id.tv_datetime);
        TextView tv_intensity = vista.findViewById(R.id.tv_intensity);
        //ImageView iv_imagen = vista.findViewById(R.id.iv_image);
        //Setting info in views
        //tv_titulo.setText( context.getResources().getString(R.string.et_title) + " " + occurencesList.get(position).getTitle());
        String title = "<b>" + context.getResources().getString(R.string.et_title) + "</b> " + occurencesList.get(position).getTitle();

        tv_titulo.setText( Html.fromHtml(title));
        String desc = occurencesList.get(position).getDescription();
        String description = "<b>" + context.getResources().getString(R.string.et_desc) + "</b> " + desc;
        if (desc.length() > 25) {
            description = "<b>" + context.getResources().getString(R.string.et_desc) + "</b> " + desc.substring(0,20) + "...";
        }
        tv_descripcion.setText(Html.fromHtml(description));
        String dtime = "<b>" + context.getResources().getString(R.string.tv_register) + "</b> " + occurencesList.get(position).getTimeOfOccurence();
        tv_datetime.setText(Html.fromHtml(dtime));
        String intensity = "<b>" + context.getResources().getString(R.string.et_intensity) + "</b> " + occurencesList.get(position).getIntensity();
        tv_intensity.setText(Html.fromHtml(intensity));

        /*iv_imagen.setTag(position);
        iv_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Imagen de ocurrencia seleccionada", Toast.LENGTH_SHORT).show();
            }
        });*/


        return vista;
    }
}
