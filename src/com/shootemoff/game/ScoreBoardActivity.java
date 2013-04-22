package com.shootemoff.game;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shootemoff.shootemoffgame.R;

public class ScoreBoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_board);
		
		Bundle data = getIntent().getExtras();
		
		int size = data.getInt("score_count");
		
		ScoreObject[] scores = new ScoreObject[size];
		
		for(int i = 0; i < size; i++){
			String name = "score_" + i;
			scores[i] = data.getParcelable(name);
		}

		TextView text = (TextView)findViewById(R.id.header);
		
		ListView scoreListView = (ListView)findViewById(R.id.list);
		String[] scoreArray = new String[size];
		
		if(size > 0){
			text.setText("there is some score!");
		}
		
		for(int i = 0; i < size; i++){
			String score_line = "NAME : " + scores[i].name + " SCORE : " +scores[i].score ;
			scoreArray[i] = score_line;
		}
		
		// Create and populate a List of names and scores.
		ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(scoreArray) );
		
	    // Create ArrayAdapter using the planet list.  
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
	    
	    scoreListView.setAdapter( listAdapter );
	    
//		SavedFiles = getApplicationContext().fileList();
//		   ArrayAdapter<String> adapter
//		   = new ArrayAdapter<String>(this,
//		     android.R.layout.simple_list_item_1,
//		     SavedFiles);
//
//	   listSavedFiles.setAdapter(adapter);
//	   
//	   if(SavedFiles.length > 0){
//		   text.setText("1");
//	   }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_board, menu);
		return true;
	}
	
	public void BackToMenu(View view)
	{
		Intent intent = new Intent (this, StartScreenActivity.class);
		startActivity(intent);
	}
	
	public void onBackPressed(){
		//Nothing to do
		super.onBackPressed();
	}

}
