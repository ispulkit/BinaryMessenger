package binary.datahandlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pulkit.
 */
public class chatdatabasehandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messenger";

    public chatdatabasehandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE messages (_id INTEGER PRIMARY KEY autoincrement,_from text,_to text,message text,at datetime default current_timestamp);");
        db.execSQL("create table profile (_id integer primary key autoincrement,name text,email text unique,is_group integer default 0,_gcm_id text);");
        db.execSQL("create table banner (_id integer primary key autoincrement,_with text,count integer default 0,latest_at datetime default current_timestamp,name text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("drop table if exists profile");
        db.execSQL("drop table if exists banner");
        onCreate(db);

    }

}