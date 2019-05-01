package com.abc.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abc.entity.Participant;
import com.abc.model.ParticipantDTO;
import com.abc.model.RequestVo;
import com.abc.model.ResponseVo;
import com.abc.service.ParticipantService;
import com.abc.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ParticipantController {

	private final Logger log = LoggerFactory.getLogger(ParticipantController.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired	
	private ParticipantService participantService;
	
	@Value("${token_url}")
    String validateSessionUrl;
	
	@PostMapping("/add/participant")
//    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?>  addParticipant(@Valid @RequestBody ParticipantDTO participantDTO ) {
        Map<String, Object> response = new ManagedMap<>();
        Participant  participant =null;
            try{
            	String sessionId = request.getHeader("sessionid");
            	ResponseEntity<ResponseVo> responsetr = validateSession(sessionId);
				
				System.out.println(responsetr.getBody());
				System.out.println("Success    =  "+responsetr.getBody().getSuccess());
				if(responsetr.getBody().getSuccess()){
					participant =participantService.save(participantDTO);    
	            	response.put("statuscode",0);
	            	response.put("output" ,participant);
				}else{
					response.put("msg",responsetr.getBody().getErrorMessage());
	            	response.put("statuscode",200);
				}
            	return  new ResponseEntity<>(response, HttpStatus.CREATED); 
            }catch (Exception e) {e.printStackTrace();
            	response.put("statuscode",100);
            	response.put("msg",e.getMessage());
            	return  new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
            }
    }
	public ResponseEntity<ResponseVo> validateSession(String sessionId) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		RequestVo requestVo = new RequestVo();
		requestVo.setSessionid(sessionId);
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<RequestVo> entity = new HttpEntity<RequestVo>(requestVo,requestHeaders);
		ResponseEntity<ResponseVo> responsetr=   restTemplate.exchange(validateSessionUrl,
				HttpMethod.POST, entity, ResponseVo.class);
		return responsetr;
	}
	
//	@GetMapping("/participants, /participants/{participantId}")
	@RequestMapping(value = {"/participants", "/participants/{participantId}"}, method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable(name="participantId" , required=false) Integer participantId) {
		 log.info("REST request to get Participant: {}", participantId);
	        Map<String, Object> response = new ManagedMap<>();
	        try{
	        	String sessionId = request.getHeader("sessionid");
	        	ResponseEntity<ResponseVo> responsetr = validateSession(sessionId);
	        	if(responsetr.getBody().getSuccess()){
	        		System.out.println("participantId "+participantId);
	        		if(participantId!=null ){
	        			Participant participant = participantService.findById(participantId);
	        			response.put("output" ,participant);
	        		}else{
	        			List<Participant> participants = participantService.findAll();
	        			response.put("output" ,participants);
	        		}
	            	response.put("statuscode",0);
	        	}else{
					response.put("msg",responsetr.getBody().getErrorMessage());
	            	response.put("statuscode",200);
				}
	   		 return  new ResponseEntity<>(response, HttpStatus.OK);
	        }catch (Exception e) {
	        	response.put("statuscode",100);
	        	response.put("msg",e.getMessage());
	        	return  new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
	        }
    }
	@PutMapping("/participants/{participantId}")
    public ResponseEntity<?> updateParticipant(@RequestBody ParticipantDTO participantDTO,@PathVariable Integer participantId) {
        log.debug("REST request to update Participant : {}", participantDTO);
        Participant  participant =null;
        Map<String, Object> response = new ManagedMap<>();
        try{
        	String sessionId = request.getHeader("sessionid");
        	ResponseEntity<ResponseVo> responsetr = validateSession(sessionId);
        	if(responsetr.getBody().getSuccess()){
        		participant = participantService.updateParticipant(participantDTO,participantId);
       		 response.put("statuscode",0);
       		 response.put("output" ,participant);
			}else{
				response.put("msg",responsetr.getBody().getErrorMessage());
            	response.put("statuscode",200);
			}
        		 return  new ResponseEntity<>(response, HttpStatus.OK); 
        }catch (Exception e) {
        	response.put("statuscode",100);
        	response.put("msg",e.getMessage());
        	return  new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
	}
	
	@DeleteMapping("/participants/{participantId}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer participantId) {
        log.debug("REST request to delete Participant: {}", participantId);
        Map<String, Object> response = new ManagedMap<>();
        try{
        	String sessionId = request.getHeader("sessionid");
        	ResponseEntity<ResponseVo> responsetr = validateSession(sessionId);
        	if(responsetr.getBody().getSuccess()){
        		participantService.deleteCandidate(participantId);
            	response.put("statuscode",0);
            	response.put("msg","Deleted Participant with ID :"+participantId);
        	}else{
				response.put("msg",responsetr.getBody().getErrorMessage());
            	response.put("statuscode",200);
			}
        	HttpHeaders headers=HeaderUtil.createEntityDeletionAlert("Participant", ""+participantId);
   		 return  new ResponseEntity<>(response, headers,HttpStatus.OK);
        }catch (Exception e) {
        	response.put("statuscode",100);
        	response.put("msg",e.getMessage());
        	return  new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
	
}
