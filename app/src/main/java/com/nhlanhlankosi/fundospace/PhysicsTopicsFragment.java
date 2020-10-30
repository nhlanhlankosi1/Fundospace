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

public class PhysicsTopicsFragment extends Fragment {

    public static final String TOPIC_ID = "com.nhlanhlankosi.fundospace.TOPIC_ID";

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition(); // gets position of single item on recyclerView

            String physicsTopicName = FundoSpaceTopicsData.physicsTopics[position];

            int itemId = Arrays.asList(FundoSpaceTopicsData.physicsTopics).indexOf(physicsTopicName);

            switch (itemId) {

                case 0:
                    Intent intent0 = new Intent(getContext(), StartActivity.class);
                    intent0.putExtra(TOPIC_ID, "11");
                    startActivity(intent0);
                    break;

                case 1:
                    Intent intent1 = new Intent(getContext(), StartActivity.class);
                    intent1.putExtra(TOPIC_ID, "12");
                    startActivity(intent1);
                    break;

                case 2:
                    Intent intent2 = new Intent(getContext(), StartActivity.class);
                    intent2.putExtra(TOPIC_ID, "13");
                    startActivity(intent2);
                    break;

                case 3:
                    Intent intent3 = new Intent(getContext(), StartActivity.class);
                    intent3.putExtra(TOPIC_ID, "14");
                    startActivity(intent3);
                    break;

                case 4:
                    Intent intent4 = new Intent(getContext(), StartActivity.class);
                    intent4.putExtra(TOPIC_ID, "15");
                    startActivity(intent4);
                    break;

                case 5:
                    Intent intent5 = new Intent(getContext(), StartActivity.class);
                    intent5.putExtra(TOPIC_ID, "16");
                    startActivity(intent5);
                    break;

                case 6:
                    Intent intent6 = new Intent(getContext(), StartActivity.class);
                    intent6.putExtra(TOPIC_ID, "17");
                    startActivity(intent6);
                    break;

                case 7:
                    Intent intent7 = new Intent(getContext(), StartActivity.class);
                    intent7.putExtra(TOPIC_ID, "18");
                    startActivity(intent7);
                    break;

                case 8:
                    Intent intent8 = new Intent(getContext(), StartActivity.class);
                    intent8.putExtra(TOPIC_ID, "19");
                    startActivity(intent8);
                    break;

                case 9:
                    Intent intent9 = new Intent(getContext(), StartActivity.class);
                    intent9.putExtra(TOPIC_ID, "20");
                    startActivity(intent9);
                    break;

                case 10:
                    Intent intent10 = new Intent(getContext(), StartActivity.class);
                    intent10.putExtra(TOPIC_ID, "21");
                    startActivity(intent10);
                    break;

            }

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics_lists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_topics_lists_recycler_view);

        PhysicsTopicsListAdapter physicsTopicsListAdapter = new PhysicsTopicsListAdapter();

        recyclerView.setAdapter(physicsTopicsListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        physicsTopicsListAdapter.setItemClickListener(onItemClickListener);

        return view;
    }

}
