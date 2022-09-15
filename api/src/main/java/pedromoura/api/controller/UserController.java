package pedromoura.api.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pedromoura.api.model.UserModel;
import pedromoura.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserController(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @GetMapping(path = "/user/list")
    public List<UserModel> list () {
    return repository.findAll();
    }

    @PostMapping(path = "/user/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel add (@RequestBody UserModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
       return repository.save(user);
    }

    @GetMapping (path = "/user/{id}")
    public ResponseEntity consult (@PathVariable ("id") Long id)
    {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/user/delete/{id}")
    public void delete (@PathVariable("id") Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
    }

    @GetMapping(path = "/user/validate")
    public ResponseEntity<Boolean> ValidatePassword (@RequestParam String login, @RequestParam String password){

        Optional<UserModel> optUser = repository.findByLogin(login);
        if (optUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        boolean valid = encoder.matches(password, optUser.get().getPassword());
        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);
    }
}
