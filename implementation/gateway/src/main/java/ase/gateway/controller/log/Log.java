package ase.gateway.controller.log;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private String remoteAddress;
    private String requestTime;
    private String requestMethod;
    public String requestURI;


    public Log(String remoteAddress, String requestTime, String requestMethod, String requestURI) {
        this.remoteAddress = remoteAddress;
        this.requestTime = requestTime;
        this.requestURI = requestURI;
        this.requestMethod = requestMethod;
    }

    public Log(Long id, String remoteAddress, String requestTime, String requestMethod, String requestURI) {
        this.id = id;
        this.remoteAddress = remoteAddress;
        this.requestTime = requestTime;
        this.requestURI = requestURI;
        this.requestMethod = requestMethod;
    }

    public Log() {
    }

}
