package nbu.f104260.structurestudioreservationapp.Validators;

public class PhoneValidator {

    private static final String BULGARIAN_PHONE_PATTERN =
            "^((\\\\(\\\\d{3}\\\\))|\\\\d{3})[- .]?\\\\d{3}[- .]?\\\\d{4}$";
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches(BULGARIAN_PHONE_PATTERN);
    }
}
