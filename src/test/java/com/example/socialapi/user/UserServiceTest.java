package com.example.socialapi.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.socialapi.user.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void mustCreateUserWithSuccess() {
    // Arrange
    var request = new CreateUserRequest("Rafael", "rafael@email.com", "senha123");
    var userSaved = new User(1L, "Rafael", "rafael@email.com", "senha123");

    when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(userSaved);

    // Act
    var response = userService.create(request);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Rafael");
    assertThat(response.email()).isEqualTo("rafael@email.com");
  }

  @Test
  void mustSearchUserById() {
    var user = new User();
    user.setId(1L);
    user.setName("Rafael");
    user.setEmail("rafael@email.com");
    user.setPassword("password123");

    when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

    var response = userService.findById(1L);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Rafael");
  }

  @Test
  void mustShowExceptionThereIsntUser() {
    when(userRepository.findById(999L)).thenReturn(java.util.Optional.empty());

    assertThatThrownBy(() -> userService.findById(999L)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void mustListEveryUser() {
    var user1 = new User();
    user1.setId(1L);
    user1.setName("Rafael");
    user1.setEmail("rafael@emai.com");
    user1.setPassword("senha123");

    var user2 = new User();
    user2.setId(2L);
    user2.setName("Maria");
    user2.setEmail("rafael@email.com");
    user2.setPassword("senha456");

    when(userRepository.findAll()).thenReturn(java.util.List.of(user1, user2));

    var response = userService.findAll();

    assertThat(response).hasSize(2);
    assertThat(response.get(0).name()).isEqualTo("Rafael");
    assertThat(response.get(1).name()).isEqualTo("Maria");
  }

  @Test
  void mustDeleteUserWithSuccess() {
    when(userRepository.existsById(1L)).thenReturn(true);

    userService.delete(1L);

    verify(userRepository).deleteById(1L);
  }

  @Test
  void mustSHowExceptionWhenThereIsntUser() {
    when(userRepository.existsById(999L)).thenReturn(false);

    assertThatThrownBy(() -> userService.delete(999L))
        .isInstanceOf(UserNotFoundException.class);
  }


}
