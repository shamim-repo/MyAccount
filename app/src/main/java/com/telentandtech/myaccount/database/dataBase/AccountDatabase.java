package com.telentandtech.myaccount.database.dataBase;


import static com.telentandtech.myaccount.core.DataClass.DATABASE_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.telentandtech.myaccount.database.convertors.Converter;
import com.telentandtech.myaccount.database.dao.FeesDao;
import com.telentandtech.myaccount.database.dao.PaymentDao;
import com.telentandtech.myaccount.database.dao.StudentsDao;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.core.HashingHelper;
import com.telentandtech.myaccount.database.dao.AttendanceDao;
import com.telentandtech.myaccount.database.dao.ClassDao;
import com.telentandtech.myaccount.database.dao.GroupDao;
import com.telentandtech.myaccount.database.dao.UserDao;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.Payments;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;

@Database(entities = {Attendance.class, Classe.class, Fees.class, Group.class,
        Payments.class, Students.class, User.class}, version = 1,exportSchema = true)
@TypeConverters({Converter.class})
public abstract class AccountDatabase extends RoomDatabase {
    private static AccountDatabase databaseInstance; //only one interface

    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
    public abstract ClassDao classDao();
    public abstract FeesDao feesDao();
    public abstract GroupDao groupDao();
    public abstract PaymentDao paymentsDao();
    public abstract StudentsDao studentsDao();


    public static synchronized AccountDatabase getInstance(Context context){
        if(databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AccountDatabase.class , DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                try {
                                    getInstance(context).userDao().insertUser(new User(
                                            new HashingHelper().hashString("admin@gmail.com"),"admin",
                                            "admin@gmail.com","adminpassword"));
                                    getInstance(context).userDao().insertUser(new User(
                                            new HashingHelper().hashString("example@gmail.com"),"example",
                                            "example@gmail.com","12345678"));
                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    })
                    .build();
        }
        return databaseInstance;
    }
}
