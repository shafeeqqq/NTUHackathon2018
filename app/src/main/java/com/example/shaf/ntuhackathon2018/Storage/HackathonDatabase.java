package com.example.shaf.ntuhackathon2018.Storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ExtractedText.class}, version = 1)
public abstract class HackathonDatabase extends RoomDatabase {

    public abstract ExtractedTextDao extractedTextDao();

    private static HackathonDatabase INSTANCE;
//

    static HackathonDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (HackathonDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HackathonDatabase.class, "hackathon_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
