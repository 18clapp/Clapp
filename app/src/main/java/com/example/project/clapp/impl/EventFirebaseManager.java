package com.example.project.clapp.impl;

import android.util.Log;

import com.example.project.clapp.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ruigo on 12/03/2018.
 */

public class EventFirebaseManager implements IEvent{
    public ArrayList<Event> eventList = new ArrayList<>();


    public ArrayList<Event> getEventList() {
        return eventList;
    }

    static EventFirebaseManager efm = null;
    private static final String TAG = "FirebaseTest";

    public static EventFirebaseManager getInstance() {
        if(efm == null) {
            efm = new EventFirebaseManager();
        }
        return efm;
    }

    @Override
    public Event getEvent(int id) {
        return null;
    }

    @Override
    public void getEvents() {
        DatabaseReference dataEvents;
        dataEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventListRef = dataEvents.child("events");
            eventListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String id = ds.child("id").getValue(String.class);
                        String nome = ds.child("name").getValue(String.class);
                        String uID = ds.child("uID").getValue(String.class);
                        Event event = new Event(id, nome, uID);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.toString());
                }
            });
    }


    @Override
    public void addEvent(String nameEvent, String dateEvent, String timeEvent, String localEvent, String durationEvent, String priceEvent, String descEvent, String userId) {
        DatabaseReference databaseEvents;
        databaseEvents = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pushedPostRef = databaseEvents.push();
        String postId = pushedPostRef.getKey();
        System.out.println(postId);
        Event event = new Event(postId, nameEvent, userId, "https://ih0.redbubble.net/image.342699943.3651/flat,800x800,070,f.u1.jpg", localEvent, dateEvent, timeEvent, durationEvent, descEvent, "", 0, 100, Integer.parseInt(priceEvent));
        databaseEvents.child("events").child(postId).setValue(event);

    }
}