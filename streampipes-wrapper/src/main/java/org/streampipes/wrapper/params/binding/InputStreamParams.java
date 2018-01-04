package org.streampipes.wrapper.params.binding;

import org.streampipes.model.grounding.EventGrounding;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.SpDataStream;
import org.streampipes.model.util.SchemaUtils;

import java.io.Serializable;
import java.util.List;

public class InputStreamParams implements Serializable {

	private static final long serialVersionUID = -240772928651344246L;
	
	private EventGrounding eventGrounding;
	private EventSchema eventSchema;
	private String inName;

	public InputStreamParams(SpDataStream inputStream) {
		super();
		this.eventGrounding = inputStream.getEventGrounding();
		this.inName = eventGrounding.getTransportProtocol().getTopicName();
		this.eventSchema = inputStream.getEventSchema();
	}
	
	public EventGrounding getEventGrounding() {
		return eventGrounding;
	}
	
	public String getInName() {
		return inName;
	}
	
	public List<String> getAllProperties() {
		return SchemaUtils.toPropertyList(eventSchema.getEventProperties());
	}
	
}
