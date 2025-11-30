package com.yuva.kreeda.vikasa.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
	private Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    private Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private static final int OTP_EXPIRY_MINUTES = 10;

    // Helper class to store OTP + expiry
    private static class OtpData {
        String otp;
        LocalDateTime expiry;

        OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }

    // Generate OTP
    public String generateOtp(String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        otpStore.put(email, new OtpData(otp, expiryTime));
        return otp;
    }

    // Validate OTP
    public boolean validateOtp(String email, String otp) {
        OtpData data = otpStore.get(email);
        if (data == null) return false;
        if (LocalDateTime.now().isAfter(data.expiry)) {
            otpStore.remove(email);
            return false;
        }
        boolean match = data.otp.equals(otp);
        if (match) {
            otpStore.remove(email);
            verifiedEmails.add(email);   //Mark email as verified
        }
        return match;
    }
    
    public boolean isEmailVerified(String email) {
        return verifiedEmails.contains(email);
    }

    public void clearVerifiedEmail(String email) {
        verifiedEmails.remove(email);
    }


}
