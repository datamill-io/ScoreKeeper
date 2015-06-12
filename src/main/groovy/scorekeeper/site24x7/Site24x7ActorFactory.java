package scorekeeper.site24x7;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.timgroup.statsd.StatsDClient;
import scorekeeper.Metric;
import scorekeeper.MetricsEnvironmentSetupMessage;

import java.sql.SQLException;
import java.util.Collection;

public class Site24x7ActorFactory {

    private final StatsDClient client;
    private final ActorContext context;
    private final MetricsEnvironmentSetupMessage setupMessage;

    public Site24x7ActorFactory(MetricsEnvironmentSetupMessage setupMessage, StatsDClient client, ActorContext context) {
        this.setupMessage = setupMessage;
        this.client = client;
        this.context = context;
    }

    public void makeMetricsForDatasource(Metric m) throws SQLException {
        makeScalarActor(m);
    }

    private void makeScalarActor(Metric m) throws SQLException {
        Props props = Props.create(Site24x7PollingActor.class, setupMessage, m.getUrl(), client, m);
        newActorFromProps(m.getActorName(), props);
    }

    private void newActorFromProps(String metricName, Props props)
            throws SQLException {
        ActorRef ar = context.actorOf(props, metricName);
    }
}
