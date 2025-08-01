package com.medical_ofiice.gateway.cors;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class CORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(CORSFilter.class);
    public CORSFilter(){
        log.info("CORSFilter init");
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if(ipAddress == null)
        {
            ipAddress = request.getRemoteAddr();
        }

        log.info("Path:" + path + "| Request Parameters: " + request.getQueryString() + " | Method:" + request.getMethod() + " | IP/Remote Address:" + ipAddress);

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers","Content-Type, Accept, X-Requested-With, remember-me");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        FilterRegistration.Dynamic corsFilter = filterConfig.getServletContext().addFilter("CORSFilter", this);
        corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }

    @Override
    public void destroy() {
        // Implementarea distrugerii filtrului
    }

}
