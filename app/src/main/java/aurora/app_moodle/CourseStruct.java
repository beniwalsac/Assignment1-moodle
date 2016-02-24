package aurora.app_moodle;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by beniw on 24/02/2016.
 */
public class CourseStruct extends ListFragment {

    View rootview;
    String item;
    Bundle data;
    Bundle data0;
    Bundle data1;
    Bundle data2;

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
            String url = "http://192.168.1.171:8000/courses/course.json/"+item+"/assignments";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    data0 = new Bundle();
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
            String url = "http://192.168.1.171:8000/courses/course.json/"+item+"/grades";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    data1 = new Bundle();
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
            String url = "http://192.168.1.171:8000/courses/course.json/"+item+"/threads";
            NetworkController.getInstance().functionForVolleyRequest(url, this.getContext(), new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    data2 = new Bundle();
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
        JSONArray assignments;
        String[] assg_array , assg_id;
        try {
            JSONObject jsonObject = new JSONObject(response);
            assignments = jsonObject.getJSONArray("assignments");
            if(assignments == null || assignments.length() == 0) {
                data0.putString("Empty", "NO ASSIGNMENTS TO SHOW");
                data0.putString("course", item);
            }
            else {
                assg_array = new String[assignments.length()];
                assg_id = new String[assignments.length()];
                for (int i=0;i<assg_array.length;i++) {
                    assg_array[i] = assignments.getJSONObject(i).getString("name");
                    assg_id[i] = assignments.getJSONObject(i).getString("id");
                }
                data0.putStringArray("AssgArray", assg_array);
                data0.putStringArray("AssgId", assg_id);
                data0.putString("course", item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assignments nextFrag= new Assignments();
        nextFrag.setArguments(data0);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void showResponseGrades(String response) {
        JSONArray grades;
        String[] grade_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            grades = jsonObject.getJSONArray("grades");
            if(grades == null || grades.length() == 0) {
                data1.putString("Empty", "NO GRADES TO SHOW");
            }
            else {
                grade_array = new String[grades.length()];
                for (int i=1;i<=grade_array.length;i++) {
                    JSONObject g = grades.getJSONObject(i-1);
                    grade_array[i-1] = i+"."+"    "+g.getString("name")+"\n"+"       Score:-  "+g.getString("score")+"/"+g.getString("out_of")+"\n"+"       Weightage:-  "+g.getString("weightage");
                }
                data1.putStringArray("GradeArray", grade_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Grades nextFrag= new Grades();
        nextFrag.setArguments(data1);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void showResponseThreads(String response) {
        JSONArray threads;
        String[] thread_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            threads = jsonObject.getJSONArray("course_threads");
            if(threads == null || threads.length() == 0) {
                data2.putString("Empty", "NO THREADS TO SHOW");
                data2.putString("course", item);
            }
            else {
                thread_array = new String[threads.length()];
                for (int i=1;i<=thread_array.length;i++) {
                    JSONObject g = threads.getJSONObject(i-1);
                    thread_array[i-1] = i+"."+"    "+g.getString("title")+"\n"+"          "+g.getString("updated_at");
                }
                data2.putStringArray("ThreadArray", thread_array);
                data2.putString("course", item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Threads nextFrag= new Threads();
        nextFrag.setArguments(data2);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.section_label, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }
}
