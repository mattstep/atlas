package com.ning.atlas;

import java.util.Map;

public class NoOpInitializer implements Initializer
{

    public NoOpInitializer(Map<String, String> a)
    {

    }

    @Override
    public Server initialize(Server server, String arg, ProvisionedTemplate root)
    {
        return server;
    }
}
