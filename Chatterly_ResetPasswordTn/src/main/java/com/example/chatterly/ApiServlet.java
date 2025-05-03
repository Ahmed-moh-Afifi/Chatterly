package com.example.chatterly;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ApiServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.3:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthenticationAPI authenticationAPI = retrofit.create(AuthenticationAPI.class);

        ResetPasswordTnModel resetPasswordTnModel = new ResetPasswordTnModel(
                request.getParameter("email"),
                request.getParameter("token"),
                request.getParameter("newPassword")
        );

        out.println(resetPasswordTnModel);

        out.println("<h1>API Response:</h1>");

        authenticationAPI.resetPasswordTnRequest(resetPasswordTnModel).enqueue(new Callback<ResetPasswordTnModel>() {
            @Override
            public void onResponse(Call<ResetPasswordTnModel> call, Response<ResetPasswordTnModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    out.println(" " + response.code()+", "+response.message() + ", " + response.body());
                } else {
                    out.println(" " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordTnModel> call, Throwable t) {
                out.println(t);
            }
        });
        out.println("Here");
        out.close();
    }
}