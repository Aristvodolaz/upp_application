package com.example.timetrekerforandroid.network;

public class Const {
    public static final String URL_DOWNLOAD = "http://31.129.100.172:3005";
//    public static final String URL = "https://corrywilliams.ru";
//public static final String URL = "https://corrywilliams.ru";
    public static final String URL = "https://31.128.44.48";


    public static final String HOST = "31.128.44.48";
    public static final int PORT = 22;
    public static final String USERNAME ="root";
    public static final String PASSWORD ="Arishka_2002!";

    public static final String GET_NAME_FOR_WAIT = "/download/list-files";
    public static final String DOWNLOAD_FROM_SERVER_EXCEL = "/download/process-excel";
    public static final String GET_NAME_FOR_WORK = "/market/tasks/names";
    public static final String TASKS_WORK = "/market/tasks"; //todo работает только для загруженных в бд данных
    public static final String SEARCH_SHK = "/market/tasks/searchShk";
    public static final String SEARCH_ARTIKUL_TASKS = "/market/tasks/searchArticulTask";
    public static final String UPDATE_STATUS = "/market/tasks/updateStatus";
    public static final String END_STATUS = "market/tasks/endStatus";
    public static final String SEND_SROK = "/srok";

    //todo обновить значение для заданий
    public static final String UPDATE_CHECK_BOX = "/market/tasks/updateTasks";

    //todo для закрытия работы с артикулом
    public static final String FINISHED_REQUEST = "/send/update";

    public static final String SEARCH_IN_DB_FOR_ARTICLE_OR_SHK = "/article"; //todo перепроверить в бэке как это выглядит

    public static final String AUTH = "/auth";
    public static final String UPDATE_SHK = "/market/tasks/recordNewShk";
    public static final String DUPLICATE = "/market/tasks/duplicate";

}
