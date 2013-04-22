package com.shootemoff.game;

import android.os.Parcel;
import android.os.Parcelable;

public class ScoreObject implements Parcelable
{
	public ScoreObject(){}
	
	public ScoreObject(String newName, Integer newScore)
	{
		name = newName;
		score = newScore;
	}
	
	public String name;
	public int score;
	
	// Parcelling part
    public ScoreObject(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.score = Integer.parseInt(data[1]);

    }

   // @Оverride
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name,
                                            Integer.toString(this.score)});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ScoreObject createFromParcel(Parcel in) {
            return new ScoreObject(in); 
        }

        public ScoreObject[] newArray(int size) {
            return new ScoreObject[size];
        }
    };
}