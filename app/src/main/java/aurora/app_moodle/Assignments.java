package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by beniw on 24/02/2016.
 */
public class Assignments extends ListFragment {
    Bundle data;
    int size;
    String course;
    String item;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        course = data.getString("course");
        View rootview = inflater.inflate(R.layout.my_courses, container, false);
        TextView tv = (TextView) rootview.findViewById(R.id.tv);
        tv.setText(course.toUpperCase());
        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(data.getString("Empty") == "NO ASSIGNMENTS TO SHOW") {
            size = 0;
            String[] values = new String[1];
            values[0] = data.getString("Empty");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        else {
            String[] values = data.getStringArray("AssgArray");
            size = values.length;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        item = (String) getListAdapter().getItem(position);
        String[] ids = data.getStringArray("AssgId");
        if (ids != null) {
            String url = "http://192.168.1.171:8000/courses/assignment.json/"+ids[position];
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showResponseAssignment(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            });
        }
    }

    private void showResponseAssignment(String response) {

        Bundle data = new Bundle();
        JSONObject assg;
        String name,description,deadline;
        try {
            JSONObject jsonObject = new JSONObject(response);
            assg = jsonObject.getJSONObject("assignment");
            if(assg == null ) {
                data.putString("Empty", "NO DETAILS ABOUT THIS ASSIGNMENT");
                data.putString("name", "");
            }
            else {
                name = assg.getString("name");
                deadline = assg.getString("deadline");
                description = assg.getString("description");
                data.putString("name", name);
                data.putString("deadline", deadline);
                data.putString("description", description);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assg nextFrag= new Assg();
        nextFrag.setArguments(data);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }
}
