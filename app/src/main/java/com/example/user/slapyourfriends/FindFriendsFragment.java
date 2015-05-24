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

import java.util.List;

public class FindFriendsFragment extends Fragment {
    public FindFriendsFragment() {}
    private List<ParseUser> mAllUsersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    mAllUsersList = objects;
                } else {
                    Log.d("ParseException", e.toString());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_find_friends, container, false);

        ListView mFindFriendsListView = (ListView) v.findViewById(R.id.findFriendsListView);

        ArrayAdapter<ParseUser> adapter = new FindFriendAdapter(getActivity(), R.layout.list_item_find_friend, mAllUsersList);
        mFindFriendsListView.setAdapter(adapter);
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
        //creating variables
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
            TextView mUserNameTextView = (TextView) row.findViewById(R.id.usernameTextView);
            mUserNameTextView.setText(mCurrentUser.getUsername());
            Button mAddFriendButton = (Button) row.findViewById(R.id.addFriendButton);

            return row;
        }
    }
}
