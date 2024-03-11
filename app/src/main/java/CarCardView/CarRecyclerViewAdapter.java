package CarCardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentsmartcare.R;

import java.util.ArrayList;

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.CarViewHolder> {
private final carCardInterface carCardInterface;

    Context context;
ArrayList<CarCardModel> carCardModelAdapter;
public CarRecyclerViewAdapter(CarCardView.carCardInterface carCardInterface, Context context, ArrayList<CarCardModel> carCardModelAdapter){
    this.carCardInterface = carCardInterface;
    this.context = context;
    this.carCardModelAdapter = carCardModelAdapter;

}

    public void setFiltered(ArrayList<CarCardModel> carCardModelAdapter) {
        this.carCardModelAdapter = carCardModelAdapter;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CarRecyclerViewAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //inflate layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_car_recycler,parent,false);
        return new CarRecyclerViewAdapter.CarViewHolder(view, carCardInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CarRecyclerViewAdapter.CarViewHolder holder, int position) {
    //assigning value from CarViewHolder to variable holder
        holder.ownerNameAdapter.setText(carCardModelAdapter.get(position).getOwnerName());
        holder.carModelAdapter.setText(carCardModelAdapter.get(position).getCarModel());
        holder.carNumberAdapter.setText(carCardModelAdapter.get(position).getCarNumber());
        holder.carImageAdapter.setImageResource(carCardModelAdapter.get(position).getCarImage());


    }

    @Override
    public int getItemCount() {
    //numbers of recycler views
        return carCardModelAdapter.size();
    }



    public  static class CarViewHolder extends RecyclerView.ViewHolder{
//holding the views that created from recycler view, similar to oncreate

        TextView ownerNameAdapter, carModelAdapter, carNumberAdapter, moreInfo;
        Button bookNow;
        ImageView carImageAdapter;

        public CarViewHolder(@NonNull View itemView, carCardInterface carCardInterface) {
            super(itemView);

            ownerNameAdapter = itemView.findViewById(R.id.ownerName);
            carModelAdapter = itemView.findViewById(R.id.route);
            carNumberAdapter = itemView.findViewById(R.id.carModel);
            carImageAdapter = itemView.findViewById(R.id.imageView);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            bookNow= itemView.findViewById(R.id.bookNowButton);



            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (carCardInterface != null) {
                        carCardInterface.onItemClick(getAdapterPosition());

                }
                }
            });
            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (carCardInterface != null) {
                        carCardInterface.onBookNowClick(getAdapterPosition());

                    }
                }
            });


        }
    }
}



