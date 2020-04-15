package ua.nure.korabelska.agrolab.exception;

public class UserNotFoundException extends Exception  {

    public UserNotFoundException(Long id) {
        super("User with id " + id + "was not found.");
    }
}
