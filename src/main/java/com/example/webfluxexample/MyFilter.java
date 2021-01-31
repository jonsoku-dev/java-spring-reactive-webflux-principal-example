package com.example.webfluxexample;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {

    private EventNotify eventNotify;

    public MyFilter(EventNotify eventNotify) {
        this.eventNotify = eventNotify;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 실행됨 !!!");
        HttpServletResponse servletResponse = (HttpServletResponse) response;
//        servletResponse.setContentType("text/plain; charset=utf-8"); // text/plain 이므로 응답이 4번되어도, 브라우저가 flush 가 되어도 읽지않고 쌓아놨다가 마지막 응답때 한방에 응답한다.
        servletResponse.setContentType("text/event-stream; charset=utf-8");  // text/event-stream 이므로 요청할때마다 응답한다. -> 순차적으로 응답을 주는 Flux

        PrintWriter out = servletResponse.getWriter();

        // for 문이 종료되면 서버 응답이 종료된다.
        // WebFlux
        // 두군데서 다 사용할 수 있지만 어떤 것을 사용하는가에 따른 차이점이 있다.
        // WebFlux : 단일 스레드 , 비동기
        // MVC : 멀티 스레드

        // 1. Reactive Streams 라이브러리를 쓰면 표준을 지켜서 응답할 수 있다.
        for (int i = 0; i < 5; i++) {
            out.print("응답: " + i + "\n"); // 1초마다 한번씩 응답하도록 한다.
            out.flush(); // 버퍼를 비운다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // SSE - 응답을 유지시킨다. 이것을 어디에서 ? 단일스레드 -> WebFlux, 멀티스레드 -> MVC
        // reactive-stream : 표준

        // 2. SSE Emitter 라이브러리를 쓰면 편하게 쓸 수 있다.
        while (true) {
            try {
                if (eventNotify.getChange()) {
                    int lastIndex = eventNotify.getEvents().size() - 1;
                    out.print("응답: " + eventNotify.getEvents().get(lastIndex) + "\n"); // 1초마다 한번씩 응답하도록 한다.
                    out.flush(); // 버퍼를 비운다.
                    eventNotify.setChange(false);
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 3. WebFlux -> Reactive Streams 이 친구가 적용 된 stream 을 배우고 (비동기 단일스레드 방식) <-- 여기서 하는것이 좋음
    // 4. Servlet MVC -> Reactive Streams 이 친구가 적용 된 stream 을 배우고 (멀티스레드 방식)

    // 5. 요청이 들어올때마다 쓰레드를 늘리는 것은 좋지않다.. 서버터짐
}
