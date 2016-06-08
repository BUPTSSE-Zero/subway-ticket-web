package com.subwayticket.control.listener;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shengyun-zhou on 6/6/16.
 */
@WebFilter(filterName = "MobileAPIFilter", urlPatterns = {"/mobileapi/*"})
public class MobileAPIFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;
        if(request.getMethod().toLowerCase().equals("get")){
            response.setStatus(404);
            return;
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            chain.doFilter(req, resp);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
