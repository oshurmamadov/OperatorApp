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


    public Record(String cabNumber,double latitude,double longitude){

        this.cabNumber = cabNumber;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getCabNumber() { return  this.cabNumber; }

    public Double getLatitude() { return  this.latitude; }

    public Double getLongitude() { return  this.longitude; }

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
