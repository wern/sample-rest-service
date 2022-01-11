package de.mathema.sample.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {
    
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
        return "Ok!";
    }

    @GetMapping("/check/health")
    public String health(){
        return "Ok!";
    }
}
