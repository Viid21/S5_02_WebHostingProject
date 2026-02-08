package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.ChangeUserRoleCommand;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.application.port.in.ChangeUserRoleUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.UserRole;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChangeUserRoleUseCaseImpl implements ChangeUserRoleUseCase {
    
    private final UserRepositoryPort userRepositoryPort;
    
    public ChangeUserRoleUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }
    
    @Override
    public Mono<UpdateUserResponse> changeRole(ChangeUserRoleCommand command) {
        // Solo SUPERADMIN puede cambiar roles
        if (!command.authenticatedUserRole().equals("SUPERADMIN")) {
            return Mono.error(new UserForbiddenException("Only SUPERADMIN can change user roles"));
        }
        
        // No se puede cambiar a SUPERADMIN
        if (command.newRole().equals("SUPERADMIN")) {
            return Mono.error(new UserForbiddenException("Cannot change role to SUPERADMIN"));
        }
        
        // Solo se puede cambiar entre USER y ADMIN
        if (!command.newRole().equals("USER") && !command.newRole().equals("ADMIN")) {
            return Mono.error(new UserForbiddenException("Invalid role. Only USER and ADMIN are allowed"));
        }
        
        return userRepositoryPort.findById(command.userId())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id: " + command.userId() + " not found.")))
                .flatMap(user -> {
                    // No se puede cambiar el rol de un SUPERADMIN
                    if (user.getRole() == UserRole.SUPERADMIN) {
                        return Mono.error(new UserForbiddenException("Cannot change SUPERADMIN role"));
                    }
                    
                    user.setRole(UserRole.valueOf(command.newRole()));
                    return userRepositoryPort.save(user);
                })
                .map(saved -> new UpdateUserResponse(
                        saved.getId().value().toString(),
                        saved.getName().value(),
                        saved.getEmail().value(),
                        saved.getRole().name()
                ));
    }
}

