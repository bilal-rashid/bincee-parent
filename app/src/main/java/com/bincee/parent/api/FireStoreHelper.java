package com.bincee.parent.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FireStoreHelper {

    public static Task<QuerySnapshot> getToken(String parentId) {
        return tokenCollection(parentId).get();
    }

    public static CollectionReference tokenCollection(String userId) {
        return FirebaseFirestore.getInstance().collection("token")
                .document(userId)
                .collection("tokens");

    }

}
