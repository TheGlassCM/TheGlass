package com.example.loginregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregister.models.Offer;

import java.util.List;

public class ListAdapterOffer extends RecyclerView.Adapter<ListAdapterOffer.ViewHolder>{

        private List<Offer> mData;
        private LayoutInflater mInflater;
        private Context context;

        public ListAdapterOffer(List<Offer> itemList,Context context){
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
            this.mData = itemList;


        }

    @NonNull
    @Override
    public ListAdapterOffer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.list_offers,null);
            return new ListAdapterOffer.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapterOffer.ViewHolder holder, int position) {
            holder.bindData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView userName,offer;

            ViewHolder(View itemView){
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                offer = itemView.findViewById(R.id.offer);

            }

            void bindData(final Offer item){

                userName.setText(item.getUserName());
                offer.setText(item.getOffer());

            }
    }
}
