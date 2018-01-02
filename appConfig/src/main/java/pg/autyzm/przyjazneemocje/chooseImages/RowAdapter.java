package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


import pg.autyzm.przyjazneemocje.R;

/**
 * Created by Joanna on 2016-10-11.
 */

public class RowAdapter extends ArrayAdapter<RowBean> {

    int layoutResourceId;
    RowBean data[] = null;

    private List<RowBean> rowBeanList;
    private Context context;

    public RowAdapter(Context context, int layoutResourceId, RowBean[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RowBeanHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.checkBox = (CheckBox) row.findViewById(R.id.checkBoxImagesToChoose);

            holder.checkBox.setOnCheckedChangeListener((ChooseImages) context);

            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }


        RowBean object = data[position];
        holder.checkBox.setChecked(object.selected);
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            File fileOut = new File(root + "Emotions" + File.separator + object.photoName);
            Bitmap captureBmp = MediaStore.Images.Media.getBitmap(object.cr, Uri.fromFile(fileOut));
            holder.imgIcon.setImageBitmap(captureBmp);
        } catch (Exception e) {
            System.out.println(e);
        }

        return row;
    }

    static class RowBeanHolder {
        public ImageView imgIcon;
        public CheckBox checkBox;
    }
}