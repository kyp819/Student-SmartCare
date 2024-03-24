// feedbackAdapter.java
package feedback;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.studentsmartcare.R;

import java.util.HashMap;
import java.util.Map;

public class feedbackAdapter extends FirebaseRecyclerAdapter<feedBackModel, feedbackAdapter.ViewHolder> {
    Context context;
    ListenerInterface listenInterface;
    String userId;



    public feedbackAdapter(@NonNull FirebaseRecyclerOptions<feedBackModel> options, ListenerInterface listenInterface, Context context, String userId) {
        super(options);
        this.listenInterface = listenInterface;
        this.context = context;
        this.userId = userId;
    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull feedBackModel feedBackmodel) {
        holder.fullName.setText(feedBackmodel.getFullName());
        holder.comment.setText(feedBackmodel.getComment());


        if (feedBackmodel.getUserId().equals(userId)) {
            // If yes, show the edit and delete icons
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            // If not, hide the edit and delete icons
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String model = getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey();
                        listenInterface.onDelete(model);
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




        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.editButton.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.activity_update_feedback)).setExpanded(false, 1200)
                        .create();

                View mView = dialogPlus.getHolderView();
                EditText fullNameEdit = mView.findViewById(R.id.editTextFullName);
                EditText commentEdit = mView.findViewById(R.id.editTextComment);
                Button updateButton = mView.findViewById(R.id.buttonUpdate);
                Button cancelButton = mView.findViewById(R.id.buttonCancel);

                fullNameEdit.setText(feedBackmodel.getFullName());
                commentEdit.setText(feedBackmodel.getComment());

                dialogPlus.show();

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fullName = fullNameEdit.getText().toString().trim();
                        String comment = commentEdit.getText().toString().trim();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        Map<String, Object> update = new HashMap<>();
                        update.put("fullName", fullName);
                        update.put("comment", comment);

                        FirebaseDatabase.getInstance().getReference().child("Feedback")
                                .child(getRef(holder.getBindingAdapterPosition()).getKey()).updateChildren(update)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();

                                        Toast.makeText(context, "Feedback updated successfully", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                        Toast.makeText(context, "Failed to update feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }


                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();

                    }
                });
            }
        });








    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feedback_store, parent, false);
        return new ViewHolder(view);
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, comment;
        ImageView editButton, deleteButton;
        String userId;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.userComment);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
