package BuySell;

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


public class BuySellRecyclerViewAdapter extends RecyclerView.Adapter<BuySellRecyclerViewAdapter.BuySellViewHolder> {


    private final buySellRecyclerInterface buySellRecyclerInterface;
    Context context;
    ArrayList<BuySellModel> buySellModelAdapter;

    public BuySellRecyclerViewAdapter(buySellRecyclerInterface buySellRecyclerInterface, Context context, ArrayList<BuySellModel> buySellModelsAdapter){
        this.buySellRecyclerInterface = buySellRecyclerInterface;
        this.context = context;
        this.buySellModelAdapter = buySellModelsAdapter;

    }

    //Search Field
    public void setFiltered(ArrayList<BuySellModel> buySellModelAdapter){
        this.buySellModelAdapter = buySellModelAdapter;
        notifyDataSetChanged();

    }





    @NonNull
    @Override
    public BuySellRecyclerViewAdapter.BuySellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_buy_sell_recycler,parent,false);
        return new BuySellRecyclerViewAdapter.BuySellViewHolder(view, buySellRecyclerInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull BuySellRecyclerViewAdapter.BuySellViewHolder holder, int position) {
        holder.bookNameAdapter.setText(buySellModelAdapter.get(position).getBookName());
        holder.sellerNameAdapter.setText(buySellModelAdapter.get(position).getSellerName());
        holder.contactNumAdapter.setText(buySellModelAdapter.get(position).getContactNumber());
        holder.bookImageAdapter.setImageResource(buySellModelAdapter.get(position).getBookImage());

    }





    @Override
    public int getItemCount() {
        return buySellModelAdapter.size();
    }



    public  static class BuySellViewHolder extends RecyclerView.ViewHolder{
//holding the views that created from recycler view, similar to oncreate

        TextView bookNameAdapter, sellerNameAdapter, contactNumAdapter, moreInfo;
        ImageView bookImageAdapter;
        Button bookNow;


        public BuySellViewHolder(@NonNull View itemView, buySellRecyclerInterface buySellRecyclerInterface) {
            super(itemView);

            bookNameAdapter = itemView.findViewById(R.id.bookName);
            sellerNameAdapter = itemView.findViewById(R.id.sellerName);
            contactNumAdapter = itemView.findViewById(R.id.contactNo);
            bookImageAdapter = itemView.findViewById(R.id.imageViewBook);
            moreInfo = itemView.findViewById(R.id.moreInfo);



            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the onItemClick method of the interface
                    if (buySellRecyclerInterface != null) {
                        buySellRecyclerInterface.onItemClick(getAdapterPosition());
                    }
                }
            });
}
    }
}


