package com.lostbug.service.push;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class PushServlet extends HttpServlet {
    String appKey =
            "559dd7ea67e58e790d00625c";
    String secret = "hockmr4ujwnmxhbmjebyv4iwtjfa3u3r";
    String deviceToken = "18d17c949c0e631b4ab83ade10b618ac463f3e16b08f4d3a5875ad11c2a1e98b";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("push success");
        Demo demo = new Demo(appKey, secret);
        try {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.append("你好");
//            demo.sendAndroidUnicast(deviceToken);
            demo.sendIOSUnicast(deviceToken);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
