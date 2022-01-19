package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ichatsocialmedaiapp.Model.designLayout_java_class;
import com.example.ichatsocialmedaiapp.R;

import java.util.List;

public class SpinnerCustomAdapter extends ArrayAdapter<designLayout_java_class> {
    private final Context context;
    private final int resourceId;
    private final List<designLayout_java_class> customdesignlist;

    public SpinnerCustomAdapter(@NonNull Context context, int resource, @NonNull List<designLayout_java_class> Object) {
        super(context, resource, Object);
        this.context = context;
        this.resourceId = resource;
        this.customdesignlist = Object;
    }

    @Override
    public int getCount() {
        return customdesignlist.size();
    }

    @Nullable
    @Override
    public designLayout_java_class getItem(int position) {
        return customdesignlist.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {//using this method we get the appearance or view of the spinner.
        View view=convertView;
        try {
            if(view==null){
                LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=layoutInflater.inflate(resourceId,parent,false);

                designLayout_java_class layout_java_class=customdesignlist.get(position);
                if(layout_java_class!=null){
                    TextView name=view.findViewById(R.id.spinner_txt);
                    ImageView imageView=view.findViewById(R.id.spinner_img);

                    name.setText(layout_java_class.getName());
                    imageView.setImageResource(layout_java_class.getImage());
                }
            }
            else {
                view = convertView;

                designLayout_java_class layout_java_class = customdesignlist.get(position);
                if (layout_java_class != null) {
                    TextView name = view.findViewById(R.id.spinner_txt);
                    ImageView imageView = view.findViewById(R.id.spinner_img);

                    name.setText(layout_java_class.getName());
                    imageView.setImageResource(layout_java_class.getImage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {//using this method we are getting the appearance of the dropdown menu of spinner.
        View view=convertView;
        try {
            if(view==null){
                LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=layoutInflater.inflate(resourceId,parent,false);

                designLayout_java_class layout_java_class=customdesignlist.get(position);
                if(layout_java_class!=null){
                    TextView name=view.findViewById(R.id.spinner_txt);
                    ImageView imageView=view.findViewById(R.id.spinner_img);

                    name.setText(layout_java_class.getName());
                    imageView.setImageResource(layout_java_class.getImage());
                }

            }else {//we have to put else condition also , because when 'if' is going to be false then it will return the view of previous usage , and some items will display repeatedly , and in unordered way or we can say in unusual way.
                view = convertView;

                designLayout_java_class layout_java_class = customdesignlist.get(position);
                if (layout_java_class != null) {
                    TextView name = view.findViewById(R.id.spinner_txt);
                    ImageView imageView = view.findViewById(R.id.spinner_img);

                    name.setText(layout_java_class.getName());
                    imageView.setImageResource(layout_java_class.getImage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

}