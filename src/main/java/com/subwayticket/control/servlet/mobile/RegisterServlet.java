package com.subwayticket.control.servlet.mobile;

import com.google.gson.Gson;
import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.RegisterRequest;
import com.subwayticket.model.Result;
import com.subwayticket.util.CheckUtil;
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
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/mobileapi/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
    @EJB
    private SubwayTicketDBHelperBean dbBean;
    private static Logger logger = LoggerUtil.getLogger("Register Servlet", "RegisterServlet.log");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = GsonUtil.getGson();
        try {
            RegisterRequest regReq = gson.fromJson(request.getParameter("data"), RegisterRequest.class);
            Result result = CheckUtil.checkRegisterInfo(request, regReq, dbBean, JedisUtil.getJedis());
            if (result.getResultCode() == PublicResultCode.SUCCESS_CODE) {
                Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
                dbBean.create(newUser);
            }
            response.getWriter().write(gson.toJson(result));
            logger.info("JSON Result:" + gson.toJson(result));
        }catch (Exception e){
            logger.error("", e);
            throw e;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
