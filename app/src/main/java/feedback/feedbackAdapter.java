package feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsmartcare.R;

import java.util.ArrayList;

public class feedbackAdapter extends RecyclerView.Adapter<feedbackAdapter.ViewHolder> {
    Context context;
    ArrayList<feedBack> feedBackModels;

    listenerInterface listenerInterface;



    public feedbackAdapter(Context context, ArrayList<feedBack> feedBackModels, listenerInterface listenerInterface) {
        this.context = context;
        this.feedBackModels = feedBackModels;
        this.listenerInterface = listenerInterface;
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
            editButton = itemView.findViewById(R.id.editIcon);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}