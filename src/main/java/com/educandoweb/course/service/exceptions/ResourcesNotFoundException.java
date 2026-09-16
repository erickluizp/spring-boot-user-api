package com.educandoweb.course.service.exceptions;

import java.io.Serial;

public class ResourcesNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourcesNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }
}
