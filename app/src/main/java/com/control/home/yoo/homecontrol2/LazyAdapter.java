package com.control.home.yoo.homecontrol2;

/**
 * Created by yoo on 4/28/16.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    //public ImageLoader imageLoader;


    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>>  d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);


        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> target = new HashMap<String, String>();
        target = data.get(position);

        String id = target.get("id");
        String state = target.get("duration");

        if( state == null)
            return null;

        // Setting all values in listview
        title.setText(target.get(MainActivity.KEY_TITLE));
        artist.setText(target.get(MainActivity.KEY_ARTIST));
        duration.setText(target.get(MainActivity.KEY_DURATION));
        //thumb_image.setBackground();


        int imageResource = R.drawable.image_1on;

        switch(id)
        {
            case "1":
                imageResource = R.drawable.image_1on;
                if( state.equals("Off")  )
                    imageResource = R.drawable.image_1off;

                break;

            case "2":
                imageResource = R.drawable.image_2on;
                if( state.equals("Off")  )
                    imageResource = R.drawable.image_2off;

                break;

            case "3":
                imageResource = R.drawable.image_3on;
                if(  state.equals("Off")  )
                    imageResource = R.drawable.image_3off;

                break;

            case "4":
                imageResource = R.drawable.image_4on;
                if( state.equals("Off")  )
                    imageResource = R.drawable.image_4off;

                break;

            case "5":
                imageResource = R.drawable.image_5on;
                if( state.equals("Off")  )
                    imageResource = R.drawable.image_5off;

                break;

            case "6":
                imageResource = R.drawable.image_6on;
                if( state.equals("Off")  )
                    imageResource = R.drawable.image_6off;

                break;
            default: break;
        }


        thumb_image.setImageResource(imageResource);

        //imageLoader.DisplayImage(target.get(CustomizedListView.KEY_THUMB_URL), thumb_image);

        return vi;
    }

}
