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

public class WhoSlappedFragment extends Fragment {
    public WhoSlappedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_who_slapped, container, false);
        final ListView mWhoSlappedListView = (ListView) v.findViewById(R.id.whoSlappedListView);
        TextView mWhoSlappedHeadingTextView = (TextView) v.findViewById(R.id.whoSlappedHeadingTextView);

        ArrayList mWhoSlappedList = (ArrayList) ParseUser.getCurrentUser().get("whoslapped");
        if (mWhoSlappedList == null) {
            mWhoSlappedHeadingTextView.setText(getString(R.string.nobodyslappedyou));
        }
        else if (mWhoSlappedList.size() == 0) {
            mWhoSlappedHeadingTextView.setText(getString(R.string.nobodyslappedyou));
        }
        else {
            ArrayAdapter<ParseUser> adapter = new SlapFriendsAdapter(getActivity(), R.layout.list_item_who_slapped, mWhoSlappedList);
            mWhoSlappedListView.setAdapter(adapter);
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "who slapped fragment");
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
        private ArrayList<ParseUser> mWhoSlapped;

        public SlapFriendsAdapter (Context context, int resource, ArrayList<ParseUser> whoslapped) {
            super(context, resource, whoslapped);
            mResource = resource;
            mWhoSlapped = whoslapped;
        }
        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(mResource, parent, false);
            }
            final ParseUser mCurrentWhoSlapped = mWhoSlapped.get(position);
            TextView mWhoSlappedTextView = (TextView) row.findViewById(R.id.whoSlappedTextView);
            String mCurrentWhoSlappedUsername = "";
            try {
                mCurrentWhoSlappedUsername = mCurrentWhoSlapped.fetchIfNeeded().getString("username");
            } catch (ParseException e) {
                Log.v("Friend name ParseExp", e.toString());
            }
            mWhoSlappedTextView.setText(mCurrentWhoSlappedUsername);
            Button mSlapBackButton = (Button) row.findViewById(R.id.slapBackButton);

            mSlapBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ParseUser> mWhoSlappedList = (ArrayList) mCurrentWhoSlapped.get("whoslapped");
                    if (mWhoSlappedList == null) {
                        mWhoSlappedList = new ArrayList<>();
                    }
                    mCurrentWhoSlapped.put("whoslapped", mWhoSlappedList);
                    mCurrentWhoSlapped.saveInBackground();
                    Toast.makeText(getContext(), getString(R.string.youhaveslappedback) + getString(R.string.space) + mCurrentWhoSlapped.getUsername(), Toast.LENGTH_SHORT).show();
                }
            });

            return row;
        }
    }
}
