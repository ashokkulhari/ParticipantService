package com.abc.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.entity.Participant;
import com.abc.model.ParticipantDTO;
import com.abc.repository.ParticipantRepository;

@Service
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepository;
	
	public Participant save(@Valid ParticipantDTO participantDTO) {
		Participant participant = new Participant(); 
		mapValuesFromDTO(participantDTO, participant);
		return participantRepository.save(participant);
		
	}

	public void mapValuesFromDTO(ParticipantDTO participantDTO, Participant participant) {
		if(participantDTO.getActive()!=null){
			participant.setActive(participantDTO.getActive());
		}
		if(participantDTO.getEmail()!=null){
			participant.setEmail(participantDTO.getEmail());
		}
		if(participantDTO.getFirstname()!=null){
			participant.setFirstname(participantDTO.getFirstname());
		}
		if(participantDTO.getLastname()!=null){
			participant.setLastname(participantDTO.getLastname());
		}
		participant.setInserted(Date.from(Instant.now()));
		if(participantDTO.getRefid()!=null){
			participant.setRefid(participantDTO.getRefid());
		}
		if(participantDTO.getSrc()!=null){
			participant.setSrc(participantDTO.getSrc());
		}
		if(participantDTO.getType()!=null){
			participant.setType(participantDTO.getType());
		}
		
	}

	public Participant updateParticipant(@Valid ParticipantDTO participantDTO,Integer participantId) {
		Participant participant =participantRepository.getOne(participantId);
		mapValuesFromDTO(participantDTO, participant);
		participant =participantRepository.save(participant);
		return participant;
	}

	public void deleteCandidate(Integer participantId) {
		participantRepository.delete(participantRepository.getOne(participantId));
		
	}

	public Participant findById(Integer participantId) {
		// TODO Auto-generated method stub
		return participantRepository.getOne(participantId);
	}

	public List<Participant> findAll() {
		// TODO Auto-generated method stub
		return participantRepository.findAll();
	}

}
