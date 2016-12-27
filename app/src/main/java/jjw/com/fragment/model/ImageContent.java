package jjw.com.fragment.model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.toast.android.analytics.GameAnalytics;
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
import jjw.com.myfirstapp.R;
import jjw.com.utill.LoadJSONUtil;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ImageContent {

    private String TAG = ImageContent.class.getSimpleName();
    /**
     * An array of sample (dummy) items.
     */
    public static List<OneImageItem> ITEMS = new ArrayList<OneImageItem>();

    private static ItemFragment.OnListFragmentInteractionListener  mListClickListener;
    private String JSON_URL = "http://api-lnc.cloud.toast.com/launching/v2/application/zBi2sUGPkSgbB0Hh/launching";
//    private String JSON_URL = "http://demo2587971.mockable.io/images";        // coupang
    private static View mView;
    private ImageContentDB mDb;
    private String mRecentVer = "";

    public void makeList(String jsonUrl, View view, ItemFragment.OnListFragmentInteractionListener listener){
        Log.d(TAG,"jjw makeList");

        mView = view;
        mListClickListener = listener;
        if(ITEMS.size() == 0){


//            if(ShouldUpdate() == true){
                Log.d(TAG,"jjw makeList call json");

                new LoadJSONUtil(new onLoadJsonDataListener()).execute(JSON_URL);
//            } else {
//                mDb = new ImageContentDB(mView.getContext());
//                ITEMS = mDb.getImgContentList("","");
//                showItemListInView();
//            }

        } else {
            showItemListInView();
        }
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class OneImageItem {
        public final String title;
        public final String img_url;
        public final String web_link;

        public OneImageItem(String title, String img_url, String web_link) {
            this.title = title;
            this.img_url = img_url;
            this.web_link = web_link;
        }

        @Override
        public String toString() {
            return " title:" + title + "\n img:" + img_url + "\n" +
                    " web:" + web_link;
        }
    }


    // cook
    private class onLoadJsonDataListener implements LoadJSONUtil.Listener {

        @Override
        public void onLoaded(String jsonData) {

            try {
                GameAnalytics.traceEvent("NETWORK", "launching_code", "MakeData", GameAnalytics.getVersion(), 1, 10);

                JSONObject json = new JSONObject(jsonData);
                JSONObject vodUrlObj = json.getJSONObject("launching").getJSONObject("vodUrl");

//                LoadJSONUtil.getJsonValue(vodUrlObj, "version")


//                mDb = new ImageContentDB(mView.getContext());
//                ITEMS = mDb.getImgContentList("","");
//                showItemListInView();




                for(int index = 0; index < 3; index++){
                    JSONObject oneItem = vodUrlObj.getJSONObject("item" + index);

                    OneImageItem data = new OneImageItem(
                        LoadJSONUtil.getJsonValue(oneItem, "title"),
                        LoadJSONUtil.getJsonValue(oneItem, "imgUrl"),
                        LoadJSONUtil.getJsonValue(oneItem, "webLink")
                    );

                    // save in DB
//                    mDb.addImgContent(data);

                    ITEMS.add(data);
                }




//                JSONArray jArray = json.getJSONArray("photos");
//
//                for(int index = 0; index < jArray.length(); index++){
//                    JSONObject json_data = jArray.getJSONObject(index);
//
//                    OneImageItem data = new OneImageItem(
//                            LoadJSONUtil.getJsonValue(json_data, "title")
//                            , LoadJSONUtil.getJsonValue(json_data, "url")
//                            , LoadJSONUtil.getJsonValue(json_data, "width")
//                            , LoadJSONUtil.getJsonValue(json_data, "height")
//                            , LoadJSONUtil.getJsonValue(json_data, "date_taken")
//                    );
//
//                    ITEMS.add(data);
//                }

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


    public void showItemListInView(){
        // Set the adapter
        if (mView instanceof RecyclerView) {
            Context context = mView.getContext();
            RecyclerView recyclerView = (RecyclerView) mView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyImgItemRecyclerViewAdapter(ITEMS, mListClickListener));
        }
    }

    private boolean ShouldUpdate(){



        return true;
    }


    // coupang list
//    private class onLoadJsonDataListener implements LoadJSONUtil.Listener {
//
//        @Override
//        public void onLoaded(String jsonData) {
//
//            try {
//                JSONObject json = new JSONObject(jsonData);
//
//                JSONArray jArray = json.getJSONArray("photos");
//
//                for(int index = 0; index < jArray.length(); index++){
//                    JSONObject json_data = jArray.getJSONObject(index);
//
//                    OneImageItem data = new OneImageItem(
//                            LoadJSONUtil.getJsonValue(json_data, "title")
//                            , LoadJSONUtil.getJsonValue(json_data, "url")
//                            , LoadJSONUtil.getJsonValue(json_data, "width")
//                            , LoadJSONUtil.getJsonValue(json_data, "height")
//                            , LoadJSONUtil.getJsonValue(json_data, "date_taken")
//                    );
//
//                    ITEMS.add(data);
//                }
//
//                // Set the adapter
//                if (mView instanceof RecyclerView) {
//                    Context context = mView.getContext();
//                    RecyclerView recyclerView = (RecyclerView) mView;
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                    recyclerView.setAdapter(new MyImgItemRecyclerViewAdapter(ITEMS, mListClickListener));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError() {
//
//        }
//    }
}
