package org.tinger.common.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * io工具类
 * Created by tinger on 2022-10-01
 */
public class IoUtils {
    /**
     * 关闭
     *
     * @param closeable nullable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {

            }
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {

            }
        }
    }

    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    public static void copy(InputStream source, OutputStream target) {
        try {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int count = 0;
            while (true) {
                count = source.read(bytes, 0, DEFAULT_BUFFER_SIZE);
                if (count < 0) {
                    break;
                }
                target.write(bytes, 0, count);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }
}
