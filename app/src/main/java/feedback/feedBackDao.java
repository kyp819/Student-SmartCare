package feedback;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface feedBackDao {

    @Query( "SELECT * FROM feedBackTable")
    List<feedBack> getAllFeedback();

    @Insert
    void addFeedback(feedBack feedBack);

    @Update
    void updateFeedback(feedBack feedBack);


    @Query("DELETE FROM feedBackTable WHERE id = :id")
    void deleteFeedback(int id);


}
