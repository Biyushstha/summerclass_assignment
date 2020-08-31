package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    public CategoryAdapter(List<CatgegoryModel> catgegoryModelList) {
        this.catgegoryModelList = catgegoryModelList;
    }

    private List<CatgegoryModel> catgegoryModelList;
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_activity,parent, false);
        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(catgegoryModelList.get(position).getUrl(),catgegoryModelList.get(position).getName(),catgegoryModelList.get(position).getSets());

    }

    @Override
    public int getItemCount() {
        return catgegoryModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        private CircleImageView imageView;
        private TextView title;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);

        }
        private void setData(String url, final String Title, final int sets){
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(Title);

            itemView.setOnClickListener(new View.OnClickListener() {
              @Override

                public void onClick(View view) {
                  Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("title", Title);
                    setIntent.putExtra("sets", sets);
                    itemView.getContext().startActivity(setIntent);

                }
            });


        }
    }
}

