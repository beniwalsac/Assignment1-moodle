package aurora.app_moodle;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by beniw on 23/02/2016.
 * http://www.vogella.com/tutorials/AndroidListView/article.html#listfragments
 */
public class Grades extends ListFragment {

    Bundle data;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        View rootview = inflater.inflate(R.layout.grades, container, false);
        return rootview;
    }
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       if(data.getString("Empty") == "NO GRADES TO SHOW") {
           String[] values = new String[1];
           values[0] = data.getString("Empty");
           ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                   android.R.layout.simple_list_item_1, values);
           setListAdapter(adapter);
       }
       else {
           String[] values = data.getStringArray("GradeArray");
           ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
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
