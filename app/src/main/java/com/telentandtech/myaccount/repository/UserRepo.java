package com.telentandtech.myaccount.repository;



import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.core.HashingHelper;
import com.telentandtech.myaccount.database.dao.UserDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.UserListResult;
import com.telentandtech.myaccount.database.resultObjects.UserResult;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserRepo {
    private UserDao userDao;
    private MutableLiveData<UserResult> userLiveData;
    private MutableLiveData<UserListResult> userListLiveData;

    private AccountDatabase db;

    public UserRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        userDao = db.userDao();
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();

    }
    public LiveData<UserResult> getUserLiveData() {
        return userLiveData;
    }
    public LiveData<UserListResult> getUserListLiveData() {
        return userListLiveData;
    }
    public void getUserByUid(String uid){
        new GetUserAsyncTask(userDao).execute(uid);
    }
    public void insertUser(String fullName, String email, String password) {
        new InsertUserAsyncTask(userDao).execute(fullName, email, password);
    }
    public void authenticateUser(String email, String password){
        new AuthenticateUserAsyncTask(userDao).execute(email,password);
    }
    public void getUserList(){
        new GetUserListAsyncTask(userDao).execute();
    }

    public void updateProfile(User user,String new_full_name, String new_password) {
        new UpdateProfileAsyncTask(userDao).execute(user,new_full_name,new_password);
    }
    public LiveData<UserResult> getUserInsertLiveData() {
        return userLiveData;
    }
    private class GetUserAsyncTask extends AsyncTask<String,Void,UserResult>{
            private UserDao userDao;

        public GetUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserResult doInBackground(String... strings) {
            User user = userDao.getUserByUid(strings[0]);

            if(user != null){
                userLiveData.postValue(new UserResult(user,true,"User found"));
                return new UserResult(user,true,"User found");
            }else{
                return new UserResult(null,false,"User not found");
            }
        }

        @Override
        protected void onPostExecute(UserResult userResult) {
            super.onPostExecute(userResult);
            userLiveData.postValue(userResult);
        }
    }

    private class AuthenticateUserAsyncTask extends AsyncTask<String,Void,UserResult> {
        private UserDao userDao;

        public AuthenticateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserResult doInBackground(String... strings) {
            User user = userDao.checkEmail(strings[0]);
            if (user != null) {
                if (user.getPassword().equals(strings[1])) {
                    return new UserResult(user, true, "Login successful");
                } else {
                    return new UserResult(null, false, "Password is incorrect");
                }

            } else {
                return new UserResult(null, false, "User doesn't exist");
            }

        }

        @Override
        protected void onPostExecute(UserResult userResult) {
            super.onPostExecute(userResult);
            userLiveData.postValue(userResult);
        }
    }

    private class InsertUserAsyncTask extends AsyncTask<String, Void, UserResult> {
        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserResult doInBackground(String... strings) {
            User user = userDao.checkEmail(strings[1]);
            if (user == null) {
                try {
                    user = new User(HashingHelper.hashString(strings[1]),strings[0], strings[1], strings[2]);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                userDao.insertUser(user);
                    return new UserResult(user, true, "User registered successfully");
                }else {
                return new UserResult(null, false, "User already exists");
            }
        }

        @Override
        protected void onPostExecute(UserResult userResult) {
            super.onPostExecute(userResult);
            userLiveData.postValue(userResult);
        }
    }

    private class GetUserListAsyncTask extends AsyncTask<Void,Void,UserListResult>{
        private UserDao userDao;

        public GetUserListAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserListResult doInBackground(Void... voids) {
            List<User> userList = userDao.getUsers();
            if (userList!=null){
                return new UserListResult(userList,true,"User list retrieved successfully");
            }
            else {
                return new UserListResult(null,false,"User list is empty");
            }
        }

        @Override
        protected void onPostExecute(UserListResult userListResult) {
            super.onPostExecute(userListResult);
            userListLiveData.postValue(userListResult);
        }
    }

    public void updateUser(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    private class UpdateUserAsyncTask extends AsyncTask<User, Void, UserResult> {
        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserResult doInBackground(User... users) {
            User user = users[0];
            userDao.updateUser(user);
            return new UserResult(user, true, "User updated successfully");
        }

        @Override
        protected void onCancelled(UserResult userResult) {
            super.onCancelled(userResult);
            userLiveData.postValue(new UserResult(null, false, "User update cancelled"));
        }

        @Override
        protected void onPostExecute(UserResult userResult) {
            super.onPostExecute(userResult);
            userLiveData.postValue(userResult);
        }
    }


    private class UpdateProfileAsyncTask extends AsyncTask<Object, Void, UserResult> {
        private UserDao userDao;

        public UpdateProfileAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserResult doInBackground(Object... objects) {
            User user = (User) objects[0];
            String new_full_name = (String) objects[1];
            String new_password = (String) objects[2];
            userDao.updateProfile(user.getUid(),user.getEmail(),user.getPassword(),
                    new_full_name,new_password);

            return new UserResult(user, true, "Profile updated successfully");
        }

        @Override
        protected void onCancelled(UserResult userResult) {
            super.onCancelled(userResult);
            userLiveData.postValue(new UserResult(null, false, "Profile update cancelled"));
        }

        @Override
        protected void onPostExecute(UserResult userResult) {
            super.onPostExecute(userResult);
            userLiveData.postValue(userResult);
        }
    }
}
