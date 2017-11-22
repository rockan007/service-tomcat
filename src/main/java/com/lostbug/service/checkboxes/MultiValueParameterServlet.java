package com.lostbug.service.checkboxes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@WebServlet(name = "MultiValueParameterServlet",
        urlPatterns = {"/checkboxes"})
public class MultiValueParameterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] fruits = request.getParameterValues("fruit");
        System.out.println("requestUrl:"+request.getRequestURL());
        System.out.println("fruits:"+ Arrays.toString(fruits));
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("   <head>\r\n")
                .append("       <title>checkboxes</title>\r\n")
                .append("   </head>\r\n")
                .append("   <body>\r\n")
                .append("   <h2>Your Selections</h2>\r\n");
        if (fruits == null) {
            writer.append("     You did not select any fruits");
        } else {
            writer.append("     <ul>\r\n");
            for (String fruit : fruits) {
                writer.append("         <li>").append(fruit).append("</li>\r\n");
            }
            writer.append("     </ul>\r\n");
        }
        writer.append("     </body>\r\n")
                .append("</html>\r\n");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("   <head>\r\n")
                .append("       <title>multi-values</title>\r\n")
                .append("   </head>\r\n")
                .append("   <body>\r\n")
                .append("       <form action=\"checkboxes\" method=\"POST\">\r\n")
                .append("Select the fruits you like to eat:<br/>\r\n")
                .append("           <input type=\"checkbox\" name=\"fruit\" value=\"Banana\"/>Banana<br/>\r\n")
                .append("           <input type=\"checkbox\" name=\"fruit\" value=\"Apple\"/>Apple<br/>\r\n")
                .append("           <input type=\"checkbox\" name=\"fruit\" value=\"Orange\"/>Orange<br/>\r\n")
                .append("           <input type=\"checkbox\" name=\"fruit\" value=\"Guava\"/>Guava<br/>\r\n")
                .append("           <input type=\"checkbox\" name=\"fruit\" value=\"Kiwi\"/>Kiwi<br/>\r\n")
                .append("           <input type=\"submit\" value=\"Submit\">\r\n")
                .append("       </form>\r\n")
                .append("   </body>\r\n")
                .append("</html>");

    }
}
