package com.riskaudit.servlet;

import com.google.gson.Gson;
import com.riskaudit.util.Helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alisenturk
 */
@WebServlet(name = "FileDownloader", urlPatterns = {"/FileDownloader"})
public class FileDownloader extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FileDownloader</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FileDownloader at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
       try{
           request.setCharacterEncoding("UTF-8");
           response.setCharacterEncoding("UTF-8");
           
           HttpSession session = request.getSession();
           String fileData = request.getParameter("file");
           if(session.getAttribute(Helper.SES_SECRET_KEY)!=null){
               String key = (String)session.getAttribute(Helper.SES_SECRET_KEY);
               String data = Helper.getDecryptData(fileData, key,false);
               Gson gson = new Gson();
               FileDownload fileDownload = gson.fromJson(data,FileDownload.class);
               
               if(fileDownload!=null){
                    response.setContentType(fileDownload.getContentType()); 
                    response.addHeader("Content-Disposition", "attachment; filename=" + fileDownload.getFileName()); 
                    File 		file	= new File(fileDownload.getFilePath());
                    OutputStream 	out 	= response.getOutputStream();
                    FileInputStream fileIn 	= new FileInputStream(file);

                    byte[] outputByte = new byte[4096];					
                    while(fileIn.read(outputByte, 0, 4096) != -1)
                    {
                            out.write(outputByte, 0, 4096);
                    }
                    fileIn.close();
                    out.flush();
                    out.close();
               }else{
                   processRequest(request, response);
               }
           }else{
               processRequest(request, response);
           }
       }catch(Exception e){
           Helper.errorLogger(getClass(), e);
           try {
               processRequest(request, response);
           } catch (ServletException | IOException ex) {
               Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }

    
}
