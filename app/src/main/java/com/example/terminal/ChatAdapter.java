package com.example.terminal;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChatAdapter extends FirestoreRecyclerAdapter<People, ChatAdapter.NoteHolder> {

    private OnItemClickListener listener;


    public ChatAdapter(@NonNull FirestoreRecyclerOptions<People> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull People model) {

        holder.textViewName.setText(model.getName());
        holder.textViewLastName.setText(model.getEmail());

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_layout, parent, false);

        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewLastName;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.discover_name);
            textViewLastName = itemView.findViewById(R.id.discover_last_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}