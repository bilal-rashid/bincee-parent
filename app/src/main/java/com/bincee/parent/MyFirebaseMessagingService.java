package com.bincee.parent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "66";
    private String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived: ");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();


        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        String tag = remoteMessage.getNotification().getTag();
        String bodyLocalizationKey = remoteMessage.getNotification().getBodyLocalizationKey();
        String[] bodyLocalizationArgs = remoteMessage.getNotification().getBodyLocalizationArgs();

        Map<String, String> data = remoteMessage.getData();

        String type = "";
        String studentId = "-1";

        if (remoteMessage.getData().containsKey(Student.STUDENT_ID)) {
            studentId = (remoteMessage.getData().get(Student.STUDENT_ID));

        }

        if (remoteMessage.getData().containsKey(Student.NOTIFICATION_TYPE)) {
            type = (remoteMessage.getData().get(Student.NOTIFICATION_TYPE));
        }


        if (type.equalsIgnoreCase("2") || type.equalsIgnoreCase("1") || type.equalsIgnoreCase("3")) {
            //Atandnace or update status

            if (!studentId.equalsIgnoreCase(" -1")) {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Student.STUDENT_ID, studentId);
                intent.putExtra(Student.STUDENT_NOTIFICATION_MESSAGE, body);
                intent.putExtra(Student.NOTIFICATION_TYPE, type);
                startActivity(intent);

            }


        } else {


//            if (body.contains("Bus has Reached the school")) {
//
//
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(Student.STUDENT_ID, studentId);
//                intent.putExtra(Student.STUDENT_NOTIFICATION_MESSAGE, body);
//                intent.putExtra(Student.NOTIFICATION_TYPE, type);
//                startActivity(intent);
//
//            }


        }


    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        LoginResponse.User user = MyApp.instance.user.getValue();
        if (user != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", s);
            instance.collection("token").document(user.id + "")
                    .set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Token Updated");
                        }
                    });
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


/* this function was written to check user notification type and perform action accordingly

    func checkNotificationType(userInfo : [AnyHashable : Any])
    {
        let userInfo = userInfo
        print("user info here \(userInfo)")
        if let type = userInfo[gcmMessageType]
        {


            if(type as! String == "3")
            {
                //Ride
                print(type as! String)
                return
            }
            else if(type as! String == "2" || type as! String == "1")
            {
                //attendance // update status
                if let data = userInfo[gcmMessageData]
                {
                    var studentData = data as! String
//                    var studentId = studentData.value(forKey: "studentId") as! Int

                    studentData.removeFirst(13)
                    studentData.removeLast(1)
                    print("Student id sent \(studentData)")
                    CurrentKidDataFirebase.shared.notificationForKidId = Int(studentData)!
                        NotificationCenter.default.post(name: Notification.Name(rawValue: "launchTrackMyKid"), object: nil, userInfo: nil)
                }
            }
            else if(type as! String == "Evening1")
            {
                if let data = userInfo["studentId"]
                {
                    var studentData = data as! String
                    //                    var studentId = studentData.value(forKey: "studentId") as! Int
                    print("Student id sent \(studentData)")
                    CurrentKidDataFirebase.shared.notificationForKidId = Int(studentData)!
                        NotificationCenter.default.post(name: Notification.Name(rawValue: "EveningStatusReceived"), object: nil, userInfo: nil)
                }
            }

            else if(type as! String == "notification")
            {
                NotificationCenter.default.post(name: Notification.Name(rawValue: "AnnouncementReceived"), object: nil, userInfo: nil)
            }
            else if(type as! String == "alert")
            {
///Local Notification was issued

                NotificationCenter.default.post(name: Notification.Name(rawValue: "AlertReceived"), object: nil, userInfo: nil)
            }
            else
            {

            }

        }

//        call commented section only when user status is changed

    }
    */

}
