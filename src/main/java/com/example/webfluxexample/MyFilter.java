package com.example.webfluxexample;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 실행됨 !!!");
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setContentType("text/plain; charset=utf-8"); // text/plain 이므로 브라우저가 쌓아놨다가 한번에 응답한다.

        PrintWriter out = servletResponse.getWriter();

        for (int i = 0; i < 5; i++) {
            out.print("응답: " + i + "\n"); // 1초마다 한번씩 응답하도록 한다.
            out.flush(); // 버퍼를 비운다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
