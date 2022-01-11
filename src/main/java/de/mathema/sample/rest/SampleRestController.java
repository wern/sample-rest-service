package de.mathema.sample.rest;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {

    private Date notHealthyUntil = new Date(0);
    private Date notReadyUntil = new Date(0);

    @GetMapping("/sampleservice")
    public String sayHello(@RequestParam(defaultValue = "John", name="to") String toWhom){
        if(Math.random() > 0.3) {
            return "Hello " + toWhom + "!";
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
