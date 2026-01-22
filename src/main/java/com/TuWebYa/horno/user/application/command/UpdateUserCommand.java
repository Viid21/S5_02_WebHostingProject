package com.TuWebYa.horno.user.application.command;

import java.util.UUID;

public record UpdateUserCommand (UUID id, String name, String email, String role){
}
