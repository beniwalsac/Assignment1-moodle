package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

/**
 * Created by beniw on 24/02/2016.
 */
public class My_Courses extends ListFragment {

    Bundle data;
    int size;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        View rootview = inflater.inflate(R.layout.my_courses, container, false);
        TextView tv = (TextView) rootview.findViewById(R.id.tv);
        tv.setText("Course List");
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(data.getString("Empty") == "NO COURSES TO SHOW") {
            size = 0;
            String[] values = new String[1];
            values[0] = data.getString("Empty");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        else {
            String[] course_code = data.getStringArray("CourseCode");
            String[] course_name = data.getStringArray("CourseName");
            String[] values = new String[course_code.length];
            size = course_code.length;
            for(int i=0; i<values.length;i++) {
                values[i] = course_code[i].toUpperCase()+": "+course_name[i];
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        String item = (String) getListAdapter().getItem(position);
        //Toast.makeText(this, (item + " selected"), Toast.LENGTH_LONG).show();
        Bundle data = new Bundle();
        data.putString("course",item.substring(0,6).toLowerCase());
        super.onListItemClick(l, v, position, id);
        CourseStruct nextFrag= new CourseStruct();
        nextFrag.setArguments(data);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

}
