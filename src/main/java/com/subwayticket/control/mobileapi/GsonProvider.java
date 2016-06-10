package com.subwayticket.control.mobileapi;

import com.google.gson.Gson;
import com.subwayticket.util.GsonUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by shengyun-zhou on 6/10/16.
 */
@Provider
@Produces("application/json")
@Consumes("application/json")
public class GsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        Gson gson = GsonUtil.getGson();
        Reader inputReader = new InputStreamReader(entityStream, DEFAULT_CHARSET);
        return gson.fromJson(inputReader, type);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        OutputStreamWriter writer = new OutputStreamWriter(entityStream, DEFAULT_CHARSET);
        Gson gson = GsonUtil.getGson();
        writer.write(gson.toJson(o));
        writer.close();
    }
}
