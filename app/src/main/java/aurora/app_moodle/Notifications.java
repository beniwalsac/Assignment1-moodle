package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by beniw on 24/02/2016.
 */
public class Notifications extends ListFragment{

    Bundle data;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        View rootview = inflater.inflate(R.layout.grades, container, false);
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(data.getString("Empty") == "NO NOTIFICATIONS TO SHOW") {
            String[] values = new String[1];
            values[0] = data.getString("Empty");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        else {
            String[] values = data.getStringArray("NotifArray");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        //String item = (String) getListAdapter().getItem(position);
        //Toast.makeText(this, (item + " selected"), Toast.LENGTH_LONG).show();
    }
}
