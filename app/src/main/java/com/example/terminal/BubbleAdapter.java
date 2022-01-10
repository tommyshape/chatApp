package com.example.terminal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class BubbleAdapter extends FirestoreRecyclerAdapter<BubbleKeeper, BubbleAdapter.NoteHolder> {

    private OnItemClickListener listener;
    private Context mContext;

    public BubbleAdapter(@NonNull FirestoreRecyclerOptions<BubbleKeeper> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull BubbleAdapter.NoteHolder holder, int position, @NonNull BubbleKeeper model) {

        holder.setIsRecyclable(false);

        String str = new String(model.getDate());
        int index = str.indexOf("x");

        if (index>-1){
            String newDate = new String (str.substring(index+1 , index+6));
            holder.dateBubble.setText(newDate);
            holder.messageBubble.setText(model.getMessage());

        }else{
            holder.messageBubble.setText(model.getMessage());
            holder.dateBubble.setText(model.getDate());
        }



        if (model.getSender().equals(Bubble.myUid)){

            //holder.messageBubble.setBackgroundColor(Color.parseColor("#0098FF"));
            holder.messageBubble.setTextColor(Color.parseColor("#FFFFFF"));
            holder.messageBubble.setBackground(mContext.getResources().getDrawable(R.drawable.sent_message));

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.messageBubble,ConstraintSet.RIGHT,R.id.constraintLayout,ConstraintSet.RIGHT,20);
            constraintSet.applyTo(holder.constraintLayout);
        }
        else {
            //holder.messageBubble.setBackgroundColor(Color.parseColor("#E6E6E6"));
            holder.messageBubble.setTextColor(Color.parseColor("#000000"));
            holder.messageBubble.setBackground(mContext.getResources().getDrawable(R.drawable.received_message));

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.messageBubble,ConstraintSet.LEFT,R.id.constraintLayout,ConstraintSet.LEFT,20);
            constraintSet.applyTo(holder.constraintLayout);
        }



    }

    @NonNull
    @Override
    public BubbleAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bubbles, parent, false);

        mContext = parent.getContext();

        return new BubbleAdapter.NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ConstraintLayout constraintLayout;
        TextView messageBubble;
        TextView dateBubble;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);


            constraintLayout= itemView.findViewById(R.id.constraintLayout);
            messageBubble = itemView.findViewById(R.id.messageBubble);
            dateBubble = itemView.findViewById(R.id.dateBubble);
            cardView = itemView.findViewById(R.id.cardviewLayout);

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

    public void setOnItemClickListener(BubbleAdapter.OnItemClickListener listener){
        this.listener = listener;
    }



}
