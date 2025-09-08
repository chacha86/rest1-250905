package com.rest1.global.aspect;

import com.rest1.global.rsData.RsData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ResponseAspect {
    private final HttpServletResponse response;

    @Around("""
                (
                    within(@org.springframework.web.bind.annotation.RestController *) &&
                    (
                        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
                        @annotation(org.springframework.web.bind.annotation.PostMapping) ||
                        @annotation(org.springframework.web.bind.annotation.PutMapping) ||
                        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
                        @annotation(org.springframework.web.bind.annotation.RequestMapping)
                    )
                ) ||
                @annotation(org.springframework.web.bind.annotation.ResponseBody)
            """)
    public Object handleResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // 원래 메서드 실행
        Object proceed = joinPoint.proceed();

        // RsData 타입이면 상태 코드 설정
        if (proceed instanceof RsData) {
            RsData<?> rsData = (RsData<?>) proceed;
            response.setStatus(rsData.getStatusCode());
        }

        return proceed;
    }
}
