package com.example.chatterly.webreset;

import com.example.chatterly.utils.Config;
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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.api)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthenticationAPI authenticationAPI = retrofit.create(AuthenticationAPI.class);

        ResetPasswordTnModel resetPasswordTnModel = new ResetPasswordTnModel(
                request.getParameter("email"),
                request.getParameter("token"),
                request.getParameter("newPassword")
        );

        final String[] apiResponse = new String[1];

        authenticationAPI.resetPasswordTnRequest(resetPasswordTnModel).enqueue(new Callback<ResetPasswordTnModel>() {
            @Override
            public void onResponse(Call<ResetPasswordTnModel> call, Response<ResetPasswordTnModel> response) {
                apiResponse[0] = response.toString();
            }

            @Override
            public void onFailure(Call<ResetPasswordTnModel> call, Throwable t) {
                apiResponse[0] = response.toString();
            }
        });

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>API Response:</h1>");
        out.println("<p>" + apiResponse[0] + "</p>");
    }
}