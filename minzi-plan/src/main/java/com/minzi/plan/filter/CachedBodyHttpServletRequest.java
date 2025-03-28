package com.minzi.plan.filter;

import javax.servlet.ReadListener;
import javax.servlet.http.HttpServletRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] body;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        BufferedReader reader = request.getReader();
        try (StringWriter writer = new StringWriter()) {
            int read;
            char[] buf = new char[1024 * 8];
            while ((read = reader.read(buf)) != -1) {
                writer.write(buf, 0, read);
            }
            this.body = writer.getBuffer().toString().getBytes();
        }
    }
    public String getBody(){
        return new String(body, StandardCharsets.UTF_8);
    }
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}