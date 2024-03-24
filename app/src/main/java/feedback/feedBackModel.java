package feedback;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class feedBackModel implements Serializable {
    private String fullName;
   private String comment;
    private String userId;

    public feedBackModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public feedBackModel(String fullName, String comment, String userId) {
        this.fullName = fullName;
        this.comment = comment;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}