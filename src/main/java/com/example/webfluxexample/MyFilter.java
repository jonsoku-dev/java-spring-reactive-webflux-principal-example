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
//        servletResponse.setContentType("text/plain; charset=utf-8"); // text/plain 이므로 응답이 4번되어도, 브라우저가 flush 가 되어도 읽지않고 쌓아놨다가 마지막 응답때 한방에 응답한다.
        servletResponse.setContentType("text/event-stream; charset=utf-8");  // text/event-stream 이므로 요청할때마다 응답한다. -> 순차적으로 응답을 주는 Flux

        PrintWriter out = servletResponse.getWriter();

        // for 문이 종료되면 서버 응답이 종료된다.
        // WebFlux
        for (int i = 0; i < 5; i++) {
            out.print("응답: " + i + "\n"); // 1초마다 한번씩 응답하도록 한다.
            out.flush(); // 버퍼를 비운다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // SSE - 응답을 유지시킨다.
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
