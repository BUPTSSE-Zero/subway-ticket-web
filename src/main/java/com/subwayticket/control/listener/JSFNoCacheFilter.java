package com.subwayticket.control.listener;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，禁止所有web网页的缓存
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
@WebFilter(filterName = "JSFNoCacheFilter", servletNames = {"Faces Servlet"})
public class JSFNoCacheFilter implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!req.getRequestURI().contains(ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            res.setDateHeader("Expires", 0); // Proxies.
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {}

}
