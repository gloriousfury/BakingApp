package com.gloriousfury.bakingapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloriousfury.bakingapp.R;
import com.gloriousfury.bakingapp.model.Step;
import com.gloriousfury.bakingapp.ui.activity.SingleRecipeActivity;
import com.gloriousfury.bakingapp.ui.activity.SingleStepActivity;

import java.util.ArrayList;
import java.util.List;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    Context context;
    private List<Step> step_list;
    String STEP_ITEM_KEY = "step_item";



    public StepAdapter(Context context, List<Step> StepList) {
        this.context = context;
        this.step_list = StepList;


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView stepName;


        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
           stepName = (TextView) view.findViewById(R.id.step_short_desc);
//            stepNo = (TextView) view.findViewById(R.id.step_no);

        }

        @Override
        public void onClick(View view) {
            Step step = step_list.get(getAdapterPosition());

            Bundle stepParameters = new Bundle();
            stepParameters.putParcelable(STEP_ITEM_KEY,step);

            ((SingleRecipeActivity) context).onDescriptionSelected(stepParameters,getAdapterPosition());

//            Intent intent = new Intent(context, DetailActivity.class);
//            intent.putExtra("movieItem", movie);
//            context.startActivity(intent);

//            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_description_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int stepNo= position +1;
        String description = step_list.get(position).getShortDescription();

        holder.stepName.setText(String.valueOf(stepNo)+ "." + " "+ description);
//        holder.stepNo.setText(String.valueOf(position+1));

//        Picasso.with(context).load(poster_img_path).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {

        if(step_list.size()==0){

            return 0;
        }else {
            return step_list.size();
        }
    }


    public void setStepData(List<Step> StepData) {
        step_list = StepData;
        notifyDataSetChanged();
    }

    public void appendStep(ArrayList<Step> StepList) {
        step_list.addAll(StepList);
        notifyDataSetChanged();
    }

}