package com.example.myapplication;

public class FirebaseServices {
    private static FirebaseServices instance;
    private final FirebaseServices auth;
    private final FirebaseServices db;
    private final FirebaseServices storage;


    private FirebaseServices(){
        auth =  FirebaseServices.GetInstance;
        db =  FirebaseServices.GetInstance;
        storage =  FirebaseServices.GetInstance;

    }

    public static FirebaseServices getInstance() {
        if(instance==null){
            instance = new FirebaseServices();
        }
        return instance;
    }

    public FirebaseServices getAuth() {
        return auth;
    }

    public FirebaseServices getDb() {
        return db;
    }

    public FirebaseServices getStorage() {
        return storage;
    }
}
