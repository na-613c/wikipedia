package com.example.wikipedia.Controllers;

import androidx.annotation.NonNull;

import com.example.wikipedia.Controllers.RecyclerView.DataAdapter;
import com.example.wikipedia.Models.SearchWordModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.example.wikipedia.ui.SearchFragment.oldWord;

public class FireBaseController {
    private SearchWordModel searchWordFromDb;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    private List<SearchWordModel> value = new ArrayList<>();

    public void read(final DataAdapter adapter) {

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                searchWordFromDb = dataSnapshot.getValue(SearchWordModel.class);

                if (searchWordFromDb != null) {
                    searchWordFromDb.setKey(dataSnapshot.getKey());

                    value.add(0, searchWordFromDb);
                    updateOldWord();
                    adapter.updateItems(value);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                searchWordFromDb = dataSnapshot.getValue(SearchWordModel.class);

                if (searchWordFromDb != null) {
                    searchWordFromDb.setKey(dataSnapshot.getKey());

                    for (int i = 0; i < value.size(); i++) {
                        if (value.get(i).getKey().equals(searchWordFromDb.getKey())) {
                            value.remove(i);
                        }
                    }

                    updateOldWord();
                    adapter.updateItems(value);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void delete(final String key) {
        myRef.child(key).removeValue();
    }

    public void write(SearchWordModel wordForDB) {
        myRef.push().setValue(wordForDB);
    }

    private void updateOldWord() {
        if (value.size() != 0) {
            oldWord = value.get(0).getWord();
        } else {
            oldWord = "";
        }
    }
}