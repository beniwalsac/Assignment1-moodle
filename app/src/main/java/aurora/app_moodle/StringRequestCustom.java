package aurora.app_moodle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beniw on 23/02/2016.
 */
public class StringRequestCustom extends com.android.volley.toolbox.StringRequest {

    public StringRequestCustom(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        NetworkController.getInstance().checkSessionCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }
        NetworkController.getInstance().addSessionCookie(headers);
        return headers;
    }
}
