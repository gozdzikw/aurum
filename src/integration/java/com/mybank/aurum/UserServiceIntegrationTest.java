package com.mybank.aurum;

import com.mybank.aurum.User;
import com.mybank.aurum.UserRepository;
import com.mybank.aurum.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldSaveUser() {
        // given
        int initialCount = userRepository.findAll().size();

        // when
        String firstName = "John";
        String lastName = "Doe";
        User registeredUser = userService.saveUser(firstName, lastName);

        // then
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getFirstName()).isEqualTo(firstName);

        assertThat(userRepository.findAll()).hasSize(initialCount + 1);
    }

//    @Test
//    void shouldThrowExceptionWhenThereIsNoLastName() {
//        // given
//        String firstName = "John";
//        String emptyLastName = "";
//
//        // expect
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.saveUser(firstName, emptyLastName);
//        }, "Empty lastName");
//        assertThat(userRepository.findAll()).isEmpty();
//    }
}
