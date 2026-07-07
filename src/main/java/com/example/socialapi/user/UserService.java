package com.example.socialapi.user;

import com.example.socialapi.user.dto.CreateUserRequest;
import com.example.socialapi.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse create(CreateUserRequest request) {
    var user = new User();

    user.setName(request.name());
    user.setEmail(request.email());
    user.setPassword(request.password());

    var savedUser = userRepository.save(user);
    return UserResponse.from(savedUser);
  }

  public UserResponse findById(Long id) {
    var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

    return UserResponse.from(user);
  }

  public List<UserResponse> findAll() {
    return userRepository.findAll().stream().map(UserResponse::from)
        .toList();
  }

  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }

    userRepository.deleteById(id);
  }
}
