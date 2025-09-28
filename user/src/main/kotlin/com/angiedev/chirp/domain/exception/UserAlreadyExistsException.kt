package com.angiedev.chirp.domain.exception

import java.lang.RuntimeException

class UserAlreadyExistsException: RuntimeException ("User with the given username or email already exists")