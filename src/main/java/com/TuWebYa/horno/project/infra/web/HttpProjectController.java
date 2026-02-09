package com.TuWebYa.horno.project.infra.web;

import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.project.application.dto.response.ProjectResponse;
import com.TuWebYa.horno.project.application.port.in.GetMyProjectUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/projects")
public class HttpProjectController {

    private final GetMyProjectUseCase getMyProjectUseCase;
    private final SecurityContextService securityContextService;

    public HttpProjectController(
            GetMyProjectUseCase getMyProjectUseCase,
            SecurityContextService securityContextService
    ) {
        this.getMyProjectUseCase = getMyProjectUseCase;
        this.securityContextService = securityContextService;
    }

    @GetMapping("/mine")
    public Mono<ResponseEntity<ProjectResponse>> getMyProject() {
        return securityContextService.currentUserId()
                .flatMap(getMyProjectUseCase::getMyProject)
                .map(ResponseEntity::ok);
    }
}

