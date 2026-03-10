package com.example.CWMS.iservice;

import java.io.BufferedReader;
import java.util.List;

public interface EmailValidationService {
    boolean isEmailReachable(String email);
    List<String> resolveMxRecords(String domain);
    Boolean trySmtpHandshake(String mxHost, String recipientEmail);
    String readSmtpResponse(BufferedReader reader) throws Exception;
}
