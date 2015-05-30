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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

public class SlapFriendsFragment extends Fragment {
    public SlapFriendsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_slap_friends, container, false);
        final ListView mSlapFriendsListView = (ListView) v.findViewById(R.id.slapFriendsListView);
        TextView mSlapFriendsHeadingTextView = (TextView) v.findViewById(R.id.slapFriendHeadingTextView);

        ArrayList mFriendsList = (ArrayList) ParseUser.getCurrentUser().get("friends");
        if (mFriendsList == null) {
            mSlapFriendsHeadingTextView.setText(getString(R.string.nofriendstoslap));
        }
        else {
            ArrayAdapter<ParseUser> adapter = new SlapFriendsAdapter(getActivity(), R.layout.list_item_slap_friend, mFriendsList);
            mSlapFriendsListView.setAdapter(adapter);
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "slap friends fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class SlapFriendsAdapter extends ArrayAdapter<ParseUser> {
        private int mResource;
        private ArrayList<ParseUser> mFriends;

        public SlapFriendsAdapter (Context context, int resource, ArrayList<ParseUser> friends) {
            super(context, resource, friends);
            mResource = resource;
            mFriends = friends;
        }
        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(mResource, parent, false);
            }
            final ParseUser mCurrentFriend = mFriends.get(position);
            TextView mFriendTextView = (TextView) row.findViewById(R.id.friendTextView);
            String mCurrentFriendUsername = "";
            try {
                mCurrentFriendUsername = mCurrentFriend.fetchIfNeeded().getString("username");
            } catch (ParseException e) {
                Log.v("Friend name ParseExp", e.toString());
            }
            mFriendTextView.setText(mCurrentFriendUsername);
            Button mAddFriendButton = (Button) row.findViewById(R.id.slapBackButton);

            mAddFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ParseUser> mWhoSlappedList = (ArrayList) mCurrentFriend.get("whoslapped");
                    if (mWhoSlappedList == null) {
                        mWhoSlappedList = new ArrayList<>();
                    }
                    mWhoSlappedList.add(ParseUser.getCurrentUser());
                    mCurrentFriend.put("whoslapped", mWhoSlappedList);
                    mCurrentFriend.saveInBackground();
                    Toast.makeText(getContext(), getString(R.string.youhaveslapped) + getString(R.string.space) + mCurrentFriend.getUsername(), Toast.LENGTH_SHORT).show();
                }
            });

            return row;
        }
    }
}
