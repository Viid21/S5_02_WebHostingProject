package com.TuWebYa.horno.user.application.port.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    boolean deleteUser(UUID id);
}
