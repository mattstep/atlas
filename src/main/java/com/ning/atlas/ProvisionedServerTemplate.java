package com.ning.atlas;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class ProvisionedServerTemplate extends ProvisionedTemplate
{
    private final String externalIpAddress;
    private final String internalIpAddress;

    @JsonIgnore
    private final Server server;
    private final List<String> installations;


    public ProvisionedServerTemplate(String type, String name, My my, Server server, List<String> installations)
    {
        super(type, name, my);
        this.server = server;
        this.installations = installations;
        this.externalIpAddress = server.getExternalIpAddress();
        this.internalIpAddress = server.getInternalIpAddress();
    }

    public ProvisionedServerTemplate(BoundServerTemplate boundServerTemplate, Server server, List<String> installations)
    {
        this(boundServerTemplate.getType(),
             boundServerTemplate.getName(),
             boundServerTemplate.getMy(),
             server,
             installations);
    }

    @JsonIgnore
    public List<? extends ProvisionedTemplate> getChildren()
    {
        return Collections.emptyList();
    }

    @Override
    public ListenableFuture<? extends InitializedTemplate> initialize(Executor ex, final ProvisionedTemplate root)
    {
        ListenableFutureTask<InitializedServerTemplate> f =
            new ListenableFutureTask<InitializedServerTemplate>(new Callable<InitializedServerTemplate>()
            {
                @Override
                public InitializedServerTemplate call() throws Exception
                {

                    final Server rs = server.getBase().initialize(server, root);
                    return new InitializedServerTemplate(getType(),
                                                         getName(),
                                                         getMy(),
                                                         rs,
                                                         installations);
                }
            });

        ex.execute(f);
        return f;
    }

    @JsonProperty("external_ip")
    public String getExternalIP()
    {
        return externalIpAddress;
    }

    @JsonProperty("internal_ip")
    public String getInternalIP()
    {
        return internalIpAddress;
    }
}
