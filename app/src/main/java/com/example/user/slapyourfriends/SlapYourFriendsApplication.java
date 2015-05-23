package com.example.user.slapyourfriends;

import android.app.Application;

import com.parse.Parse;

public class SlapYourFriendsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "oy6Nf7lc7vQFkxWVitIydk3ZOKt0MM2WvQ4QOLPG", "r6Ho2vnMTbSCSieMihaMiT7HFwvjrNB5EVZsgodK");
    }
}
