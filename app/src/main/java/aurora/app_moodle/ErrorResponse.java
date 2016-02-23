package aurora.app_moodle;

import com.android.volley.VolleyError;

/**
 * Created by beniw on 23/02/2016.
 */
public interface ErrorResponse {
    void onErrorResponse(VolleyError volleyError);
}
