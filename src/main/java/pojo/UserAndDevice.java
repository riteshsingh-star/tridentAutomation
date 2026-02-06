package pojo;

public record UserAndDevice(
        String email,
        String firstName,
        String lastName,
        String organizationName,
        long phoneNumber,
        String language,
        String unit,
        String timeZone,
        String alertMedia,
        String alertType,
        boolean isAdmin,
        String deviceIdentifier,
        String deviceName,
        String plantName,
        String externalDeviceType,
        String deviceType

) {
}
