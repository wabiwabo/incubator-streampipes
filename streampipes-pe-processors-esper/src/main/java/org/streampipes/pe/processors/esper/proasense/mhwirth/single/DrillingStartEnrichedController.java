package org.streampipes.pe.processors.esper.proasense.mhwirth.single;


import org.streampipes.commons.Utils;
import org.streampipes.container.util.StandardTransportFormat;
import org.streampipes.model.DataProcessorType;
import org.streampipes.model.SpDataStream;
import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.model.output.AppendOutputStrategy;
import org.streampipes.model.output.OutputStrategy;
import org.streampipes.model.schema.EventProperty;
import org.streampipes.model.schema.EventPropertyPrimitive;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.staticproperty.FreeTextStaticProperty;
import org.streampipes.model.staticproperty.MappingPropertyUnary;
import org.streampipes.model.staticproperty.StaticProperty;
import org.streampipes.model.util.SepaUtils;
import org.streampipes.pe.processors.esper.config.EsperConfig;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.vocabulary.MhWirth;
import org.streampipes.vocabulary.XSD;
import org.streampipes.wrapper.standalone.ConfiguredEventProcessor;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventProcessorDeclarerSingleton;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrillingStartEnrichedController extends
        StandaloneEventProcessorDeclarerSingleton<DrillingStartEnrichedParameters> {

  @Override
  public ConfiguredEventProcessor<DrillingStartEnrichedParameters>
  onInvocation(DataProcessorInvocation sepa) {
    int minRpm = Integer.parseInt(SepaUtils.getFreeTextStaticPropertyValue(sepa, "rpm"));
    int minTorque = Integer.parseInt(SepaUtils.getFreeTextStaticPropertyValue(sepa, "torque"));

    String latPropertyName = SepaUtils.getMappingPropertyName(sepa, "rpm");
    String lngPropertyName = SepaUtils.getMappingPropertyName(sepa, "torque");

    System.out.println(minRpm + ", " + minTorque + ", " + latPropertyName + ", " + lngPropertyName);
    DrillingStartEnrichedParameters staticParam = new DrillingStartEnrichedParameters(
            sepa,
            minRpm,
            minTorque,
            latPropertyName,
            lngPropertyName);

    return new ConfiguredEventProcessor<>(staticParam, DrillingStartEnriched::new);
  }

  @Override
  public DataProcessorDescription declareModel() {
    SpDataStream stream1 = new SpDataStream();

    EventSchema schema1 = new EventSchema();
    EventPropertyPrimitive p1 = EpRequirements.domainPropertyReq(MhWirth.Rpm);
    schema1.addEventProperty(p1);

    EventPropertyPrimitive p2 = EpRequirements.domainPropertyReq(MhWirth.Torque);
    schema1.addEventProperty(p2);


    DataProcessorDescription desc = new DataProcessorDescription("drillingstartenriched", "Drilling Status", "Detects a status change in a drilling process (drilling and cooling)");
    desc.setIconUrl(EsperConfig.iconBaseUrl + "/Drilling_Start_HQ.png");
    desc.setCategory(Arrays.asList(DataProcessorType.ALGORITHM.name()));

    stream1.setUri(EsperConfig.serverUrl + "/" + Utils.getRandomString());
    stream1.setEventSchema(schema1);
    desc.addEventStream(stream1);

    List<OutputStrategy> strategies = new ArrayList<OutputStrategy>();
    List<EventProperty> appendProperties = new ArrayList<EventProperty>();

    EventProperty result = new EventPropertyPrimitive(XSD._boolean.toString(),
            "drillingStatus", "", Utils.createURI(MhWirth.DrillingStatus));
    ;

    appendProperties.add(result);
    strategies.add(new AppendOutputStrategy(appendProperties));
    desc.setOutputStrategies(strategies);

    List<StaticProperty> staticProperties = new ArrayList<StaticProperty>();

    FreeTextStaticProperty rpmThreshold = new FreeTextStaticProperty("rpm", "RPM threshold", "");
    FreeTextStaticProperty torqueThreshold = new FreeTextStaticProperty("torque", "Torque threshold", "");
    staticProperties.add(rpmThreshold);
    staticProperties.add(torqueThreshold);

    staticProperties.add(new MappingPropertyUnary(URI.create(p1.getElementName()), "rpm", "Select RPM Mapping", ""));
    staticProperties.add(new MappingPropertyUnary(URI.create(p2.getElementName()), "torque", "Select Torque Mapping", ""));
    desc.setStaticProperties(staticProperties);
    desc.setSupportedGrounding(StandardTransportFormat.getSupportedGrounding());
    return desc;
  }

}
