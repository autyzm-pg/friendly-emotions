package pg.autyzm.przyjazneemocje;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pg.autyzm.przyjazneemocje.View.LevelConfiguration;
import pg.autyzm.przyjazneemocje.lib.Level;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;

public class CustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<Boolean> active_list = new ArrayList<Boolean>();
    private Context context;
    public SqlliteManager sqlm;


    public CustomList(ArrayList<String> list, ArrayList<Boolean> active_list, Context context) {
        this.list = list;
        this.active_list = active_list;
        this.context = context;
        sqlm = getInstance(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_single, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));



        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        CheckBox activeChck = (CheckBox) view.findViewById(R.id.active_chck);


        activeChck.setChecked(active_list.get(position));


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                sqlm.delete("levels", "id", String.valueOf(findLevelId(position)));
                sqlm.delete("levels_photos", "levelid", String.valueOf(findLevelId(position)));
                sqlm.delete("levels_emotions", "levelid", String.valueOf(findLevelId(position)));
                list.remove(position);
                active_list.remove(position);

                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something


                Intent intent = new Intent(context, LevelConfiguration.class);
                //intent.putExtra(EXTRA_MESSAGE, findLevelId(position));

                Bundle b = new Bundle();
                b.putInt("key", findLevelId(position)); //Your id

                System.out.println("przeslij " + findLevelId(position));

                intent.putExtras(b);

                context.startActivity(intent);



                notifyDataSetChanged();
            }
        });





        activeChck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                /*

                    1. znajdz id poziomu
                    2. pobierz poziom z bazy
                    3. edytuj go
                    4. zapisz go do bazy

                    */



                Cursor cur2 = sqlm.giveLevel(findLevelId(position));
                Cursor cur3 = sqlm.givePhotosInLevel(findLevelId(position));
                Cursor cur4 = sqlm.giveEmotionsInLevel(findLevelId(position));

                Level l = new Level(cur2, cur3, cur4);

                l.setLevelActive(! l.isLevelActive());
               // l.isLevelActive = ! l.isLevelActive;
                active_list.set(position, l.isLevelActive());

                //

                sqlm.addLevel(l);

                notifyDataSetChanged();
            }
        });




        return view;
    }


    int findLevelId(int position){

        String levelString = list.get(position);


        String[] splittedLevelString = levelString.split(" ");
        int levelId = Integer.parseInt(splittedLevelString[0]);


        return levelId;

    }

}