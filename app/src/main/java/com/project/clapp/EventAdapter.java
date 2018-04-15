package com.project.clapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.clapp.models.Event;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by ruigo on 16/03/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> eventList;
    private Context context;
    private StorageReference mStorageRef;
    private int listType;




    public EventAdapter(ArrayList<Event> eventList, int i) {
        listType = i;
        this.eventList = eventList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        View viewItem;
        if (listType == 1) {
            viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        } else {
            viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_calendar, parent, false);
        }
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Event event = eventList.get(position);
        holder.nameEvent.setText(event.getName());
       // holder.localEvent.setText(event.getLocal());
       // holder.hourEvent.setText(event.getTime());

        if (listType == 1) {
//            holder.dateEvent.setText(event.getDate());
            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference eventImg = mStorageRef.child("events").child(event.getImgURL());

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final File newFile = localFile;
            eventImg.getFile(newFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...

                            holder.imageEvent.setImageURI(Uri.fromFile(newFile));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }


            });
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("id", event.getId());
                context.startActivity(intent);
            }
        });







    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameEvent, localEvent, hourEvent, dateEvent;
        protected ImageView imageEvent;
        private CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            if (listType == 1) {
                nameEvent = itemView.findViewById(R.id.nameEvent);
                //localEvent = itemView.findViewById(R.id.localEvent);
                //hourEvent = itemView.findViewById(R.id.timeEvent);
                //dateEvent = itemView.findViewById(R.id.dateEvent);
                imageEvent = itemView.findViewById(R.id.imgEvent);
                cv = itemView.findViewById(R.id.cardEvent);

            } else {
                nameEvent = itemView.findViewById(R.id.nameEventCal);
                localEvent = itemView.findViewById(R.id.localEventCal);
                hourEvent = itemView.findViewById(R.id.timeEventCal);
                cv = itemView.findViewById(R.id.cardCalEvent);
            }


        }

    }
    /*@Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.some_layout, parent, false);

        //findViewById...

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }*/


}
