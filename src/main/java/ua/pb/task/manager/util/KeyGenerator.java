package ua.pb.task.manager.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Component
public class KeyGenerator {
    private static final AtomicLong count = new AtomicLong(0);

    private SecureRandom sessionGenerator;

    public static Long getId() {
       return count.incrementAndGet();
    }

    public String getRandomKey() {
        byte[] buff = new byte[32];
        try {
            if (sessionGenerator == null) {
                sessionGenerator = SecureRandom.getInstance("SHA1PRNG");
                sessionGenerator.generateSeed(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sessionGenerator = new SecureRandom();
        }

        sessionGenerator.nextBytes(buff);
        return Base64.encodeBase64URLSafeString(buff);
    }
}
