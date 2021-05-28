package ase.gateway.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ase.gateway.controller.log.Log;
import ase.gateway.controller.log.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogInterceptor extends HandlerInterceptorAdapter
{
    @Autowired
    private LogRepository logRepository;

    //Intercepts request before handing over to handler method -- Saves Audit logs to repository
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String remoteAddress = request.getRemoteAddr();
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String requestDate = formatter.format(date);

        Log audit = new Log(remoteAddress, requestMethod, requestDate, requestURI);
        logRepository.save(audit);
        System.out.println("Audit log saved");
        return true;
    }

}
