package com.lostbug.service.hello;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class HelloServlet extends HttpServlet {
    private static final String DEFAULT_USER = "Guest";

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + getServletName() + " has started!");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + getServletName() + " has stopped!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map headMap = request.getParameterMap();
        System.out.println("requestMap：" + headMap.toString());
        response.getWriter().println("Hello,World!");
        String user = request.getParameter("user");
        if (user == null) {
            user = DEFAULT_USER;
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("   <head>\r\n")
                .append("       <title>Hello User Application</title>\r\n")
                .append("   </head>\r\n")
                .append("   <body>\r\n")
                .append("       Hello,").append(user).append("!<br/><br/>\r\n")
                .append("       <form action=\"greeting\" method=\"POST\">\r\n")
                .append("       Enter your name:<br/>\r\n")
                .append("           <input type=\"text\" name=\"user\" /><br/>\r\n")
                .append("           <input type=\"submit\" value=\"Submit\"/>\r\n")
                .append("       </form>\r\n")
                .append("   </body>\r\n")
                .append("</html>\r\n");
    }
}
