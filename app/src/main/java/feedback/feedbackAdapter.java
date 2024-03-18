package feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.studentsmartcare.R;

import java.util.ArrayList;




public class feedbackAdapter extends RecyclerView.Adapter<feedbackAdapter.ViewHolder> {

    feedbackDBHelper feedbackDBHelper;


    feedbackAdapter feedbackAdapter;
    feedBackDao feedBackDao;


    Context context;
    ArrayList<feedBack> feedBackModels;

    listenerInterface listenerInterface;


      String currentUserID;
    public feedbackAdapter(Context context, ArrayList<feedBack> feedBackModels, listenerInterface listenerInterface, String currentUserID) {
        this.context = context;
        this.feedBackModels = feedBackModels;
        this.listenerInterface = listenerInterface;
        this.currentUserID = currentUserID;
    }


    public void addData(feedBack feedBack){
       feedBackModels.add(feedBack);
        notifyDataSetChanged();


    }

    public void clearData(){
        feedBackModels.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public feedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_feedback_store,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull feedbackAdapter.ViewHolder holder, int position) {

        feedBack feedBackModel = feedBackModels.get(position);

        holder.fullName.setText(feedBackModel.getUserName());
        holder.comment.setText(feedBackModel.getComment());



        //visibility button hidden for other users
         currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!feedBackModel.getUserId().equals(currentUserID)) {
            holder.deleteButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);

        }

        holder.editButton.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                     listenerInterface.onUpdate(feedBackModel);

                                                 }

        });

        //deletion function for delete holder
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerInterface.onDelete(feedBackModel.getId(), position);
                        dialog.dismiss();
                        Toast.makeText(context, "Deleted feedback", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

    });
    }

    @Override
    public int getItemCount() {
        return feedBackModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
TextView fullName, comment;
ImageView editButton, deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.userComment);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }


}
