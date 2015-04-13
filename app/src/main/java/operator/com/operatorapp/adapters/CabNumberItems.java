package operator.com.operatorapp.adapters;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by parviz on 4/13/15.
 */
public interface CabNumberItems {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
