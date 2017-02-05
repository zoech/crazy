package com.imeee.crazy.misc;

/**
 * Created by Administrator on 2017/1/25.
 */

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class FastJsonConverterFactory extends Converter.Factory {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final SerializeConfig serializeConfig;

    private FastJsonConverterFactory(SerializeConfig serializeConfig) {
        if (serializeConfig == null)
            throw new NullPointerException("serializeConfig == null");
        this.serializeConfig = serializeConfig;
    }

    public static FastJsonConverterFactory create() {
        return create(SerializeConfig.getGlobalInstance());
    }

    public static FastJsonConverterFactory create(SerializeConfig serializeConfig) {
        return new FastJsonConverterFactory(serializeConfig);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>(serializeConfig);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type);
    }




    public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

        private SerializeConfig serializeConfig;

        public FastJsonRequestBodyConverter(SerializeConfig serializeConfig) {
            this.serializeConfig = serializeConfig;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, serializeConfig));
        }
    }

    public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private Type type;

        public FastJsonResponseBodyConverter(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            return JSON.parseObject(value.string(), type);
        }
    }
}