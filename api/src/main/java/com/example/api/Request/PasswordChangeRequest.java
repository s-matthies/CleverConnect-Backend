package com.example.api.Request;

/**
 * Diese Klasse ist ein Request Objekt, welches die Daten für die Änderung des Passworts eines Benutzers enthält.
 */
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword; }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword; }

    public String getNewPassword() {
        return newPassword; }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
