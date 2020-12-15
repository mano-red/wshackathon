package com.wholesale.hackathon.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wholesale.hackathon.demo.dto.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
 
}
