package com.subwayticket.control.servlet.mobile;

import com.google.gson.Gson;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.GsonUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.LoggerUtil;
import com.subwayticket.util.SecurityUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@WebServlet(name = "PhoneCaptchaServlet", urlPatterns = {"/mobileapi/PhoneCaptchaServlet"})
public class PhoneCaptchaServlet extends HttpServlet {
    private class PhoneCaptchaRequest{
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    private static Logger logger = LoggerUtil.getLogger("Phone Captcha Servlet", "PhoneCaptchaServlet.log");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = GsonUtil.getGson();
        PhoneCaptchaRequest pcReq = gson.fromJson(request.getParameter("data"), PhoneCaptchaRequest.class);
        Result result = SecurityUtil.sendPhoneCaptcha(request, pcReq.getPhoneNumber(), JedisUtil.getJedis());
        response.getWriter().write(gson.toJson(result));
        logger.info("JSON Result:" + gson.toJson(result));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
