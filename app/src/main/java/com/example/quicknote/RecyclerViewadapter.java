package com.example.quicknote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewadapter extends FirebaseRecyclerAdapter<Note, RecyclerViewadapter.NotesVieHoleder> {
    Context parent;



    public RecyclerViewadapter(@NonNull FirebaseRecyclerOptions<Note> options, Context context) {
        super(options);
        parent = context;

    }



    @Override
    protected void onBindViewHolder(@NonNull NotesVieHoleder holder, @SuppressLint("RecyclerView") int position, @NonNull Note model) {

        holder.notetitle.setText(model.getTitle());
        holder.notedescription.setText(model.getDescription());
        holder.notetimestamp.setText(model.getTimestamp());

        holder.update.setOnClickListener(new View.OnClickListener() {
            EditText notetitleupdate, notedescriptionupdate;
            TextView notetimeupdate;
            Button updatebutton;

            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(parent).inflate(R.layout.formupdatenote, null);

                notetitleupdate = v.findViewById(R.id.updatetitlename);
                notedescriptionupdate = v.findViewById(R.id.updatetitledescription);
                notetimeupdate = v.findViewById(R.id.tvtimeStampupdate);
                updatebutton = v.findViewById(R.id.noteupdatebtn);

                notetitleupdate.setText(model.getTitle());
                notedescriptionupdate.setText(model.getDescription());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date date = new Date();
                notetimeupdate.setText(formatter.format(date));

                AlertDialog.Builder update = new AlertDialog.Builder(parent)
                        .setView(v);
                final AlertDialog dialog = update.create();
                dialog.setCancelable(false);
                updatebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = notetitleupdate.getText().toString();
                        String desc = notedescriptionupdate.getText().toString();
                        String time = notetimeupdate.getText().toString();
                        if (name.isEmpty() || desc.isEmpty()) {
                            Toast.makeText(parent, "Field Empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("title", name);
                            data.put("description", desc);
                            data.put("timestamp", time);
                            getRef(position).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(parent, "Data Updates Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });

                dialog.show();

            }

        });

    }

    @NonNull
    @Override
    public NotesVieHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_note_design, parent, false);
        return new NotesVieHoleder(v);
    }


    public class NotesVieHoleder extends RecyclerView.ViewHolder {
        TextView notetitle, notedescription, notetimestamp;
        ImageView update;

        public NotesVieHoleder(@NonNull View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.notename);
            notedescription = itemView.findViewById(R.id.notedescription);
            notetimestamp = itemView.findViewById(R.id.timestamp);
            update = itemView.findViewById(R.id.updateimg);

        }
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();
        notifyItemRemoved(position);
    }





}
