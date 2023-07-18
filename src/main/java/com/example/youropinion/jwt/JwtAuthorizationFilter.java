package com.example.youropinion.jwt;

import com.example.youropinion.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request header 에서 토큰을 가져옵니다.
        String tokenValue = jwtUtil.getJwtFromHeader(request);

        // 토큰이 존재하고, 로그인 메서드는 POST
        //  if ( StringUtils.hasText(tokenValue) && !req.getMethod().equals("GET") ) { // 토큰이 있고, GET 메서드가 아닐 경우
        if (StringUtils.hasText(tokenValue)) { // 토큰이 있고, GET 메서드가 아닐 경우

            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                resultSetResponse(response,400,"유효하지 않은 토큰입니다.");
                log.error("유효하지 않은 토큰입니다.");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        filterChain.doFilter(request,response);
    }

    // 인증 처리 메서드
    private void setAuthentication(String name) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(name);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        // SecurityContextHolder 안에 SecurityContext 가 있고, SecurityContext 안에 Authentication 이 있다.
        // Authentication 안에 Principle Credentials, Authorities 이렇게 3개가 들어 있다.
    }


    // 인증 객세 생성 메서드
    private Authentication createAuthentication(String name) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


    // Client 에 반환할 msg, status 세팅 메서드
    private void resultSetResponse(HttpServletResponse res, int status, String msg) throws IOException {
        String jsonResult = " {\"status\": " + status + ", \"message\": \"" + msg + "\"}";

        // Content-Type 및 문자 인코딩 설정
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // PrintWriter 를 사용하여 응답 데이터 전송
        PrintWriter writer = res.getWriter();
        writer.write(jsonResult);
        writer.flush();
    }
}
