package ua.nure.korabelska.agrolab.exception;

public class UserNotFoundException extends Exception  {

    public UserNotFoundException(Long id) {
        super("User with username " + id + " was not found.");
    }

    public UserNotFoundException(String username) {
        super("User with username " + username + " was not found.");
    }


}
