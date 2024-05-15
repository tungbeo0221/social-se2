package org.se2.authentication.service;

import org.se2.authentication.dto.UserDto;
import org.se2.authentication.model.User;

public interface UserService {
    User save (UserDto userDto);

    User getUserById(Long id);
}
