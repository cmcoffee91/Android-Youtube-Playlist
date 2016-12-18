package com.chriscoffee.youtubeplaylist.youtubeplaylist;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophercoffee on 12/17/16.
 */

public class CM_youtubePlaylist extends Fragment {


    List<Videos> displaylistArray = new ArrayList<Videos>();
    private RecyclerView mVideoRecyclerView;
    private YT_recycler_adapter mVideoAdapter;
    Context context;
    private String playlist_id;
    private String browserKey;
    String loadMsg;
    String loadTitle;


    //onCreateView...
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        //show life-cycle event in LogCat console...
       context = getActivity().getApplicationContext();
        View thisScreensView = inflater.inflate(R.layout.cm_yt_playlist_recycler, container, false);








        //show a long toast so user knows this screen is blank intentionally...
        //showToast("This screen is blank intentionally. See CM_anWatchList.java to see how little is required to launch a simple plugin.", "long");
        playlist_id = "yourPlaylistId";
        int videoTxtColor = Color.parseColor("#000000");
        loadTitle = "Loading...";
        loadMsg = "Loading your videos...";
        browserKey = "yourBrowserKey";
        int cornerRadius = 5;
        int cardColor = Color.parseColor("#FFFFFF");
        int recyclerColor = Color.parseColor("#ff0000");


        mVideoRecyclerView = (RecyclerView) thisScreensView.findViewById(R.id.yt_recycler_view);
        mVideoRecyclerView.setBackgroundColor(recyclerColor);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoAdapter = new YT_recycler_adapter(displaylistArray, browserKey, getActivity(), cornerRadius, cardColor, videoTxtColor);
        mVideoRecyclerView.setAdapter(mVideoAdapter);

        new TheTask().execute();

        mVideoAdapter.notifyDataSetChanged();

        //return the layout file as the view for this screen..
        return thisScreensView;


    }//onCreateView...


    private class TheTask extends AsyncTask<Void,Void,Void>
    {

        Videos displaylist;
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(loadTitle);
            dialog.setMessage(loadMsg);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try
            {

                String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + playlist_id+ "&key=" + browserKey + "&maxResults=50";

                String response = getUrlString(url);

                JSONObject json = new JSONObject(response.toString());

                JSONArray jsonArray = json.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    JSONObject video = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                    String title = jsonObject.getJSONObject("snippet").getString("title");

                    String id = video.getString("videoId");

                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");


                    displaylist = new Videos(title, thumbUrl ,id);
                    displaylistArray.add(displaylist);
                }


            }
            catch(Exception e1)
            {
                e1.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mVideoAdapter.notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

    }



    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
