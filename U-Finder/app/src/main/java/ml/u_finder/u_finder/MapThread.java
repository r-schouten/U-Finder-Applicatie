package ml.u_finder.u_finder;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Raber on 11/18/2017.
 */

public class MapThread extends Thread {
    private String room;
    private MapActivity activity;
    private Bitmap image;
    private Coordinates coordinates;
    private final UWBServer server = new UWBServer("77.172.10.240",8379);
    public MapThread(String room, MapActivity mapActivity){
        this.room = room;
        this.activity = mapActivity;
    }

    public void run(){
        Log.v("Raber", ""+room);
        image = server.getImage(room);

        if (image != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    activity.setImage(image);
                }
            });
        }
        Log.v("Raber", "Picture geplaats");

        while (true){
            coordinates = server.getCoordinates(room);



            for (int i = 0 ; i < coordinates.getSize(); i++ ){
                final int i2=i;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        activity.track(coordinates.getX(i2), coordinates.getY(i2), i2);
                        Log.v("Raber", coordinates.getX(i2)+" "+coordinates.getY(i2));
                    }
                });
            }



            try {
                sleep(2000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    activity.repeat();
                }
            });


        }

    }
}
