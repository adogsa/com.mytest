package jjw.com.fragment.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appdatasearch.DocumentId;
import com.toast.android.analytics.GameAnalytics;
import com.toast.android.analytics.common.utils.JsonUtils;
import com.toast.android.analytics.common.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jjw.com.fragment.ItemFragment;
import jjw.com.fragment.MyImgItemRecyclerViewAdapter;
import jjw.com.fragment.MyItemRecyclerViewAdapter;
import jjw.com.myfirstapp.R;
import jjw.com.utill.DialogUtil;
import jjw.com.utill.LoadJSONUtil;
import jjw.com.utill.TimeUtil;

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
    private long mLastUserUpdateTime = 0;
    private String mRecentVer = "";

    public void makeList(Activity activity, String jsonUrl, View view, ItemFragment.OnListFragmentInteractionListener listener){
        Log.d(TAG,"jjw makeList");

        mView = view;
        mListClickListener = listener;
        try{
            mDb = new ImageContentDB(mView.getContext());
        } catch (Exception e){
            System.err.print("jjw error : " + e.getMessage());
            mDb.close();
        }

        if(ITEMS.size() == 0){

            ITEMS = mDb.getImgContentList("","");

            if(CheckShouldUpdate() == true){
                Log.d(TAG,"jjw makeList call json");

                DialogUtil.showProgressDialog(activity, "서버에서 정보를 받아오는 중입니다.");
                new LoadJSONUtil(new onLoadJsonDataListener()).execute(JSON_URL);
            } else {

                if(ITEMS.size() == 0){
                    DialogUtil.showProgressDialog(activity, "서버에서 정보를 받아오는 중입니다.");
                    new LoadJSONUtil(new onLoadJsonDataListener()).execute(JSON_URL);
                } else {
                    showItemListInView();
                }
            }

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
        public final String food_stuffs;

        public OneImageItem(String title, String img_url, String web_link, String food_stuffs) {
            this.title = title;
            this.img_url = img_url;
            this.web_link = web_link;
            this.food_stuffs = food_stuffs;
        }

        @Override
        public String toString() {
            return " title:" + title + "\n img:" + img_url + "\n" +
                    " web:" + web_link + " foodstuffs:" + food_stuffs;
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
                int itemCount = Integer.valueOf(LoadJSONUtil.getJsonValue(vodUrlObj, "itemCount"));
                boolean isContinueUpdate = "Y".equalsIgnoreCase(LoadJSONUtil.getJsonValue(vodUrlObj, "isContinueUpdate") );
                boolean isEnforceDb = "Y".equalsIgnoreCase(LoadJSONUtil.getJsonValue(vodUrlObj, "isEnforceDb") );
                int contentsCnt = Integer.parseInt(LoadJSONUtil.getJsonValue(vodUrlObj, "itemCount") );

                // if isEnforceDb is Y and

                if(  (isEnforceDb == true && ITEMS.size() != 0 ) || ITEMS.size() == contentsCnt){

                    // Set List adapter
                    showItemListInView();

                    return;
                }

                // take care of update time
                mDb.deleteImgContent();
                ITEMS.clear();

                for(int index = 0; index < itemCount; index++){
                    JSONObject oneItem = vodUrlObj.getJSONObject("item" + index);

                    OneImageItem data = new OneImageItem(
                        LoadJSONUtil.getJsonValue(oneItem, "title").replaceAll("\\[15분 레시피\\] ", ""),
                        LoadJSONUtil.getJsonValue(oneItem, "imgUrl"),
                        LoadJSONUtil.getJsonValue(oneItem, "webLink"),
                        (LoadJSONUtil.getJsonValue(oneItem, "foodStuffs")).replaceAll(" ", "")
                    );

                    Log.d(TAG,"save imgContent");
                    // save in DB
                    mDb.addImgContent(data);

                    ITEMS.add(data);
                }

                // take care of update time
                mDb.deleteLastUpdateMilliTime();

                if(isContinueUpdate != true){
                    Log.d(TAG,"save milli time");
                    // save update time
                    saveLastUpdateMilliTime(TimeUtil.getTodayMilli());
                }

                // Set List adapter
                showItemListInView();

            } catch (JSONException e) {
                e.printStackTrace();
                saveLastUpdateMilliTime(1);
            }
        }

        @Override
        public void onError() {

        }
    }


    public void showItemListInView(){
        // Set List adapter
        if (mView instanceof RecyclerView) {
            Context context = mView.getContext();
            RecyclerView recyclerView = (RecyclerView) mView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyImgItemRecyclerViewAdapter(ITEMS, mListClickListener));
            recyclerView.getAdapter().notifyDataSetChanged();

            DialogUtil.hideAllProgressDialog();
        }
    }

    public void searchListWithKwd(String column, String keyword){
        ITEMS = mDb.getImgContentList(column, keyword);
        showItemListInView();
    }

    private boolean CheckShouldUpdate(){
        Log.d(TAG, "CheckShouldUpdate");

        // when server update data
        Calendar timeOfServerUpdate= Calendar.getInstance ( );
        timeOfServerUpdate = TimeUtil.getLastDayOfWeek(timeOfServerUpdate, Calendar.THURSDAY);
        Log.d(TAG, "jjw  calendar cal :" + timeOfServerUpdate.getTime());


        // when user update
        Calendar timeOfUserUpdate= Calendar.getInstance ( );
        timeOfUserUpdate.setTime(new Date(getLastUpdateMilliTime())); ;
        Log.d(TAG, "jjw  calendar cal2 :" + timeOfUserUpdate.getTime());

        Log.d(TAG, "jjw  calendar cal cal2 :" + timeOfServerUpdate.compareTo(timeOfUserUpdate));


        return (timeOfServerUpdate.compareTo(timeOfUserUpdate)) > 0;
    }

    private long getLastUpdateMilliTime(){

        mLastUserUpdateTime = mDb.getLastUpdateMilliTime();

        return mLastUserUpdateTime;
    }

    private void saveLastUpdateMilliTime(long updateTime){

        if(mLastUserUpdateTime == 0){
            mDb.addLastUpdateMilliTime(updateTime);
        } else {
            mDb.updateLastUpdateMilliTime(updateTime);
        }
    }

}
