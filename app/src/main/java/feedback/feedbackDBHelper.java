package feedback;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {feedBack.class}, version = 2)
public abstract class feedbackDBHelper extends RoomDatabase{
    public abstract feedBackDao getDao();
    public static feedbackDBHelper INSTANCE;

    public  static feedbackDBHelper getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, feedbackDBHelper.class, "feedBackDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();


        }
        return INSTANCE;
    }
}

