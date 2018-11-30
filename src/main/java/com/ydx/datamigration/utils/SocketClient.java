package com.ydx.datamigration.utils;


import com.google.common.base.Throwables;
import com.ydx.datamigration.enums.ConstantEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * socket客户端工具
 */
public class SocketClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public static String requestDataSourceInfo(String applicationName, String dataSourceName, String ip, int port) {
        port = OptionalInt.of(port).orElseThrow(
                () -> new NullPointerException(ConstantEnum.ERROR_DATASOURCE_PORT.getName()));
        ip = Optional.ofNullable(ip).orElseThrow(
                () -> new NullPointerException(ConstantEnum.ERROR_DATASOURCE_HOST.getName()));
        applicationName = Optional.ofNullable(applicationName)
                .orElse(ConstantEnum.ERROR_DATASOURCE_APPLICATION_NAME.getValue());
        dataSourceName = Optional.ofNullable(dataSourceName).orElseThrow(
                () -> new NullPointerException(ConstantEnum.ERROR_DATASOURCE_DATASOURCE_NAME.getName()));
        if (ConstantEnum.ERROR_DATASOURCE_APPLICATION_NAME.getValue().equals(applicationName)) {
            return applicationName;
        } else {
            applicationName = applicationName.toLowerCase(Locale.CHINA);
            StringBuilder sb = null;
            try (
                    Socket socket = new Socket(ip, port);
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream()
            ) {
                out.write(("applicationName=" + applicationName + "&" + "dataSourceName" + "=" + dataSourceName).getBytes());
                byte[] by = new byte[1024];
                int len = in.read(by);
                sb = new StringBuilder(new String(by, 0, len));
            } catch (ConnectException e) {
                logger.error("method=requestDataSourceInfo,{The socketserver has not started.} ==> {}", Throwables.getStackTraceAsString(e));
            } catch (IOException e1) {
                logger.error(Throwables.getStackTraceAsString(e1));
            }
            return sb.toString();
        }
    }
}
