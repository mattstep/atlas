package com.ning.atlas;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Map;

public class Base
{
    private final String name;
    private final Map<String, String> attributes = Maps.newConcurrentMap();

    public Base(String name)
    {
        this.name = name;
    }

    public Base(String name, Map<String, String> attributes) {
        this(name);
        this.attributes.putAll(attributes);
    }

    public Map<String, String> getAttributes()
    {
        return attributes;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;

        Base base = (Base) o;

        return attributes.equals(base.attributes) && name.equals(base.name);

    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + attributes.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                      .add("name", getName())
                      .add("attributes", attributes)
                      .toString();
    }
}