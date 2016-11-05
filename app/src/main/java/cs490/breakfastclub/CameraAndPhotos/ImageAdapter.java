package cs490.breakfastclub.CameraAndPhotos;

/**
 * Created by Emma on 10/30/2016.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import cs490.breakfastclub.UserFiles.User;

public class ImageAdapter extends BaseAdapter {
    static private Context mContext;
    static private ArrayList<URL> photos;
    private ArrayList<String> photoids;
    static int mSize;

    public ImageAdapter(Context c, User user, ArrayList<URL> photos, ArrayList<String> photoids) {
        mContext = c;
        this.photos = photos;
        this.photoids = photoids;
        mSize = photos.size();
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {return (long)position;}

    public static URL getItemURL(int position) {
        return photos.get(position);
    }

    public static URL getPrevItemURL(int position) {
        if(--position<0) position = mSize - 1;
        return photos.get(position);
    }

    public static URL getNextItemURL(int position) {
        if(++position>mSize-1) position = 0;
        return photos.get(position);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        if(mSize == 0)
        {
            return imageView;
        }

        Picasso.with(mContext).load(getItemURL(position).toString()).into(imageView);
        //imageView.setImageBitmap(photos[position].getbMap());
      // imageView.setImageResource(mThumbIds[position]);
       // imageView.setLayoutParams(new GridLayoutManager.LayoutParams(imageView.getWidth(), imageView.getWidth()));

        return imageView;
    }

    public static Context getmContext() {
        return mContext;
    }

}