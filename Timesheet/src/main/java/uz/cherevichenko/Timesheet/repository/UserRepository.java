package uz.cherevichenko.Timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.cherevichenko.Timesheet.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.login = :login")
    Optional<User> findByLoginWithRoles(@Param("login") String login);
}
