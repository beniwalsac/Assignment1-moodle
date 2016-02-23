package aurora.app_moodle;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

/**
 * Created by beniw on 23/02/2016.
 */
public class JSonArrayParser implements Parcelable {

    private JSONArray jsonArray;
    public JSonArrayParser() {}

    public JSonArrayParser(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
