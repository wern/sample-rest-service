package de.mathema.sample.rest;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {

    private Date notHealthyUntil = new Date(0);
    private Date notReadyUntil = new Date(0);
    private String hostname;

    public SampleRestController(){
        try {
            hostname = Inet4Address.getLocalHost().getHostName();
        } catch(UnknownHostException ex){
            hostname = "UnknownHost";
        }
    }

    @GetMapping("/sampleservice")
    public String sayHello(@RequestParam(defaultValue = "John", name="to") String toWhom){
        if(Math.random() > 0.1) {
            return "Hello " + toWhom + " from "+ hostname +" :)";
        } else {
            throw new RuntimeException("Something really bad happend!!!!");
        }
    }

    @GetMapping("/check/ready")
    public String ready(){
        if(new Date().before(notReadyUntil)){
            throw new RuntimeException("I don't feel good :(!!!!");
        } else {
            return "Ok!";
        }
    }

    @GetMapping("/check/health")
    public String health(){
        if(new Date().before(notHealthyUntil)){
            throw new RuntimeException("I don't feel good :(!!!!");
        } else {
            return "Ok!";
        }
    }

    @PostMapping("/mock/notHealthy")
    public void mockHealthyFailure(@RequestParam(defaultValue = "10", name="failureSpanInSeconds") int failureSpanInSeconds){
        notHealthyUntil = new Date(System.currentTimeMillis()+failureSpanInSeconds*1000);
    }

    @PostMapping("/mock/notReady")
    public void mockReadyFailure(@RequestParam(defaultValue = "10", name="failureSpanInSeconds") int failureSpanInSeconds){
        notReadyUntil = new Date(System.currentTimeMillis()+failureSpanInSeconds*1000);
    }
}
