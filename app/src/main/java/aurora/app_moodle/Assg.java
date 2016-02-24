package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by beniw on 24/02/2016.
 */
public class Assg extends ListFragment{
    Bundle data;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        View rootview = inflater.inflate(R.layout.assg, container, false);
        TextView tv = (TextView) rootview.findViewById(R.id.tv);
        tv.setText(data.getString("name"));
        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(data.getString("Empty") == "NO ASSIGNMENTS TO SHOW") {
            String[] values = new String[1];
            values[0] = data.getString("Empty");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        else {
            String[] values = {"Deadline - "+data.getString("deadline")+"\n"+"\n"+data.getString("description")};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
    }
}
