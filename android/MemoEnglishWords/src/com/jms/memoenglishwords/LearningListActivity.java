package com.jms.memoenglishwords;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jms.memoenglishwords.MainApplication.TrackerName;
import com.jms.memoenglishwords.adapter.WordListAdapter;
import com.jms.memoenglishwords.vo.WordData;

public class LearningListActivity extends ActionBarActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		WordListFragment pFragment = new WordListFragment(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, pFragment).commit();
		}
		
		
		
		//Google Analytics
		MainApplication application = (MainApplication) getApplication();
		Tracker t = application.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName(MainApplication.LEARNING_LIST_ACTIVITY);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	public static class WordListFragment extends Fragment {
		private Context context;
		public ArrayList<WordData> englishWordList = null;
		public ListView wordListView = null;
		public WordListAdapter wordListAdapter = null;
		
		public WordListFragment(Context c) {
			context = c;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.wordlist, container,
					false);
			
			try {
				InputStream wordListSteam = this.getResources().openRawResource(R.raw.words);
				String wordListStr = Utils.readStringFromStream(wordListSteam);
				JSONObject wordList = new JSONObject(wordListStr);
				JSONArray keyArray = wordList.names();
				
				//sort json objects
				List<String> jsonKey = new ArrayList<String>();
				for(int i=0; i<keyArray.length(); i++) {
					jsonKey.add(keyArray.getString(i));
				}
				Collections.sort(jsonKey);
				keyArray = new JSONArray(jsonKey);
				englishWordList = new ArrayList<WordData>();
				
				for (int i=0; i < keyArray.length(); i++) {
					WordData wd = new WordData();
					wd.word = keyArray.getString(i);
					wd.translation = wordList.getString(wd.word);
					englishWordList.add(wd);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			wordListView = (ListView) rootView.findViewById(R.id.wordlist);
			wordListAdapter = new WordListAdapter(context, R.layout.worditem, englishWordList);
			wordListView.setAdapter(wordListAdapter);
			
			return rootView;
		}
	}
}

