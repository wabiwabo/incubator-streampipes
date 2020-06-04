/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.rest.shared.serializer;

import com.google.gson.Gson;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


public abstract class GsonJerseyProvider implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {

    private final String UTF8 = "UTF-8";

    protected abstract Gson getGsonSerializer();

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return jsonSerialized(mediaType) && requiredAnnotationsPresent(annotations);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
                           Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        InputStreamReader streamReader = new InputStreamReader(entityStream, UTF8);

        try {
            Type jsonType;
            if (type.equals(genericType)) {
                jsonType = type;
            } else {
                jsonType = genericType;
            }

            return getGsonSerializer().fromJson(streamReader, jsonType);
        } finally {
            streamReader.close();
        }

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return jsonSerialized(mediaType) && requiredAnnotationsPresent(annotations);
    }

    @Override
    public long getSize(Object t, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object t, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException,
            WebApplicationException {

        OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF8);

        try {
            Type jsonType;
            if (type.equals(genericType)) {
                jsonType = type;
            } else {
                jsonType = genericType;
            }

            getGsonSerializer().toJson(t, jsonType, writer);
        } finally {
            writer.close();
        }
    }

    protected boolean jsonSerialized(MediaType mediaType) {
        return mediaType.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType()) &&
                mediaType.getSubtype().equals(MediaType.APPLICATION_JSON_TYPE.getSubtype());
    }

    protected abstract boolean requiredAnnotationsPresent(Annotation[] annotations);
}
