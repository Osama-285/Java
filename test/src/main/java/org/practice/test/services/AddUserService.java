package org.practice.test.services;

import org.practice.test.model.User;
import org.practice.test.repository.AddUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AddUserService {
    @Autowired
    private AddUserRepo userRepo;

    public void addUser(User data) throws DataIntegrityViolationException {
        User user = new User(data.getUsername(), data.getEmail());
        userRepo.save(user);
    }
}
