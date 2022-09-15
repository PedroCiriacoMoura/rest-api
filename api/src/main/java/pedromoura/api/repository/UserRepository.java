package pedromoura.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedromoura.api.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

}
