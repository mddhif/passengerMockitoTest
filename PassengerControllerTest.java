/**
 * 
 */
package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.crossover.techtrial.model.Passenger;
import com.crossover.techtrial.repositories.PassengerRepository;
import com.crossover.techtrial.service.PassengerService;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static java.util.Collections.singletonList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.mockito.InjectMocks;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PassengerControllerTest {
  
  MockMvc mockMvc;
  
  @InjectMocks
  private PassengerController passengerController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  PassengerRepository passengerRepository;
  

  @Mock
  PassengerService passengerService;

 @Autowired
  ObjectMapper objectMapper ;
  

  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(PassengerController).build();
  }
  
 
 //This is the original code commented out, I am using objects of class Passenger instead for unit testing with Mockito
 /* 
  @Test
  public void testPanelShouldBeRegistered() throws Exception {
	  HttpEntity<Object> Passenger = getHttpEntity(
		        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"," 
		            + " \"registrationNumber\": \"41DCT\",\"registrationDate\":\"2018-08-08T12:12:12\" }");
    ResponseEntity<Passenger> response = template.postForEntity(
        "/api/Passenger", Passenger, Passenger.class);
    //Delete this user
    PassengerRepository.deleteById(response.getBody().getId());
    Assert.assertEquals("test 1", response.getBody().getName());
    Assert.assertEquals(200,response.getStatusCode().value());
  }



  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

*/



  // I am using model objects of class Passenger for unit testing
	  private Passenger getPassenger() {
	  
	  Passenger passenger = new Passenger(); 
	  Passenger.setName("test1");
	  Passenger.setEmail("test1@gmail.com"); 
	  Passenger.setRegistrationNumber("XYZ");
	  
	  return passenger; 
	  
	  }
	 

 @Test
 public void retrievePassengersList() throws Exception {

  Passenger passenger = getPassenger();

   List<Passenger> passengerList = new ArrayList<Passenger>();

   PassengerList.add(passenger);
   
  
   when(passengerService.getAll()).thenReturn(passengerList);


   MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/api/passenger").accept(MediaType.APPLICATION_JSON)).andReturn();

   assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());


  
  String responseString = response.getResponse().getContentAsString();
  
  List<Passenger>  responseList = objectMapper.readValue(responseString, new TypeReference<List<Passenger>>(){});
   

   assertFalse(responseList.isEmpty());
 }


@Test
public void addPassenger() throws Exception {

Passenger passenger = getPassenger();

when(passengerService.save(passenger)).thenReturn(passenger);

MvcResult response = mockMvc.perform(post("/api/passenger").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(passenger))).andReturn();

assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());

String responseString = response.getResponse().getContentAsString();

Passenger testPassenger = objectMapper.readValue(responseString, Passenger.class);

assertEquals("test1", testPassenger.getName());
}


@Test
public void retrieveOnePassenger() throws Exception{


Passenger passenger = getPassenger();


   when(passengerService.findById(Long.valueOf(1))).thenReturn(passenger);

MvcResult response = mockMvc.perform(get("/api/passenger/1").accept(MediaType.APPLICATION_JSON)).andReturn();

 assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());

 String responseString = response.getResponse().getContentAsString();

 Passenger testPassenger = objectMapper.readValue(responseString, Passenger.class);

assertEquals("XYZ",testPassenger.getRegistrationNumber());
}



@Test
public void updatePassenger() throws Exception {

Passenger passenger, updatedPassenger;

passenger = updatedPassenger = getPassenger();


updatedPassenger.setName("test2");

when(passengerService.update(Long.valueOf(1), passenger)).thenReturn(updatedPassenger);

MvcResult response =  mockMvc.perform(put("/api/passenger/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(Passenger))).andReturn();

assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());

String responseString = response.getResponse().getContentAsString();

Passenger testPassenger = objectMapper.readValue(responseString, Passenger.class);

assertEquals("test2", testPassenger.getName());
}



@Test
public void deletePassenger() throws Exception{

when(passengerService.delete(Long.valueOf(1))).thenReturn("Passenger successfully deleted");

MvcResult response = mockMvc.perform(delete("/api/passenger/1")).andReturn();

assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());

String responseString = response.getResponse().getContentAsString();

assertEquals("Passenger successfully deleted", responseString);


}



}





