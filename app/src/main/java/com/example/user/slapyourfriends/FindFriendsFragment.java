package com.example.user.slapyourfriends;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsFragment extends Fragment {
    public FindFriendsFragment() {}
    private List<ParseUser> mAllUsersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_find_friends, container, false);
        final ListView mFindFriendsListView = (ListView) v.findViewById(R.id.findFriendsListView);

        //todo message if empty list for all fragments
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    mAllUsersList = objects;
                    for (int i = 0; i < mAllUsersList.size(); i++) {
                        if (ParseUser.getCurrentUser() == mAllUsersList.get(i)) {
                            mAllUsersList.remove(mAllUsersList.get(i));
                        }
                    }
                    ArrayAdapter<ParseUser> adapter = new FindFriendAdapter(getActivity(), R.layout.list_item_find_friend, mAllUsersList);
                    mFindFriendsListView.setAdapter(adapter);
                } else {
                    Log.d("ParseException", e.toString());
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class FindFriendAdapter extends ArrayAdapter<ParseUser> {
        private int mResource;
        private List<ParseUser> mUsers;

        public FindFriendAdapter (Context context, int resource, List<ParseUser> users) {
            super(context, resource, users);
            mResource = resource;
            mUsers = users;
        }
        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(mResource, parent, false);
            }
            final ParseUser mCurrentUser = mUsers.get(position);
            TextView mUserTextView = (TextView) row.findViewById(R.id.usernameTextView);
            mUserTextView.setText(mCurrentUser.getUsername());
            final Button mAddFriendButton = (Button) row.findViewById(R.id.addFriendButton);

            ArrayList<ParseUser> mCheckFriendsList = (ArrayList) ParseUser.getCurrentUser().get("friends");
            if (mCheckFriendsList != null) {
                for (int i = 0; i < mCheckFriendsList.size(); i++) {
                    if (mCurrentUser == mCheckFriendsList.get(i)) {
                        mAddFriendButton.setEnabled(false);
                        mAddFriendButton.setText(getString(R.string.addedfriend));
                    }
                }
            }
            mAddFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ParseUser> mFriendsList = (ArrayList) ParseUser.getCurrentUser().get("friends");
                    if (mFriendsList == null) {
                        mFriendsList = new ArrayList<>();
                    }
                    mFriendsList.add(mCurrentUser);
                    ParseUser.getCurrentUser().put("friends", mFriendsList);
                    ParseUser.getCurrentUser().saveInBackground();
                    mAddFriendButton.setEnabled(false);
                    mAddFriendButton.setText(getString(R.string.addedfriend));
                }
            });

            return row;
        }
    }
}
