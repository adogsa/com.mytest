package jjw.com.fragment.model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.toast.android.analytics.common.utils.JsonUtils;
import com.toast.android.analytics.common.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jjw.com.fragment.ItemFragment;
import jjw.com.fragment.MyImgItemRecyclerViewAdapter;
import jjw.com.fragment.MyItemRecyclerViewAdapter;
import jjw.com.mytest.R;
import jjw.com.utill.LoadJSONUtil;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ImageContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<OneImageItem> ITEMS = new ArrayList<OneImageItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, OneImageItem> ITEM_MAP = new HashMap<String, OneImageItem>();

    private static ItemFragment.OnListFragmentInteractionListener  mListClickListener;
    private String JSON_URL = "http://demo2587971.mockable.io/images";
    private static View mView;

    public void makeList(String jsonUrl, View view, ItemFragment.OnListFragmentInteractionListener listener){
        mView = view;
        mListClickListener = listener;
        new LoadJSONUtil(new onLoadJsonDataListener()).execute(JSON_URL);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class OneImageItem {
        public final String title;
        public final String img_url;
        public final String img_size_width;
        public final String img_size_height;
        public final String date_taken;

        public OneImageItem(String title, String img_url, String img_size_height, String img_size_width, String date_taken) {
            this.title = title;
            this.img_url = img_url;
            this.img_size_width = img_size_width;
            this.img_size_height = img_size_height;
            this.date_taken = date_taken;
        }

        public String getImg_size(){
            if(StringUtil.isEmpty(img_size_height) == true){
                return "";
            } else {
                return img_size_width + " X " + img_size_height;
            }
        }

        @Override
        public String toString() {
            return title + "_" + img_url;
        }
    }

    private class onLoadJsonDataListener implements LoadJSONUtil.Listener {

        @Override
        public void onLoaded(String jsonData) {

            try {
                JSONObject json = new JSONObject(jsonData);

                JSONArray jArray = json.getJSONArray("photos");

                for(int index = 0; index < jArray.length(); index++){
                    JSONObject json_data = jArray.getJSONObject(index);

                    OneImageItem data = new OneImageItem(
                            LoadJSONUtil.getJsonValue(json_data, "title")
                            , LoadJSONUtil.getJsonValue(json_data, "url")
                            , LoadJSONUtil.getJsonValue(json_data, "width")
                            , LoadJSONUtil.getJsonValue(json_data, "height")
                            , LoadJSONUtil.getJsonValue(json_data, "date_taken")
                    );

                    ITEMS.add(data);
                }

                // Set the adapter
                if (mView instanceof RecyclerView) {
                    Context context = mView.getContext();
                    RecyclerView recyclerView = (RecyclerView) mView;
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new MyImgItemRecyclerViewAdapter(ITEMS, mListClickListener));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError() {

        }
    }
}
