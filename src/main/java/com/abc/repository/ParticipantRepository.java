package com.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.entity.Participant;


public interface ParticipantRepository extends JpaRepository<Participant,Integer>{

}
