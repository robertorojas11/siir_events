package com.siir.itq.events.config.exceptions;

public class CustomExceptions {
    public static class ResourceNotFoundException extends RuntimeException{
        public ResourceNotFoundException(String message) {
        super(message);
    }
    }

    public static class ResourceConflictException extends RuntimeException{
        public ResourceConflictException (String resourceName, String id){
            super(String.format("El recurso %s con el id %s ya se encuentra registrado", resourceName, id));
        }
        public ResourceConflictException (String message){
            super(message);
        }
    }

    public static class InternalServerError extends RuntimeException{
        public InternalServerError (){
            super("Error durante la ejecucion");
        }
    }

    public static class BadRequestException extends RuntimeException{
        public BadRequestException(String message){
            super(message);
        }
    }

    public static class NotAuthorizedException extends RuntimeException{
        public NotAuthorizedException(String message){
            super(message);
        }
    }
}
