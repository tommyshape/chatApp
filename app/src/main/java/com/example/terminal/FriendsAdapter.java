package com.example.terminal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class FriendsAdapter extends FirestoreRecyclerAdapter<People, FriendsAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public FriendsAdapter(@NonNull FirestoreRecyclerOptions<People> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull FriendsAdapter.NoteHolder holder, int position, @NonNull People model) {


            holder.textViewName.setText(model.getName());
            holder.textViewLastName.setText(model.getEmail());

        //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));





    }

    @NonNull
    @Override
    public FriendsAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_layout, parent, false);

        return new FriendsAdapter.NoteHolder(v);
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

    public void setOnItemClickListener(FriendsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }



}
