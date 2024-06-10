package com.example.quicknote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fbtn;
    RecyclerView recyclerView;
    RecyclerViewadapter adapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();


        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder add;
                add=new AlertDialog.Builder(MainActivity.this);
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.formaddnote,null,false);
                add.setView(view);
                final AlertDialog dialog=add.create();
                dialog.setCancelable(false);
                Button addnoteinfobtn=view.findViewById(R.id.noteaddedbtn);
                addnoteinfobtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingInflatedId")
                    @Override
                    public void onClick(View v) {
                        EditText title,notedescription;
                        TextView tvtime;
                        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                        Date date=new Date();
                        title=view.findViewById(R.id.titlename);
                        notedescription=view.findViewById(R.id.titledescription);
                        tvtime=view.findViewById(R.id.tvtimeStamp);

                        tvtime.setText(formatter.format(date));
                        String ettitle=title.getText().toString().trim();
                        String etnotedescription=notedescription.getText().toString().trim();


                        if(ettitle.isEmpty()||etnotedescription.isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Something is missing", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                        else
                        {
                            HashMap<String,Object> data=new HashMap<>();
                            data.put("title",ettitle);
                            data.put("description",etnotedescription);
                            data.put("timestamp",tvtime.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("Notes")
                                    .push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }
                    }
                });



                dialog.show();


            }

        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
    private void init()
    {
        fbtn=findViewById(R.id.fabbtn);
        recyclerView=findViewById(R.id.rv);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Notes");

        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(query, Note.class)
                        .build();
        adapter=new RecyclerViewadapter(options,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}