package pg.autyzm.przyjazneemocje.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import pg.autyzm.przyjazneemocje.LevelConfigurationActivity;
import pg.autyzm.przyjazneemocje.R;
import pg.autyzm.przyjazneemocje.lib.entities.Level;


/**
 * Created by joagi on 05.12.2017.
 */

public class CheckboxImageAdapter extends ArrayAdapter<GridCheckboxImageBean> {

    int layoutResourceId;
    GridCheckboxImageBean data[] = null;

    private List<GridCheckboxImageBean> rowBeanList;
    private Context context;

    public CheckboxImageAdapter(Context context, int layoutResourceId, GridCheckboxImageBean[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RowBeanHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.checkBox = (CheckBox) row.findViewById(R.id.checkBoxImagesToChoose);

            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }


        final GridCheckboxImageBean object = data[position];


        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

            File fileOut;
            Bitmap captureBmp;
            if(object.photoName.contains(".mp4"))
            {
                fileOut = new File(root + "FriendlyEmotions/Videos" + File.separator + object.photoName);
                captureBmp = ThumbnailUtils.createVideoThumbnail(fileOut.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            }
            else
            {
                fileOut = new File(root + "FriendlyEmotions/Photos" + File.separator + object.photoName);
                captureBmp = MediaStore.Images.Media.getBitmap(object.cr, Uri.fromFile(fileOut));
            }

            holder.imgIcon.setImageBitmap(captureBmp);
        } catch (Exception e) {
            System.out.println(e);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = ((CheckBox)arg0).isChecked();
                if(isChecked) {
                    Integer photoId = object.getId();
                    Level configuredLevel = ((LevelConfigurationActivity) context).getLevel();

                    if(object.photoName.contains(".mp4"))
                    {
                        configuredLevel.setPhotosOrVideosFlag("videos");
                    }
                    else
                        configuredLevel.addPhoto(photoId);

                }
            }
        });

        return row;
    }

    boolean isPhotoInLevelYet(Integer photoId){

        Level configuredLevel = ((LevelConfigurationActivity) context).getLevel();
        if(configuredLevel == null) return false;

        for(Integer photoInLevelId : configuredLevel.getPhotosOrVideosIdList()){
            if(photoInLevelId.equals(photoId)){
                return true;
            }
        }

        return false;
    }


    static class RowBeanHolder {
        public ImageView imgIcon;
        public CheckBox checkBox;
    }
}