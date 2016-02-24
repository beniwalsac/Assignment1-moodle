package aurora.app_moodle;

import android.os.Bundle;
import android.provider.Telephony;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by beniw on 24/02/2016.
 */
public class CourseStruct extends ListFragment {

    View rootview;
    Bundle data;
    String item;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        item = data.getString("course");
        rootview = inflater.inflate(R.layout.course_struct, container, false);
        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            String[] values = {"Assignments","Grades","Threads"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {

        if(position == 0) {
            String url = "http://tapi.cse.iitd.ernet.in:1805/courses/course.json/"+item+"/assignments.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showResponseAssignments(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            });
        }
        else if(position == 1) {
            String url = "http://tapi.cse.iitd.ernet.in:1805/courses/course.json/"+item+"/grades.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showResponseGrades(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            });
        }
        else if (position==2) {
            String url = "http://tapi.cse.iitd.ernet.in:1805/courses/course.json/"+item+"/threads.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showResponseThreads(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            });
        }
    }

    private void showResponseAssignments(String response) {
        Bundle data = new Bundle();
        JSONArray assignments;
        String[] assg_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            assignments = jsonObject.getJSONArray("assignments");
            if(assignments == null || assignments.length() == 0) {
                data.putString("Empty", "NO ASSIGNMENTS TO SHOW");
            }
            else {
                assg_array = new String[assignments.length()];
                for (int i=0;i<assg_array.length;i++) {
                    assg_array[i] = assignments.getJSONObject(i).getString("name");
                }
                data.putStringArray("AssgArray", assg_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assignments nextFrag= new Assignments();
        nextFrag.setArguments(data);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void showResponseGrades(String response) {
        Bundle data = new Bundle();
        JSONArray grades;
        String[] grade_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            grades = jsonObject.getJSONArray("grades");
            if(grades == null || grades.length() == 0) {
                data.putString("Empty", "NO GRADES TO SHOW");
            }
            else {
                grade_array = new String[grades.length()];
                for (int i=1;i<=grade_array.length;i++) {
                    JSONObject g = grades.getJSONObject(i-1);
                    grade_array[i-1] = i+"."+"    "+g.getString("name")+"\n"+"       Score:-  "+g.getString("score")+"/"+g.getString("out_of")+"\n"+"       Weightage:-  "+g.getString("weightage");
                }
                data.putStringArray("GradeArray", grade_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Grades nextFrag= new Grades();
        nextFrag.setArguments(data);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void showResponseThreads(String response) {
        Bundle data = new Bundle();
        JSONArray threads;
        String[] thread_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            threads = jsonObject.getJSONArray("course_threads");
            if(threads == null || threads.length() == 0) {
                data.putString("Empty", "NO THREADS TO SHOW");
            }
            else {
                thread_array = new String[threads.length()];
                for (int i=1;i<=thread_array.length;i++) {
                    JSONObject g = threads.getJSONObject(i-1);
                    thread_array[i-1] = i+"."+"    "+g.getString("title")+"\n"+"          "+g.getString("updated_at");
                }
                data.putStringArray("ThreadArray", thread_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Threads nextFrag= new Threads();
        nextFrag.setArguments(data);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }
}
