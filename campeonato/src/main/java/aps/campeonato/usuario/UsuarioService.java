package aps.campeonato.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

}
