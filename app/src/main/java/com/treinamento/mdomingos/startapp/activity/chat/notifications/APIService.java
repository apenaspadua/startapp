package com.treinamento.mdomingos.startapp.activity.chat.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZMEue8k:APA91bE39VX2T2K6klPS96b8NILHO4UW1cLRjx4Rvx918QjufVQeCtjAyMPUnf6bWHzUMWfQlcT_2y0Pff2ptbys1AkX3Q525vmU3_71eMsMvqrdMEY_N0anXf1rSBS1wm0tpcotzQgN"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}