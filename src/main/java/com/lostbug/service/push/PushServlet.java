package com.lostbug.service.push;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PushServlet extends HttpServlet {
    String appKey =
            "5a3db57fb27b0a4dda0006e2";
    String secret = "3b8fbfe544fae40a6292753373ebfd35";
    String deviceToken = "AjG8IMivFGXhxA1JSoqfoHcI_cr486pumlPzyibOCWG4";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("push success");
        Demo demo = new Demo(appKey, secret);
        try {
            demo.sendAndroidUnicast(deviceToken);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
