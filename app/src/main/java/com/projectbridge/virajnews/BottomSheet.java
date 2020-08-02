package com.projectbridge.virajnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static android.content.Context.MODE_PRIVATE;

public class BottomSheet extends BottomSheetDialogFragment{
    SharedPreferences pref;
    Set<String> topicSet;
    List<String> topicList;
    bottomSheetAdapter madapter;
    EditText addTopic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);
        RecyclerView savedTopicList = rootView.findViewById(R.id.savedTopicsList);
        addTopic = rootView.findViewById(R.id.AddTopic);
        Button add = rootView.findViewById(R.id.add);

        pref = Objects.requireNonNull(getContext()).getSharedPreferences("VirajNews", MODE_PRIVATE);
        topicSet = pref.getStringSet("TopicList", null);
        assert topicSet != null;
        topicList = new ArrayList<>(topicSet);

        madapter = new bottomSheetAdapter(topicList, topicSet);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        savedTopicList.setLayoutManager(mLayoutManager);
        savedTopicList.setItemAnimator(new DefaultItemAnimator());
        savedTopicList.setAdapter(madapter);

        add.setOnClickListener(view -> addToTopicList());

        return rootView;
    }

    private void addToTopicList(){
        if(!addTopic.getText().toString().isEmpty()) {
            topicSet.add(addTopic.getText().toString());

            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putStringSet("TopicList", topicSet);
            prefEditor.apply();

            topicList.add(addTopic.getText().toString());
            madapter.notifyDataSetChanged();

            addTopic.setText("");
        }
    }
}

class bottomSheetAdapter extends RecyclerView.Adapter<bottomSheetAdapter.MyViewHolder>{
    SharedPreferences pref;
    private List<String> feedItems;
    private Set<String> feedSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView topicName;
        public Button delete;

        MyViewHolder(@NonNull View view) {
            super(view);
            topicName = view.findViewById(R.id.topicName);
            delete = view.findViewById(R.id.delete);
        }
    }

    bottomSheetAdapter(List<String> feedItems, Set<String> feedSet) {
        this.feedItems = feedItems;
        this.feedSet = feedSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_topic_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.topicName.setText(feedItems.get(position));

        holder.delete.setOnClickListener(view -> {
            removeFromTopicList(feedItems.get(position));
        });
    }

    private void removeFromTopicList(String item){
        feedSet.remove(item);

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putStringSet("TopicList", feedSet);
        prefEditor.apply();

        feedItems.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}