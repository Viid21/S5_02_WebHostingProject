package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.application.port.in.RetrieveUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.application.query.RetrieveUserAllQuery;
import com.TuWebYa.horno.user.application.query.RetrieveUserQuery;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RetrieveUserUseCaseImpl implements RetrieveUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public RetrieveUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<RetrieveUserResponse> retrieveById(RetrieveUserQuery query) {
        return userRepositoryPort.findById(query.id())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id: " + query.id() + " not found.")))
                .flatMap(user -> {
                    // Allow if user is retrieving their own information, or if they have admin role
                    boolean isOwnUser = query.authenticatedUserId() != null 
                            && query.authenticatedUserId().equals(query.id());
                    boolean isAdmin = !query.authenticatedUserRole().equals("USER");
                    
                    if (!isOwnUser && !isAdmin) {
                        return Mono.error(new UserForbiddenException());
                    }
                    
                    return Mono.just(new RetrieveUserResponse(
                            user.getId().value().toString(),
                            user.getName().value(),
                            user.getEmail().value(),
                            user.getRole().name()
                    ));
                });
    }

    @Override
    public Flux<RetrieveUserResponse> retrieveAllUsers(RetrieveUserAllQuery query) {
        return userRepositoryPort.findAll()
                .switchIfEmpty(Mono.error(new UserNotFoundException("User list empty.")))
                .flatMap(user -> {
                    if (query.authenticatedUserRole().equals("USER")) {
                        return Mono.error(new UserForbiddenException());
                    }
                    return Mono.just(new RetrieveUserResponse(
                            user.getId().value().toString(),
                            user.getName().value(),
                            user.getEmail().value(),
                            user.getRole().name()
                    ));
                });
    }
}
