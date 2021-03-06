package edu.uco.retrocollect.retrocollect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abilb on 11/4/2016.
 */

public class SqlGameHelper extends SQLiteOpenHelper {

    //Static Database Constraints
    // Books table name
    private static final String TABLE_GAMES = "Games";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String GAME_RELEASE_YEAR = "game_release_year";
    private static final String GAME_RELEASE_DATE = "game_release_date";
    private static final String GAME_PUBLISHER = "game_publisher";
    private static final String GAME_STUDIO= "game_studio";
    private static final String GAME_RATING = "game_rating";
    private static final String GAME_USER_RATING = "game_user_rating"; //game_user_rating added - Nicholas Clemmons
    private static final String COVER_HASH = "cover_hash";
    private static final String GAME_VALUE = "game_value";
    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE, GAME_RELEASE_YEAR, GAME_RELEASE_DATE,
            GAME_PUBLISHER, GAME_STUDIO, GAME_RATING, GAME_USER_RATING, COVER_HASH, GAME_VALUE};//game_user_rating added - Nicholas Clemmons

    // Database Version

    private static final int DATABASE_VERSION = 9;

    // Database Name
    private static final String DATABASE_NAME = "GameDB";

    public SqlGameHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAME_TABLE =  "CREATE TABLE Games ( " +
                "id TEXT PRIMARY KEY, " +
                "title TEXT, "+
                "game_release_year TEXT," +
                "game_release_date TEXT," +
                "game_publisher    TEXT," +
                "game_studio       TEXT," +
                "game_rating       TEXT," +
                "game_user_rating  TEXT," +
                "cover_hash        TEXT," +
                "game_value        TEXT)";
        //game_user_rating added - Nicholas Clemmons
        //Create Database
        db.execSQL(CREATE_GAME_TABLE);
        Log.d("create table", db.toString() );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS Games");

        // create fresh books table
        this.onCreate(db);
    }
    public void addGame(Game game){
        //for logging
        Log.d("addGame", game.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, game.getGameId());
        values.put(KEY_TITLE, game.getTitle()); // get title
        Log.d("putTitle", game.getTitle());
        values.put(GAME_RELEASE_YEAR, game.getReleaseYear()); // get release year
        values.put(GAME_RELEASE_DATE, game.getReleaseDate()); // get release date
        values.put(GAME_PUBLISHER, game.getPublisher()); // get publisher
        values.put(GAME_STUDIO, game.getStudio()); // get publisher
        values.put(GAME_RATING, game.getRating()); // get publisher
        values.put(GAME_USER_RATING, Float.toString(game.getGameUserRating()));//getGameUserRating - Nicholas Clemmons
        values.put(COVER_HASH, game.getCoverHash()); //-HASEEB
        values.put(GAME_VALUE, game.getGameValue());
        Log.d("putHash", game.getCoverHash() + " ");
        // 3. insert
        db.insert(TABLE_GAMES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Game getGame(String title){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_GAMES, // a. table
                        COLUMNS, // b. column names
                        " title = ?", // c. selections
                        new String[] {title}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object

        String gameId = cursor.getString(0);
        String gameTitle = cursor.getString(1);
        double gameReleaseYear = Double.parseDouble(cursor.getString(2));
        String gameReleaseDate = cursor.getString(3);
        String gamePublisher = cursor.getString(4);
        String gameStudio = cursor.getString(5);
        double gameRating = Double.parseDouble(cursor.getString(6)); //
        float gameUserRating = Float.parseFloat(cursor.getString(7)); //gameUserRating added and values increased- Nicholas Clemmons
        String coverHash = cursor.getString(8); //
        String gameValue = cursor.getString(9);
        Game game = new Game(gameTitle, gameId, gameReleaseYear, gameReleaseDate, gamePublisher,
                gameStudio, gameRating, gameUserRating, coverHash);//gameUserRating and gameValue added and values increased- Nicholas Clemmons
        if(gameValue != null)
        {
            game.setGameValue(gameValue);
        }
        else
        {
            game.setGameValue("N/A");
        }
        //log
        Log.d("getGame("+title+")", game.toString());

        // 5. return book
        return game;
    }
    public Game getGame(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_GAMES, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object

        String gameId = cursor.getString(0);
        String gameTitle = cursor.getString(1);
        double gameReleaseYear = Double.parseDouble(cursor.getString(2));
        String gameReleaseDate = cursor.getString(3);
        String gamePublisher = cursor.getString(4);
        String gameStudio = cursor.getString(5);
        double gameRating = Double.parseDouble(cursor.getString(6));
        float gameUserRating = Float.parseFloat(cursor.getString(7)); //gameUserRating added and values increased- Nicholas Clemmons
        String coverHash = cursor.getString(8);
        String gameValue = cursor.getString(9);
        Game game = new Game(gameTitle, gameId, gameReleaseYear, gameReleaseDate, gamePublisher,
                gameStudio, gameRating, gameUserRating, coverHash);//gameUserRating and gameValue added and values increased- Nicholas Clemmons
        if(gameValue != null)
        {
            game.setGameValue(gameValue);
        }
        else
        {
            game.setGameValue("N/A");
        }

        //log
        Log.d("getGame("+id+")", game.toString());
         // 5. return book
        return game;
    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_GAMES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Game game = null;
        if (cursor.moveToFirst()) {
            do {
                String gameId = cursor.getString(0);
                Log.d("IDTest", gameId+" ");
                String gameTitle = cursor.getString(1);
                double gameReleaseYear = Double.parseDouble(cursor.getString(2));
                String gameReleaseDate = cursor.getString(3);
                String gamePublisher = cursor.getString(4);
                String gameStudio = cursor.getString(5);
                double gameRating = Double.parseDouble(cursor.getString(6));
                float gameUserRating = Float.parseFloat(cursor.getString(7)); //gameUserRating added and values increased- Nicholas Clemmons
                String coverHash = cursor.getString(8);
                String gameValue = cursor.getString(9);
                game = new Game(gameTitle, gameId, gameReleaseYear, gameReleaseDate, gamePublisher,
                        gameStudio, gameRating, gameUserRating, coverHash);//gameUserRating and gameValue added and values increased- Nicholas Clemmons
                Log.d("titlesql", gameTitle +" ");
                Log.d("gameIdsql", gameId+ " ");
                Log.d("coverHashSql", coverHash + " ");
                if(gameValue != null)
                {
                    game.setGameValue(gameValue);
                }
                else
                {
                    game.setGameValue("N/A");
                }
                games.add(game);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", games.toString());

        return games;
    }

    public int updateGame(Game game) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, game.getTitle()); // get title
        values.put(GAME_RELEASE_YEAR, game.getReleaseYear()); // get release year
        values.put(GAME_RELEASE_DATE, game.getReleaseDate()); // get release date
        values.put(GAME_PUBLISHER, game.getPublisher()); // get publisher
        values.put(GAME_STUDIO, game.getStudio()); // get publisher
        values.put(GAME_RATING, game.getRating()); // get publisher
        values.put(GAME_USER_RATING, Float.toString(game.getGameUserRating()));
        values.put(COVER_HASH, game.getCoverHash()); //-HASEEB
        values.put(GAME_VALUE, game.getGameValue());
        // 3. updating row
        int i = db.update(TABLE_GAMES, //table
                values, // column/value
                KEY_TITLE+" = ?", // selections
                new String[] { game.getTitle()}); //selection args

        // 4. close

        Log.d("intoCo", "we made it dawg");

        db.close();

        return i;

    }
    public void deleteGame(Game game) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_GAMES, //table name
                KEY_TITLE +" = ?",  // selections
                new String[] { game.getTitle() });


        // 3. close
        db.close();

        //String id111 = game.getGameId() +" ";

        //log
        // Log.d("deleteBook", id111);

    }
/*
    public boolean deleteGameById(String id) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("deleteBook11", id);
        // 2. delete
        return db.delete(TABLE_GAMES, //table name
                KEY_ID+"=" + id,  // selections
                null) > 0; //selections args



        //log
        //Log.d("deleteBook11", id);


    }
*/

}
