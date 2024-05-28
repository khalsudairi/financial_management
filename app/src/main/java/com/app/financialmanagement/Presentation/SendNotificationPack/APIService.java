package com.app.financialmanagement.Presentation.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                     "Content-Type:application/json",
                    "Authorization:key=AAAAVb_rVic:APA91bGnIKkGaR1PhxxaeXM5YOyX7cF87ogNNn6UH_cp6S5O174ufVQeyCXRs6VfyOBsXbRAbXnzV57qoQuRwCnv2W-wqcCvAwlrpYizc0KTO0FQBAcceI4CR3Yvo3IOVs5lMw-9ylTI" // Your server key refer to video for finding your server key
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

