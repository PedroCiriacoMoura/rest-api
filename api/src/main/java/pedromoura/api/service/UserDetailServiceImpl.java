package pedromoura.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pedromoura.api.data.UserDetailData;
import pedromoura.api.model.UserModel;
import pedromoura.api.repository.UserRepository;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = repository.findByLogin(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User [" + user + "] not found");
        }

        return new UserDetailData(user);
    }

}
