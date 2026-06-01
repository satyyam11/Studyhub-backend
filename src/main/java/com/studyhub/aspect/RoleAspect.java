package com.studyhub.aspect;

import com.studyhub.annotation.RequireRole;
import com.studyhub.service.RoleService;
import com.studyhub.util.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Aspect
@Component
public class RoleAspect {

    @Autowired
    private RoleService roleService;

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        String currentUid = SecurityUtil.getCurrentUserUid();
        if (currentUid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String hubId = getHubId(joinPoint, requireRole.hubIdParam());
        if (hubId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hub ID not found");
        }

        String userRole = roleService.getRole(hubId, currentUid);
        if (userRole == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        boolean hasRole = Arrays.asList(requireRole.value()).contains(userRole);
        if (!hasRole) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return joinPoint.proceed();
    }

    private String getHubId(ProceedingJoinPoint joinPoint, String hubIdParamName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(hubIdParamName)) {
                return (String) args[i];
            }
        }
        return null;
    }
}
