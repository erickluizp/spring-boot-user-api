package com.educandoweb.course.resources;

import com.educandoweb.course.dto.UserDTO;
import com.educandoweb.course.dto.UserInsertDTO;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    private final UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "Lista todos os usuários",
            description = "Retorna todos os usuários cadastrados. Requer autenticação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @Operation(
            summary = "Busca usuário por ID",
            description = "Retorna os dados de um usuário específico. Requer autenticação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(
            summary = "Cadastra um novo usuário",
            description = "Cria um novo usuário e retorna seus dados sem a senha."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO obj) {
        User user = service.insert(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        UserDTO response = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(
            summary = "Exclui um usuário",
            description = "Exclui um usuário pelo ID. Requer autenticação com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível excluir o usuário"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualiza um usuário",
            description = "Atualiza os dados de um usuário. Requer autenticação com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserDTO obj) {
        User user = service.update(id, obj);

        UserDTO response = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
        return ResponseEntity.ok().body(response);
    }
}
