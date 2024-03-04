package HomeCardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsmartcare.R;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder> {
    private final homeCardInterface homeCardInterface;
    Context context;
    ArrayList<HomeCardModel> HomeCardAdapter;

    public HomeRecyclerAdapter(HomeCardView.homeCardInterface homeCardInterface, Context context, ArrayList<HomeCardModel> HomeCardAdapter){
        this.homeCardInterface = homeCardInterface;
        this.context = context;
        this.HomeCardAdapter = HomeCardAdapter;

    }

    public void setFiltered(ArrayList<HomeCardModel> HomeCardAdapter){
        this.HomeCardAdapter = HomeCardAdapter;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public HomeRecyclerAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_home_recycler,parent,false);
        return new HomeRecyclerAdapter.HomeViewHolder(view,homeCardInterface);


    }



    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.HomeViewHolder holder, int position) {
      holder.homeOwnerAdapter.setText(HomeCardAdapter.get(position).getOwnerName());
      holder.locationAdapter.setText(HomeCardAdapter.get(position).getLocation());
      holder.phoneNumAdapter.setText(HomeCardAdapter.get(position).getContactNumber());
      holder.homeImageAdapter.setImageResource(HomeCardAdapter.get(position).homeImage);
    }

    @Override
    public int getItemCount() {
        return HomeCardAdapter.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView homeOwnerAdapter, locationAdapter,phoneNumAdapter, moreInfo;
        ImageView homeImageAdapter;
        public HomeViewHolder(@NonNull View itemView, homeCardInterface homeCardInterface) {
            super(itemView);
            homeOwnerAdapter = itemView.findViewById(R.id.ownerName);
            locationAdapter = itemView.findViewById(R.id.location);
            phoneNumAdapter = itemView.findViewById(R.id.contactNumber);
            homeImageAdapter = itemView.findViewById(R.id.imageView);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(homeCardInterface != null){
                       homeCardInterface.onItemClick(getAdapterPosition());
                    }
                }
            });


        }
    }

    }

