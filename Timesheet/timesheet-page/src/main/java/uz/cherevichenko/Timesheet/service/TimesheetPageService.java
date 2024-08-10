package uz.cherevichenko.Timesheet.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

//import uz.cherevichenko.Timesheet.client.TimesheetResponse;

import uz.cherevichenko.Timesheet.page.TimesheetsPageDto;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TimesheetPageService {



    private final DiscoveryClient discoveryClient;

    public TimesheetPageService(DiscoveryClient discoveryClient){
        this.discoveryClient = discoveryClient;

    }
    private RestClient restClient(){
        List<ServiceInstance> instances  =  discoveryClient.getInstances("TIMESHEET-REST");

int instancesCount = instances.size();
int instancesIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);
ServiceInstance instance = instances.get(instancesIndex);
String uri = "http://"+instance.getHost()+ ":"+instance.getPort();
return RestClient.create(uri);
    }



    public Optional<TimesheetsPageDto> findByID(Long id) {
        try {
            String jsonString = restClient().get()
                    .uri("/timesheets/" + id)
                    .retrieve()
                    .body(String.class);
            JSONObject data = new JSONObject(jsonString);
            JSONObject project = data.getJSONObject("project");




            TimesheetsPageDto timesheetsPageDto = new TimesheetsPageDto();
            timesheetsPageDto.setId(String.valueOf(data.getInt("id")));
            timesheetsPageDto.setMinutes(String.valueOf(data.getInt("minutes")));
            timesheetsPageDto.setCreatedAt(data.getString("createdAt"));

            timesheetsPageDto.setProjectId(String.valueOf(project.getInt("id")));
            timesheetsPageDto.setProjectName(String.valueOf(project.getString("name")));

            return Optional.of(timesheetsPageDto);

        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }


public List<TimesheetsPageDto> findAll() {
       try{

       List<Object> jsonObject = restClient().get()
                .uri("/timesheets")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Object>>() {
                });
           ObjectMapper mapper = new ObjectMapper();
           List<String> jsonString = new ArrayList<String>();
           for (Object obj : jsonObject) {
               jsonString.add(mapper.writeValueAsString(obj));
           }

       List<TimesheetsPageDto> result = new ArrayList<>();

       for (String str : jsonString) {

           JSONObject data = new JSONObject(str);
           JSONObject project = data.getJSONObject("project");
           TimesheetsPageDto timesheetsPageDto = new TimesheetsPageDto();
           timesheetsPageDto.setId(String.valueOf(data.getInt("id")));
           timesheetsPageDto.setMinutes(String.valueOf(data.getInt("minutes")));
           timesheetsPageDto.setCreatedAt(data.getString("createdAt"));
           timesheetsPageDto.setProjectId(String.valueOf(project.getInt("id")));
           timesheetsPageDto.setProjectName(String.valueOf(project.getString("name")));
           result.add(timesheetsPageDto);
       }
       return result;
       }catch (HttpClientErrorException.NotFound e){
           return null;
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
}



}


