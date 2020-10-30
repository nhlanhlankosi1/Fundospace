package com.nhlanhlankosi.fundospace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class BiologyTopicsFragment extends Fragment {

    public static final String TOPIC_ID = "com.nhlanhlankosi.fundospace.TOPIC_ID";

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition(); // gets position of single item on recyclerView

            String biologyTopicName = FundoSpaceTopicsData.biologyTopics[position];

            int itemId = Arrays.asList(FundoSpaceTopicsData.biologyTopics).indexOf(biologyTopicName);

            switch (itemId) {

                case 0:
                    Intent intent0 = new Intent(getContext(), StartActivity.class);
                    intent0.putExtra(TOPIC_ID, "0");
                    startActivity(intent0);
                    break;

                case 1:
                    Intent intent1 = new Intent(getContext(), StartActivity.class);
                    intent1.putExtra(TOPIC_ID, "1");
                    startActivity(intent1);
                    break;

                case 2:
                    Intent intent2 = new Intent(getContext(), StartActivity.class);
                    intent2.putExtra(TOPIC_ID, "2");
                    startActivity(intent2);
                    break;

                case 3:
                    Intent intent3 = new Intent(getContext(), StartActivity.class);
                    intent3.putExtra(TOPIC_ID, "3");
                    startActivity(intent3);
                    break;

                case 4:
                    Intent intent4 = new Intent(getContext(), StartActivity.class);
                    intent4.putExtra(TOPIC_ID, "4");
                    startActivity(intent4);
                    break;

                case 5:
                    Intent intent5 = new Intent(getContext(), StartActivity.class);
                    intent5.putExtra(TOPIC_ID, "5");
                    startActivity(intent5);
                    break;

                case 6:
                    Intent intent6 = new Intent(getContext(), StartActivity.class);
                    intent6.putExtra(TOPIC_ID, "6");
                    startActivity(intent6);
                    break;

                case 7:
                    Intent intent7 = new Intent(getContext(), StartActivity.class);
                    intent7.putExtra(TOPIC_ID, "7");
                    startActivity(intent7);
                    break;

                case 8:
                    Intent intent8 = new Intent(getContext(), StartActivity.class);
                    intent8.putExtra(TOPIC_ID, "8");
                    startActivity(intent8);
                    break;

                case 9:
                    Intent intent9 = new Intent(getContext(), StartActivity.class);
                    intent9.putExtra(TOPIC_ID, "9");
                    startActivity(intent9);
                    break;

                case 10:
                    Intent intent10 = new Intent(getContext(), StartActivity.class);
                    intent10.putExtra(TOPIC_ID, "10");
                    startActivity(intent10);
                    break;

            }

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics_lists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_topics_lists_recycler_view);

        BiologyTopicsListAdapter biologyTopicsListAdapter = new BiologyTopicsListAdapter();

        recyclerView.setAdapter(biologyTopicsListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        biologyTopicsListAdapter.setItemClickListener(onItemClickListener);

        return view;
    }

}
