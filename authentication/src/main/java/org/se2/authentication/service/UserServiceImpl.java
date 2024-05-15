package org.se2.authentication.service;

import org.se2.authentication.dto.UserDto;
import org.se2.authentication.model.User;
import org.se2.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), userDto.getFullname(), userDto.getPhoneNo(), userDto.getGender(), userDto.getDob());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
}
