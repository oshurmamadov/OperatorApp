package operator.com.operatorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import operator.com.operatorapp.R;

/**
 * Created by parviz on 4/13/15.
 */
public class Record  {

    public String cabNumber;
    public double latitude;
    public double longitude;
    public String date;


    public Record(String cabNumber,double latitude,double longitude, String date){

        this.cabNumber = cabNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;

    }

    public String getCabNumber() { return  this.cabNumber; }

    public Double getLatitude() { return  this.latitude; }

    public Double getLongitude() { return  this.longitude; }

    public String getDate() { return  this.date; }

   /* @Override
    public int getViewType() {
        return RecordAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
          //  view = (View) inflater.inflate(R.layout.playlist_item, null);
        } else {
            view = convertView;
        }
        return null;
    }*/
}
