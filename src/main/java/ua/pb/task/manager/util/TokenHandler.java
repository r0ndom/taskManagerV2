package ua.pb.task.manager.util;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mike on 3/10/2016.
 */
@Component
//@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenHandler {

    private String token;
    private String error;

    final Lock lock = new ReentrantLock();

    final Condition gotAuthorizationResponse = lock.newCondition();

    public String getToken() throws IOException {
        lock.lock();
        try {
            while (token == null && error == null) {
                gotAuthorizationResponse.awaitUninterruptibly();
            }
            if (error != null) {
                throw new IOException("User authorization failed (" + error + ")");
            }
            return token;
        } finally {
            lock.unlock();
        }
    }

    public void setData(String token, String error) {
        lock.lock();
        try {
            this.token = token;
            this.error = error;
            gotAuthorizationResponse.signal();
        } finally {
            lock.unlock();
        }

    }
}
