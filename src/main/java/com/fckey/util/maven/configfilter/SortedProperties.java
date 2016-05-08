package com.fckey.util.maven.configfilter;

import java.util.*;

/**
 * Created by fckey on 2016/04/09.
 */
public class SortedProperties extends Properties {

    SortedProperties(Properties original) {
        for (java.lang.String name : original.stringPropertyNames()) {
            setProperty(name, original.getProperty(name));
        }
    }

    @Override
    public Enumeration<Object> keys() {
        List<Object> names = Arrays.asList(keySet().toArray());
        names.sort((s1,s2)-> s1.toString().compareTo(s2.toString()));
        return new EnumAdapter<Object>(names.iterator());
    }

    private static class EnumAdapter<T> implements Enumeration<T> {

        private Iterator<T> iterator;

        public EnumAdapter(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public T nextElement() {
            return iterator.next();
        }
    }


}
