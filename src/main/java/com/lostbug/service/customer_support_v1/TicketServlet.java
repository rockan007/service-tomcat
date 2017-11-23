package com.lostbug.service.customer_support_v1;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "TicketServlet",
        urlPatterns = {"/tickets"},
        loadOnStartup = 1)
@MultipartConfig(//Web容器为该Servlet提供文件上传
//        location = 存储临时文件地址
        fileSizeThreshold = 5_242_880,//小于5M保存于内存中，大于5M写入临时文件中
        maxFileSize = 20_971_520L,//禁止上传大于20M的文件
        maxRequestSize = 41_943_040L//禁止请求大于40M的文件
)
public class TicketServlet extends HttpServlet {
    private volatile int TICKET_ID_SEQUENCE = 1;
    private Map<Integer, Ticket> ticketDatabase = new LinkedHashMap<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "create":
                this.createTicket(request, response);
                break;
            case "download":
            default:
                response.sendRedirect("tickets");
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";//默认显示列表
        }
        switch (action) {
            case "create"://创建
                this.showTicketForm(response);
                break;
            case "view"://显示
                this.viewTicket(request, response);
                break;
            case "download"://下载
                this.downloadAttachment(request, response);
                break;
            default://显示列表
                this.listTickets(response);
                break;
        }
    }

    /**
     * 显示tickets
     *
     * @param response HttpServletResponse
     * @throws IOException      IO
     * @throws ServletException ServletException
     */
    private void listTickets(HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = this.writeHeader(response);

        writer.append("<h2>Tickets</h2>\r\n");
        writer.append("<a href=\"tickets?action=create\">Create Ticket")//创建
                .append("</a><br/><br/>\r\n");

        if (this.ticketDatabase.size() == 0) {//如果没有
            writer.append("<i>There are no tickets in the system.</i>\r\n");
        } else {
            for (int id : this.ticketDatabase.keySet()) {
                String idString = Integer.toString(id);
                Ticket ticket = this.ticketDatabase.get(id);
                writer.append("Ticket #").append(idString)
                        .append(": <a href=\"tickets?action=view&ticketId=")
                        .append(idString).append("\">").append(ticket.getSubject())
                        .append("</a> (customer: ").append(ticket.getCustomerName())
                        .append(")<br/>\r\n");
            }
        }

        this.writeFooter(writer);
    }

    /**
     * 下载附件
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException      IO
     * @throws ServletException ServletException
     */
    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idString = request.getParameter("ticketId");//获取ticketId
        Ticket ticket = this.getTicket(idString, response);//获取ticket
        if (ticket == null)
            return;

        String name = request.getParameter("attachment");//获取附件名称
        if (name == null) {//附件名为null
            response.sendRedirect("tickets?action=view&ticketId=" + idString);//返回viewTicket界面
            return;
        }

        Attachment attachment = ticket.getAttachment(name);//获取附件
        if (attachment == null) {//附件为null 返回viewTicket界面
            response.sendRedirect("tickets?action=view&ticketId=" + idString);
            return;
        }

        response.setHeader("Content-Disposition",//设置Header
                "attachment; filename=" + attachment.getName());
        response.setContentType("application/octet-stream");//设置类型

        ServletOutputStream stream = response.getOutputStream();
        stream.write(attachment.getContents());//写入内容
    }

    /**
     * 展示Ticket
     *
     * @param request  HttpServletRequest请求
     * @param response HttpServletResponse 响应
     * @throws IOException      IO
     * @throws ServletException ServletException
     */
    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idString = request.getParameter("ticketId");
        Ticket ticket = this.getTicket(idString, response);
        if (ticket == null)//我来组成头部
            return;

        PrintWriter writer = this.writeHeader(response);

        writer.append("<h2>Ticket #").append(idString)
                .append(": ").append(ticket.getSubject()).append("</h2>\r\n");
        writer.append("<i>Customer Name - ").append(ticket.getCustomerName())
                .append("</i><br/><br/>\r\n");
        writer.append(ticket.getBody()).append("<br/><br/>\r\n");

        if (ticket.getNumberOfAttachments() > 0) {
            writer.append("Attachments: ");
            int i = 0;
            for (Attachment attachment : ticket.getAttachments()) {
                if (i++ > 0)
                    writer.append(", ");
                writer.append("<a href=\"tickets?action=download&ticketId=")
                        .append(idString).append("&attachment=")
                        .append(attachment.getName()).append("\">")
                        .append(attachment.getName()).append("</a>");
            }
            writer.append("<br/><br/>\r\n");
        }

        writer.append("<a href=\"tickets\">Return to list tickets</a>\r\n");

        this.writeFooter(writer);
    }

    /**
     * 创建Ticket界面
     *
     * @param response HttpServletResponse
     * @throws IOException      IO
     * @throws ServletException ServletException
     */
    private void showTicketForm(HttpServletResponse response) throws IOException, ServletException {
        PrintWriter writer = this.writeHeader(response);

        writer.append("<h2>Create a Ticket</h2>\r\n");
        writer.append("<form method=\"POST\" action=\"tickets\" ")
                .append("enctype=\"multipart/form-data\">\r\n");
        writer.append("<input type=\"hidden\" name=\"action\" ")
                .append("value=\"create\"/>\r\n");
        writer.append("Your Name<br/>\r\n");
        writer.append("<input type=\"text\" name=\"customerName\"/><br/><br/>\r\n");
        writer.append("Subject<br/>\r\n");
        writer.append("<input type=\"text\" name=\"subject\"/><br/><br/>\r\n");
        writer.append("Body<br/>\r\n");
        writer.append("<textarea name=\"body\" rows=\"5\" cols=\"30\">")
                .append("</textarea><br/><br/>\r\n");
        writer.append("<b>Attachments</b><br/>\r\n");
        writer.append("<input type=\"file\" name=\"file1\"/><br/><br/>\r\n");
        writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
        writer.append("</form>\r\n");

        this.writeFooter(writer);
    }

    /**
     * 创建Ticket
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException      IO
     * @throws ServletException ServletException
     */
    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Ticket ticket = new Ticket();
        ticket.setCustomerName(request.getParameter("customerName"));
        ticket.setSubject(request.getParameter("subject"));
        ticket.setBody(request.getParameter("body"));
        Part filePart = request.getPart("file1");
        if (filePart != null) {
            Attachment attachment = this.processAttachment(filePart);
            if (attachment != null) {
                ticket.addAttachment(attachment);
            }
        }
        int id;
        synchronized (this) {
            id = this.TICKET_ID_SEQUENCE++;
            this.ticketDatabase.put(id, ticket);
        }
        response.sendRedirect("tickets?action=view&ticketId=" + id);
    }

    /**
     * processAttachment
     *
     * @param part part
     * @return Attachment
     * @throws IOException IO
     */
    private Attachment processAttachment(Part part) throws IOException {
        InputStream inputStream = part.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        final byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        Attachment attachment = new Attachment();
        attachment.setName(part.getSubmittedFileName());
        attachment.setContents(outputStream.toByteArray());
        return attachment;
    }

    /**
     * 获取Ticket
     *
     * @param idString id
     * @param response HttpServletResponse
     * @return Ticket
     * @throws ServletException ServletException
     * @throws IOException      IO
     */
    private Ticket getTicket(String idString, HttpServletResponse response)
            throws ServletException, IOException {
        if (idString == null || idString.length() == 0) {
            response.sendRedirect("tickets");
            return null;
        }

        try {
            Ticket ticket = this.ticketDatabase.get(Integer.parseInt(idString));
            if (ticket == null) {
                response.sendRedirect("tickets");
                return null;
            }
            return ticket;
        } catch (Exception e) {
            response.sendRedirect("tickets");
            return null;
        }
    }

    /**
     * 我来组成头部
     *
     * @param response HttpServletResponse
     * @return PrintWriter
     * @throws ServletException ServletException
     * @throws IOException      IO
     */
    private PrintWriter writeHeader(HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("    <head>\r\n")
                .append("        <title>Customer Support</title>\r\n")
                .append("    </head>\r\n")
                .append("    <body>\r\n");

        return writer;
    }

    /**
     * 我来组成底部
     *
     * @param writer PrintWriter
     */
    private void writeFooter(PrintWriter writer) {
        writer.append("    </body>\r\n").append("</html>\r\n");
    }

}
