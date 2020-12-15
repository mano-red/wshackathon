package com.wholesale.hackathon.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wholesale.hackathon.demo.dto.User;
import com.wholesale.hackathon.demo.repo.UserRepository;

@Service
public class UpdateService {
	@Autowired
	UserRepository repo;

	public void save(User user) {
		repo.save(user);

	}

	public List<User> getData() {
		Iterable<User> user = repo.findAll();

		List<User> users = new ArrayList();
		user.forEach(e -> users.add(e));

		return users;

	}

}
