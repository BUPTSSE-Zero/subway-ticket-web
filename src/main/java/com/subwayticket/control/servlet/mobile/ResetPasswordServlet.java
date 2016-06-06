package com.subwayticket.control.servlet.mobile;

import com.google.gson.Gson;
import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.GsonUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.LoggerUtil;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shengyun-zhou on 6/6/16.
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/mobileapi/ResetPasswordServlet"})
public class ResetPasswordServlet extends HttpServlet {
    @EJB
    private SubwayTicketDBHelperBean dbBean;
    private static Logger logger = LoggerUtil.getLogger("Reset Password Servlet", "ResetPasswordServlet.log");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = GsonUtil.getGson();
        try {
            ResetPasswordRequest rpReq = gson.fromJson(request.getParameter("data"), ResetPasswordRequest.class);
            Result result = AccountControl.resetPassword(request, rpReq, dbBean, JedisUtil.getJedis());
            response.getWriter().write(gson.toJson(result));
            logger.info("JSON Result:" + gson.toJson(result));
        }catch (Exception e){
            logger.error("", e);
            throw e;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
