package uz.cherevichenko.Timesheet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.cherevichenko.Timesheet.model.User;
import uz.cherevichenko.Timesheet.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MyCustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь нен найден"));
        System.out.println(user);


        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        );
    }
}
