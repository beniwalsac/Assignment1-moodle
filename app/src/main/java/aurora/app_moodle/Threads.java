package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by beniw on 24/02/2016.
 */
public class Threads extends ListFragment {
    Bundle data;
    int size;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        View rootview = inflater.inflate(R.layout.my_courses, container, false);
        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(data.getString("Empty") == "NO THREADS TO SHOW") {
            size = 0;
            String[] values = new String[1];
            values[0] = data.getString("Empty");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        else {
            String[] values = data.getStringArray("ThreadArray");
            size = values.length;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
    }
}
