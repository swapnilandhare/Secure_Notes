package com.example.securenotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Model> mList;
    Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child(user.getUid());
    public MyAdapter(Context context,ArrayList<Model> mList)
    {
        this.mList=mList;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Model model=mList.get(position);
        holder.topic.setText(model.getTopic());
        holder.subject.setText(model.getSubject());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,NoteDetails.class);
                intent.putExtra("topic",model.getTopic());
                intent.putExtra("subject",model.getSubject());
                intent.putExtra("key",model.getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.expandedMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,holder.expandedMenu);
                popupMenu.inflate(R.menu.expandedmenu);
                popupMenu.setOnMenuItemClickListener(item -> {
                        switch(item.getItemId())
                        {
                            case R.id.edit:
                                Intent intent=new Intent(context,NoteDetails.class);
                                intent.putExtra("topic",model.getTopic());
                                intent.putExtra("subject",model.getSubject());
                                intent.putExtra("key",model.getKey());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                            case R.id.delete:
                                root.child(model.getKey()).removeValue();
                                Toast.makeText(context, "Note deleted...",Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                break;

                        }
                        return false;
                });
                popupMenu.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView expandedMenu;
        TextView topic,subject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            topic=itemView.findViewById(R.id.tvTopic);
            subject=itemView.findViewById(R.id.tvSubject);
            expandedMenu=itemView.findViewById(R.id.expanded_menu);
        }
    }
}
