package org.pc3r.webtoontracker_server;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // Allow requests from all origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Allow specific HTTP methods
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Allow specific HTTP headers
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");

        // Allow credentials (if needed)
        // response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
