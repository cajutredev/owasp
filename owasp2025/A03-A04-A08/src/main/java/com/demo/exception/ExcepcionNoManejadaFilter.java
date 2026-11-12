package com.demo.exception;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class ExcepcionNoManejadaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse)response;
        try {
            chain.doFilter(request, httpResponse);
        } catch (Exception e) {
            
            if (response.isCommitted()) {
                relanzar(e);
            }

            httpResponse.reset();
            httpResponse.setHeader("Cache-Control", "no-store");
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }        
    }

    private void relanzar(Exception exception) throws IOException, ServletException {
        if (exception instanceof IOException ioException) {
            throw ioException;
        }
        if (exception instanceof ServletException servletException) {
            throw servletException;
        }
        if (exception instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
    }
}
