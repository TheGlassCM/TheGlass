package com.example.loginregister;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<User> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_users_rank, null);
        return new ListAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<User> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView username,rank;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            username = itemView.findViewById(R.id.usernameCard);
            rank = itemView.findViewById(R.id.rankCard);
        }

        void bindData(final User item){

            //iconImage.setImageURI(Uri.parse(item.getPhoto().toString()));
            Picasso.with(iconImage.getContext())
                            .load(item.getPhoto().toString())
                                    .into(iconImage);
            username.setText(item.getUsername());
            rank.setText(item.getRank().toString());
        }
    }





}
