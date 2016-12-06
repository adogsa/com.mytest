package jjw.com.fragment.model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    private String JSON_URL = "";
    private static View mView;

    public void makeList(String jsonUrl, View view, ItemFragment.OnListFragmentInteractionListener listener){
        mView = view;
        mListClickListener = listener;
        new LoadJSONUtil(new onLoadJsonDataListener()).execute(jsonUrl);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class OneImageItem {
        public final String title;
        public final String img_url;
        public final String content;

        public OneImageItem(String title, String img_url, String content) {
            this.title = title;
            this.img_url = img_url;
            this.content = content;
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
                JSONArray jArray = new JSONArray(jsonData);

                for(int index = 0; index < jArray.length(); index++){
                    JSONObject json_data = jArray.getJSONObject(index);
                    OneImageItem data = new OneImageItem(
                            json_data.getString("title")
                            , json_data.getString("url")
                            , json_data.getString("url")
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
