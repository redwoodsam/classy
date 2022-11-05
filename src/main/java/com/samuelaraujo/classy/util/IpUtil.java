package com.samuelaraujo.classy.util;

import javax.servlet.http.HttpServletRequest;

import com.samuelaraujo.classy.service.security.LoginAttemptService;

public class IpUtil {

    public static boolean validaIpBloqueado(LoginAttemptService loginAttemptService, HttpServletRequest request) {
        String ip = obterIPCliente(request);
	    if(loginAttemptService.estaBloqueado(ip)) {
	        return true;
	    }

        return false;
    }

	public static String obterIPCliente(HttpServletRequest request) {
	    String xfHeader = request.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return request.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}
}
