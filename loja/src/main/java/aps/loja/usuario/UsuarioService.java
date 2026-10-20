package aps.loja.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User create(User userDTO) {

        userDTO.setSenha(passwordEncoder.encode(userDTO.getSenha()));

        return usuarioRepository.save(userDTO);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userDTO = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(userDTO.getEmail())
                .password(userDTO.getSenha())
                .build();

    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            UserDetails userDetails = this.loadUserByUsername(loginRequest.getEmail());

            if (!passwordEncoder.matches(loginRequest.getSenha(), userDetails.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
            }

            String token = jwtService.generateToken(loginRequest.getEmail());
            return new LoginResponse(token);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        }
    }

}
