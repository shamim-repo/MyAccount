package com.telentandtech.myaccount.core;

import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;

import java.util.List;

public class DataClass {
    public final static String UID = "UID";
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_NAME = "USER_NAME";
    public final static String LOGOUT_="LOGOUT_";

    public final static String AUTHENTICATION_STATUS = "AUTHENTICATION_STATUS";
    public final static String DATABASE_NAME = "may_account";
    public final static int DATABASE_VERSION = 1;
    public final static String LAST_BACKUP_DATABASE_NAME = "LAST_BACKUP_DATABASE_NAME";

    public final static String ADMIN_EMAIL = "admin@gmail.com";


    public static String[] classListToIdNameStringList(List<Classe> classList) {
        String[] classIdNameList = new String[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            classIdNameList[i] =classList.get(i).getClass_id() + ": " +
                    classList.get(i).getClass_name();
        }
        return classIdNameList;
    }

    public static String[] groupListToIdNameStringList(List<Group> groupList) {
        String[] groupIdNameList = new String[groupList.size()];
        for (int i = 0; i < groupList.size(); i++) {
            groupIdNameList[i] =groupList.get(i).getGroup_id() + " :" +
                    groupList.get(i).getGroup_name();
        }
        return groupIdNameList;
    }

    public static String[] classNameIDListToStringArray(List<ClassNameId> classNameIDList) {
        String[] classNameIDArray = new String[classNameIDList.size()];
        for (int i = 0; i < classNameIDList.size(); i++) {
            classNameIDArray[i] =classNameIDList.get(i).getClass_id()+
                    ": "+classNameIDList.get(i).getClass_name();
        }
        return classNameIDArray;
    }

    public static String[] groupNameIDListTOStringArray(List<GroupNameID> groupNameIDList) {
        String[] groupNameIDArray = new String[groupNameIDList.size()];
        for (int i = 0; i < groupNameIDList.size(); i++) {
            groupNameIDArray[i] =groupNameIDList.get(i).getGroup_id()+
                    ": "+groupNameIDList.get(i).getGroup_name();
        }
        return groupNameIDArray;
    }

}
