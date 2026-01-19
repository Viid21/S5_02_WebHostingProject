package com.TuWebYa.horno.user.application.command;

public record CreateUserCommand (String name, String email, String password, String rol){
}
