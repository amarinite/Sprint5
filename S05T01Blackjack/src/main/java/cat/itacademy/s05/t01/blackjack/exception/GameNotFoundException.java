package cat.itacademy.s05.t01.blackjack.exception;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(String message) {
        super(message);
    }
}
