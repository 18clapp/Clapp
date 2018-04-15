package com.project.clapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.project.clapp.impl.EventFirebaseManager;
import com.project.clapp.models.Event;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Going extends Fragment {


    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "FirebaseTest";
    EventAdapter eventAdapter;
    RecyclerView rv;
    ArrayList<String> tags = new ArrayList<>();
    Button filterBtn;
    ArrayList<Event> EVENTS = new ArrayList<>();
    ArrayList<Event> EVENTSGoing = new ArrayList<>();

    public Going() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_all_events, container, false);

        mAuth = FirebaseAuth.getInstance();
        rv = rootView.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(llm);

        fillEventList(rootView);


        filterBtn = rootView.findViewById(R.id.filtBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEvents(rootView);
            }
        });



        return rootView;
    }

    public void filterEvents(final View view) {
        final CharSequence[] items = {"Workshop","Lecture","Documentary","Tutorial","Dinner","Fun Activity"};

        final ArrayList selectedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Tags to filter by")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(items[indexSelected]);
                        } else if (selectedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(items[indexSelected]);
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        tags.clear();
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        for (int i = 0; i < selectedItems.size(); i++) {
                            tags.add(selectedItems.get(i).toString());
                        }

                        fillEventList(view);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    public void fillEventList(View view) {
        EVENTS = EventFirebaseManager.getInstance().getEventListTagFilter(tags);

        for (int i = 0; i < EVENTS.size(); i++) {
            if (EVENTS.get(i).getUserList().contains(mAuth.getCurrentUser().getUid())){
                EVENTSGoing.add(EVENTS.get(i));
            }
        }

        eventAdapter = new EventAdapter(EVENTSGoing, 1);
        rv.setAdapter(eventAdapter);

    }

}
