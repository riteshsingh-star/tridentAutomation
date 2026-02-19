package exception;

public class NotAdminCredentials extends RuntimeException
{
    public NotAdminCredentials(String message) {
        super(message);
    }
}
