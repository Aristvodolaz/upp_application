package com.example.timetrekerforandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.timetrekerforandroid.ApplicationLoader;


public class SPHelper {
    public static final  String FILE_NAME = "market_app";
    public static final String ACTIVE_USER = "active_user";
    public static final String TYPE_MOBILE_DEVICE = "type_device";
    public static final String NAME_TASK = "name_task";
    public static final String NAME_EMPLOYER  = "name_employer";
    public static final String ARTICULE_WORK = "articule_work";
    public static final String SHK_WORK = "shk_work";
    public static final String NAME_STUFF_WORK = "name_stuff_work";
    public static final String PREFICS = "prefice";
    public static final String SROK_GODNOSTI = "sroke_godnosti";
    public static final String ITOG_ZAKAZA = "itog_zakaza";
    public static final String SYRYO = "syryo";

    public static final String ARTICULS_SYRYO = "articuls_syryo";
    public static final String SIZE_SYRYO = "size_syryo";
    public static final String NAME_TOAVARA = "name_tovara";
    public static final String SIZE_TOVARA = "size_tovara";
    public static final String SHK_PALLETA = "shk_palleta";

    public static final String SKLAD = "sklad";



    private static SharedPreferences getPrefs(){
        return ApplicationLoader.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEdit(){return getPrefs().edit();}

    public static void setTypeMobileDevice(boolean mobile){
        getEdit().putBoolean(TYPE_MOBILE_DEVICE, mobile).commit();
    }

    public static boolean getTypeMobileDevice(){
        return getPrefs().getBoolean(TYPE_MOBILE_DEVICE, false);
    }

    public static void setNameTask(String name){
        getEdit().putString(NAME_TASK, name).commit();
    }

    public static String getNameTask(){
        return getPrefs().getString(NAME_TASK, "");
    }

    public static void setSklad(String name){
        getEdit().putString(SKLAD, name).commit();
    }

    public static String getSklad(){
        return getPrefs().getString(SKLAD, "");
    }

    public static void setNameEmployer(String name){
        getEdit().putString(NAME_EMPLOYER, name).commit();
    }
    public static String getNameEmployer(){
        return  getPrefs().getString(NAME_EMPLOYER, "");
    }
    public static void setArticuleWork(String articule){
        getEdit().putString(ARTICULE_WORK, articule).commit();
    }
    public static String getArticuleWork(){
        return  getPrefs().getString(ARTICULE_WORK, "");
    }

    public static void setShkWork(String shk){
        getEdit().putString(SHK_WORK, shk).commit();
    }
    public static String getShkWork(){return  getPrefs().getString(SHK_WORK, "");}

    public static void setShkPalleta(String shk){
        getEdit().putString(SHK_PALLETA, shk).commit();
    }
    public static String getShkPalleta(){return  getPrefs().getString(SHK_PALLETA, "");}

    public static void setNameStuffWork(String name){getEdit().putString(NAME_STUFF_WORK, name).commit();}
    public static String getNameStuffWork(){return getPrefs().getString(NAME_STUFF_WORK, "");}
    public static void setSrokGodnosti(boolean sroke){
        getEdit().putBoolean(SROK_GODNOSTI, sroke).commit();
    }

    public static boolean getSrokGodnosti(){return getPrefs().getBoolean(SROK_GODNOSTI, false);}

    public static void setPrefics(String pref){
        getEdit().putString(PREFICS, pref).commit();
    }

    public static String getPrefics(){ return getPrefs().getString(PREFICS, "");}

    public static void setItogZakaza(int itog){
        getEdit().putInt(ITOG_ZAKAZA, itog).commit();
    }

    public static int getItogZakaza(){return  getPrefs().getInt(ITOG_ZAKAZA, 0);}

    public static void setSyryo(boolean i){getEdit().putBoolean(SYRYO, i).commit();}
    public static boolean getSyryo(){return  getPrefs().getBoolean(SYRYO, false);}


    public static void setSizeSyryo(String mesto){
        getEdit().putString(SIZE_SYRYO, mesto).commit();
    }
    public static String getSizeSyryo(){return  getPrefs().getString(SIZE_SYRYO, "");}

    public static void setArticulsSyryo(String shk){getEdit().putString(ARTICULS_SYRYO, shk).commit();}
    public static String getArticulsSyryo(){return  getPrefs().getString(ARTICULS_SYRYO, "");}
    public static void setNameToavara(String shk){getEdit().putString(NAME_TOAVARA, shk).commit();}
    public static String getNameToavara(){return  getPrefs().getString(NAME_TOAVARA, "");}

    public static int getSizeTovara(){return  getPrefs().getInt(SIZE_TOVARA, 0);}

    public static void setSizeTovara(int shk){getEdit().putInt(SIZE_TOVARA, shk).commit();}

}
