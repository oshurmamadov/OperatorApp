package operator.com.operatorapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import operator.com.operatorapp.R;

/**
 * Created by acer pc on 28.04.2015.
 */
public class ListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> board;
    private final ArrayList<String> name;

    public ListAdapter(Context context, ArrayList<String> name,ArrayList<String> board) {
        super(context, 0, board);
        this.context = context;
        this.board = board;
        this.name = name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_row, parent, false);

        TextView rowName = (TextView) rowView.findViewById(R.id.driver_name_row);
        TextView rowBoard = (TextView) rowView.findViewById(R.id.board_row);

        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/roboto_thin.ttf");

        rowName.setTypeface(font);
        rowBoard.setTypeface(font);

        rowName.setText(name.get(position));
        rowBoard.setText(board.get(position));

        return rowView;
    }

}
