package nbu.f104260.structurestudioreservationapp.Validators;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,20}$";
    public static boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}
