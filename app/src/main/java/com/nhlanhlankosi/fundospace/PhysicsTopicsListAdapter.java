package com.nhlanhlankosi.fundospace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhysicsTopicsListAdapter extends RecyclerView.Adapter {

    private View.OnClickListener onItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_list_item, viewGroup,
                false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ListViewHolder) viewHolder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return FundoSpaceTopicsData.physicsTopics.length;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mItemText;

        public ListViewHolder(View itemView) {
            super(itemView);

            mItemText = itemView.findViewById(R.id.item_text);

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);

        }

        public void bindView(int position) {
            mItemText.setText(FundoSpaceTopicsData.physicsTopics[position]);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
