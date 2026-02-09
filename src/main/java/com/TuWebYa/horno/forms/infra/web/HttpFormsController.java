package com.TuWebYa.horno.forms.infra.web;

import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.forms.application.dto.request.CreateFormRequest;
import com.TuWebYa.horno.forms.application.dto.request.UpdateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import com.TuWebYa.horno.forms.application.dto.response.FormSubmissionResponse;
import com.TuWebYa.horno.forms.application.port.in.AssignAdminToFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.CheckFormExistsUseCase;
import com.TuWebYa.horno.forms.application.port.in.CreateFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.SubmitFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.UpdateFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.GetMyFormUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/forms")
public class HttpFormsController {
    
    private final SubmitFormUseCase submitFormUseCase;
    private final CreateFormUseCase createFormUseCase;
    private final UpdateFormUseCase updateFormUseCase;
    private final CheckFormExistsUseCase checkFormExistsUseCase;
    private final AssignAdminToFormUseCase assignAdminToFormUseCase;
    private final SecurityContextService securityContextService;
    private final GetMyFormUseCase getMyFormUseCase;
    
    public HttpFormsController(
            SubmitFormUseCase submitFormUseCase,
            CreateFormUseCase createFormUseCase,
            UpdateFormUseCase updateFormUseCase,
            CheckFormExistsUseCase checkFormExistsUseCase,
            AssignAdminToFormUseCase assignAdminToFormUseCase,
            SecurityContextService securityContextService,
            GetMyFormUseCase getMyFormUseCase) {
        this.submitFormUseCase = submitFormUseCase;
        this.createFormUseCase = createFormUseCase;
        this.updateFormUseCase = updateFormUseCase;
        this.checkFormExistsUseCase = checkFormExistsUseCase;
        this.assignAdminToFormUseCase = assignAdminToFormUseCase;
        this.securityContextService = securityContextService;
        this.getMyFormUseCase = getMyFormUseCase;
    }
    
    @PostMapping("/submit")
    public Mono<ResponseEntity<FormSubmissionResponse>> submitForm(@RequestBody CreateFormRequest request) {
        return submitFormUseCase.submitForm(request)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/check/{email}")
    public Mono<ResponseEntity<Boolean>> checkEmailExists(@PathVariable String email) {
        return checkFormExistsUseCase.existsByEmail(email)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/exists")
    public Mono<ResponseEntity<Boolean>> checkFormExistsForCurrentUser() {
        return securityContextService.currentUserId()
                .flatMap(checkFormExistsUseCase::existsByUserId)
                .map(ResponseEntity::ok);
    }
    
    @PostMapping("/create")
    public Mono<ResponseEntity<FormResponse>> createForm(@RequestBody CreateFormRequest request) {
        return Mono.zip(
                securityContextService.currentUserId(),
                securityContextService.currentUserRole()
        ).flatMap(tuple -> {
            UUID userId = tuple.getT1();
            String role = tuple.getT2();
            
            com.TuWebYa.horno.forms.application.command.CreateFormCommand command =
                    new com.TuWebYa.horno.forms.application.command.CreateFormCommand(
                            request.email(),
                            request.businessName(),
                            request.businessInfo(),
                            request.objectives(),
                            request.preferences(),
                            userId,
                            null // Solo superadmin puede asignar admin
                    );
            
            return createFormUseCase.createForm(command)
                    .map(ResponseEntity::ok);
        });
    }
    
    @PutMapping("/update")
    public Mono<ResponseEntity<FormResponse>> updateForm(@RequestBody UpdateFormRequest request) {
        return securityContextService.currentUserId()
                .flatMap(userId -> updateFormUseCase.updateForm(userId, request))
                .map(ResponseEntity::ok);
    }
    
    @PutMapping("/{formId}/assign-admin/{adminId}")
    public Mono<ResponseEntity<FormResponse>> assignAdminToForm(
            @PathVariable String formId,
            @PathVariable UUID adminId) {
        return securityContextService.currentUserRole()
                .flatMap(role -> assignAdminToFormUseCase.assignAdmin(formId, adminId, role))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/mine")
    public Mono<ResponseEntity<FormResponse>> getMyForm() {
        return securityContextService.currentUserId()
                .flatMap(getMyFormUseCase::getMyForm)
                .map(ResponseEntity::ok);
    }
}
