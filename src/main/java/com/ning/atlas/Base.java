package com.ning.atlas;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Base
{
    private final String name;
    private final Map<String, String> attributes = Maps.newConcurrentMap();
    private final Provisioner              provisioner;
    private final Map<String, Initializer> initalizers;
    private final List<String> inits = new CopyOnWriteArrayList<String>();
    private final Map<String, Installer> installers;

    public Base(String name, Environment env, Map<String, String> attributes)
    {
        this.name = name;
        this.provisioner = env.getProvisioner();
        this.initalizers = env.getInitializers();
        this.installers = env.getInstallers();
        this.attributes.putAll(attributes);
    }

    public Base(String name, Environment env)
    {
        this(name, env, Collections.<String, String>emptyMap());
    }

    public Map<String, String> getAttributes()
    {
        return attributes;
    }

    public String getName()
    {
        return name;
    }

    public Provisioner getProvisioner()
    {
        return provisioner;
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

    public void addInit(String initializer)
    {
        inits.add(initializer);
    }

    public Server initialize(Server server, ProvisionedTemplate root)
    {
        Server next = server;
        for (String init : inits) {
            int idx = init.indexOf(':');
            String prefix = init.substring(0, idx);
            String arg = init.substring(idx + 1, init.length());
            Initializer i = initalizers.get(prefix);
            next = i.initialize(server, arg, root);
        }
        return next;
    }

    public List<String> getInits()
    {
        return inits;
    }

    public Installer getInstaller(String prefix)
    {
        return installers.get(prefix);
    }
}
