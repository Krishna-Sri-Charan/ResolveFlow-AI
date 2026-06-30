package com.cms.security.ratelimit;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIp = getClientIp(request);

        Bucket bucket;

        String uri = request.getRequestURI();

        if (uri.startsWith("/api/v1/auth/login")) {

            bucket = rateLimitService.resolveBucket(
                    clientIp + ":login",
                    10,
                    Duration.ofMinutes(1)
            );

        }
        else if (uri.startsWith("/api/v1/auth/register")) {

            bucket = rateLimitService.resolveBucket(
                    clientIp + ":register",
                    5,
                    Duration.ofMinutes(1)
            );

        }
        else if (uri.startsWith("/api/v1/complaints")) {

            bucket = rateLimitService.resolveBucket(
                    clientIp + ":complaints",
                    20,
                    Duration.ofMinutes(1)
            );

        }
        else if (uri.startsWith("/api/v1/ai")) {

            bucket = rateLimitService.resolveBucket(
                    clientIp + ":ai",
                    10,
                    Duration.ofMinutes(1)
            );

        }
        else {

            filterChain.doFilter(request, response);

            return;

        }

        ConsumptionProbe probe =
                bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {

            response.addHeader(
                    "X-Rate-Limit-Remaining",
                    String.valueOf(
                            probe.getRemainingTokens()
                    )
            );
            
            response.addHeader(
                    "X-Rate-Limit-Limit",
                    String.valueOf(
                            probe.getRemainingTokens() + 1
                    )
            );
            
            response.addHeader(
                    "Retry-After",
                    "60"
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } else {

            response.setStatus(
            		HttpStatus.TOO_MANY_REQUESTS.value()
            );

            response.setContentType(
                    MediaType.APPLICATION_JSON_VALUE
            );

            response.getWriter().write(
	    		"""
	    		{
	    		  "success": false,
	    		  "message": "Too many requests. Please wait a minute before trying again."
	    		}
	    		"""
    		);
        }
    }

    private String getClientIp(
            HttpServletRequest request
    ) {

        String forwarded =
                request.getHeader(
                        "X-Forwarded-For"
                );

        if (forwarded != null &&
                !forwarded.isBlank()) {

            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}