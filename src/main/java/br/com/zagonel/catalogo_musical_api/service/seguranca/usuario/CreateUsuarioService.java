package br.com.zagonel.catalogo_musical_api.service.seguranca.usuario;

import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.UsuarioCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.UsuarioResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.UserRole;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Usuario;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.UserMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUsuarioService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO execute(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {

        if (userRepository.existsByEmail(usuarioCreateRequestDTO.getEmail())) {
            throw new DomainException("Já existe um usuario cadastrado com o email: " + usuarioCreateRequestDTO.getEmail());
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioCreateRequestDTO.getSenha());

        Usuario userDomain = Usuario.criarNovoUsuario(
                usuarioCreateRequestDTO.getNome(),
                usuarioCreateRequestDTO.getEmail(),
                senhaCriptografada,
                UserRole.USUARIO
        );

        UserJpaEntity savedEntity = userRepository.save(userMapper.toEntity(userDomain));

        return userMapper.toResponse(savedEntity);
    }

}
