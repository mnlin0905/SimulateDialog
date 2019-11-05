package com.oppo.commhelper.plugins.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public final class ListTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!List.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));

        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
                TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter);
        return result;
    }

    private static final class Adapter<E> extends TypeAdapter<List<E>> {
        private final TypeAdapter<E> elementTypeAdapter;

        public Adapter(Gson context, Type elementType,
                       TypeAdapter<E> elementTypeAdapter) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(
                    context, elementTypeAdapter, elementType);
        }

        //关键部分是这里，重写解析方法
        public List<E> read(JsonReader in) throws IOException {
            //新建一个空的列表
            List<E> list = new LinkedList<>();

            //null值返回null
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return list;
            }
            try {
                in.beginArray();
                while (in.hasNext()) {
                    E instance = elementTypeAdapter.read(in);
                    list.add(instance);
                }
                in.endArray();
                //正常解析成为列表
            } catch (IllegalStateException e) { //如果是空字符串，会有BEGIN_ARRAY报错

            }
            return list;
        }

        public void write(JsonWriter out, List<E> list) throws IOException {
            if (list == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            for (E element : list) {
                elementTypeAdapter.write(out, element);
            }
            out.endArray();
        }
    }
}
