package aurora.app_moodle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.CookieStore;
import java.util.Map;

public class Log_In extends AppCompatActivity {

    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public static final String key_success = "success";
    public static final String key_user = "user";
    public static boolean success;
    public static JSONObject user_data;
    public static TextView hyper_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        hyper_link = (TextView) findViewById(R.id.hyper_link);
        hyper_link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hyperlink(hyper_link);
            }
        });
    }

    private void hyperlink(View view) {
        if (view == hyper_link) {
            hyper_link.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void populateAutoComplete() {
        String[] user_names = getResources().getStringArray(R.array.usernames);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,user_names);
        mUsernameView.setAdapter(adapter);
    }

    private void attemptLogin() {
        //Reset errors
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String url = "http://tapi.cse.iitd.ernet.in:1805/default/login.json?userid="+username+"&password="+password;
        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            System.out.println("Completes input validation");
            showProgress(true);
            NetworkController.getInstance().functionForVolleyRequest(url, this, new DataCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("starts the show response function");
                    showResponse(result);
                }
            }, new ErrorResponse() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showProgress(false);
                    System.out.println("doesn't get to the link");
                    Toast.makeText(Log_In.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            });
            /*RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("starts the show response function");
                            showResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //change the value of toast to show
                    showProgress(false);
                    System.out.println("doesn't get to the link");
                    Toast.makeText(Log_In.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(stringRequest);*/
        }

    }

    private boolean isUsernameValid(String username) {
        if (username.matches("cs1110200") || username.matches("cs5110281") || username.matches("cs5110271") || username.matches("cs5110300") || username.matches("vinay") || username.matches("scgupta") || username.matches("subodh")) {
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private void showResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            success = jsonObject.getBoolean(key_success);
            if(!success) {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            } else {
                showProgress(false);
                user_data = jsonObject.getJSONObject(key_user);
                System.out.println("Successfull entry");
                System.out.println(user_data.length());
                Intent intent = new Intent(this,HomePage.class);
                intent.putExtra("product", user_data.toString());
                startActivity(intent);
                this.finish();
                //start the next activity
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Taken from the login templete
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
