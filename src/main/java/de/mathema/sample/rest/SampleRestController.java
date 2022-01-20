package de.mathema.sample.rest;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.net.http.HttpHeaders;
import java.util.Date;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleRestController {
 
    private final RestTemplate restTemplate;
    private String hostname;

    private Date notHealthyUntil = new Date(0);
    private Date notReadyUntil = new Date(0);

    public SampleRestController(RestTemplate restTemplate){
        this.restTemplate=restTemplate;

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

    @GetMapping("/sampletimeservice")
    public String sayHelloWithTime(@RequestParam(defaultValue = "John", name="to") String toWhom, @RequestHeader MultiValueMap<String,String> headers){
        Set<String> propagateTheseHeaders = Set.of("x-request-id", 
                                                    "x-b3-traceid",
                                                    "x-b3-spanid",
                                                    "x-b3-parentspanid",
                                                    "x-b3-sampled",
                                                    "x-b3-flags",
                                                    "x-ot-span-context");

        HttpHeaders requestHeaders = HttpHeaders.of(headers, (k,v) -> propagateTheseHeaders.contains(k));
        final HttpEntity<String> entity = new HttpEntity<String>(new MultiValueMapAdapter<String,String>(requestHeaders.map()));
        return "Hello " + toWhom + " from "+ hostname +"! It's " + restTemplate.exchange("http://sample-time-service:8080/now", HttpMethod.GET, entity, String.class).getBody() + ". :)";
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
