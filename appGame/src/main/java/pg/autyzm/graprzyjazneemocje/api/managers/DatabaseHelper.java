package pg.autyzm.graprzyjazneemocje.api.managers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "friendlyAnimations.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 18;

    // the DAO object we use to access the SimpleData table
    private Dao<Picture, Integer> pictureDao = null;
    private RuntimeExceptionDao<Picture, Integer> pictureRuntimeDao = null;

    private Dao<PicturesContainer, Integer> pictureContainerDao = null;
    private RuntimeExceptionDao<PicturesContainer, Integer> pictureContainerRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, PicturesContainer.class);
            TableUtils.createTable(connectionSource, Picture.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Picture.class, true);
            TableUtils.dropTable(connectionSource, PicturesContainer.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Picture, Integer> getPictureDao() throws SQLException {
        if (pictureDao == null) {
            pictureDao = getDao(Picture.class);
        }
        return pictureDao;
    }

    public RuntimeExceptionDao<Picture, Integer> getPictureRuntimeDao() {
        if (pictureRuntimeDao == null) {
            pictureRuntimeDao = getRuntimeExceptionDao(Picture.class);
        }
        return pictureRuntimeDao;
    }


    public Dao<PicturesContainer, Integer> getPictureContainerDao() throws SQLException {
        if (pictureContainerDao == null) {
            pictureContainerDao = getDao(PicturesContainer.class);
        }
        return pictureContainerDao;
    }

    public RuntimeExceptionDao<PicturesContainer, Integer> getPictureContainerRuntimeDao() {
        if (pictureContainerRuntimeDao == null) {
            pictureContainerRuntimeDao = getRuntimeExceptionDao(PicturesContainer.class);
        }
        return pictureContainerRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        pictureDao = null;
        pictureRuntimeDao = null;
        pictureContainerDao = null;
        pictureContainerRuntimeDao = null;
    }
}