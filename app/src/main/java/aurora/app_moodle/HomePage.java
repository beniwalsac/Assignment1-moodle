package aurora.app_moodle;

import android.support.v4.app.ListFragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Map;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name;
    private TextView email;
    public static JSONObject user_data;

    ArrayList<String> courseList,gradeList;
    ListFragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.name);
        email = (TextView) header.findViewById(R.id.email);

        try {
            user_data = new JSONObject(getIntent().getStringExtra("product"));
            name.setText(user_data.getString("first_name"));
            name.append(" ");
            name.append(user_data.getString("last_name"));
            email.setText(user_data.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Overveiw) {
            fragment = new Overview();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.section_label, fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.Grades) {
            fragment = new Grades();
            String url = "http://tapi.cse.iitd.ernet.in:1805/default/grades.json";
            /*RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("starts the show response function");
                            showResponseGrades(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //change the value of toast to show
                    System.out.println("doesn't get to the link");
                    Toast.makeText(HomePage.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(stringRequest);*/
            NetworkController.getInstance().functionForVolleyRequest(url, this, new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("starts the show response function");
                    showResponseGrades(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(HomePage.this, "There is something wrong with the connection.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (id == R.id.Notifications) {
            fragment = new Notifications();
            String url = "http://tapi.cse.iitd.ernet.in:1805/default/notifications.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this, new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showResponseNotify(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(HomePage.this, "There is something wrong with the connection.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (id == R.id.My_Courses) {
            fragment = new My_Courses();
            String url = "http://tapi.cse.iitd.ernet.in:1805/courses/list.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this, new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    showMyCourses(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(HomePage.this, "There is something wrong with the connection.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (id == R.id.logout) {
            String url = "http://tapi.cse.iitd.ernet.in:1805/default/logout.json";
            NetworkController.getInstance().functionForVolleyRequest(url, this, new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    logout(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(HomePage.this, "There is something wrong with the connection.", Toast.LENGTH_LONG).show();
                }
            });
        }
        return true;
    }

    private void showResponseGrades(String response) {
        Bundle data = new Bundle();
        JSONArray courses,grades;
        String[] grade_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            courses = jsonObject.getJSONArray("courses");
            grades = jsonObject.getJSONArray("grades");
            if(courses == null || grades == null || courses.length() == 0 || grades.length() == 0) {
                data.putString("Empty", "NO GRADES TO SHOW");
            }
            else {
                grade_array = new String[courses.length()];
                for (int i=1;i<=grade_array.length;i++) {
                    JSONObject c = courses.getJSONObject(i-1);
                    JSONObject g = grades.getJSONObject(i-1);
                    grade_array[i-1] = i+"."+"    "+"Course:-  "+c.getString("code")+"\n"+"       Name:-  "+g.getString("name")+"\n"+"       Score:-  "+g.getString("score")+"/"+g.getString("out_of")+"\n"+"       Weightage:-  "+g.getString("weightage");
                }
                data.putStringArray("GradeArray", grade_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.section_label, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showResponseNotify(String response) {
        Bundle data = new Bundle();
        JSONArray notifications;
        String[] notif_array;
        try {
            JSONObject jsonObject = new JSONObject(response);
            notifications = jsonObject.getJSONArray("notifications");
            if(notifications == null || notifications.length() == 0) {
                data.putString("Empty", "NO NOTIFICATIONS TO SHOW");
            }
            else {
                notif_array = new String[notifications.length()];
                for (int i=1;i<=notif_array.length;i++) {
                    JSONObject c = notifications.getJSONObject(i - 1);
                    notif_array[i - 1] = i + "." + "    " + c.getString("description") + "\n" + "          " + c.getString("created_at");
                }
                data.putStringArray("NotifArray",notif_array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.section_label, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showMyCourses(String response) {
        Bundle data = new Bundle();
        JSONArray courses;
        String[] course_name,course_code;
        try {
            JSONObject jsonObject = new JSONObject(response);
            courses = jsonObject.getJSONArray("courses");
            if(courses == null || courses.length() == 0) {
                data.putString("Empty", "NO COURSES TO SHOW");
            }
            else {
                course_code = new String[courses.length()];
                course_name = new String[courses.length()];
                for (int i=0;i<courses.length();i++) {
                    JSONObject c = courses.getJSONObject(i);
                    course_code[i] = c.getString("code");
                    course_name[i] = c.getString("name");
                }
                data.putStringArray("CourseCode",course_code);
                data.putStringArray("CourseName",course_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.section_label, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout(String response) {
        Intent intent = new Intent(this,Log_In.class);
        startActivity(intent);
        this.finish();
    }

}
