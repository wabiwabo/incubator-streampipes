/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.connect.rest.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streampipes.connect.exception.AdapterException;
import org.streampipes.connect.management.AdapterWorkerManagement;
import org.streampipes.connect.management.IAdapterWorkerManagement;
import org.streampipes.connect.rest.AbstractContainerResource;
import org.streampipes.model.Notification;
import org.streampipes.model.client.messages.Notifications;
import org.streampipes.model.connect.adapter.AdapterDescription;
import org.streampipes.model.connect.adapter.AdapterSetDescription;
import org.streampipes.model.connect.adapter.AdapterStreamDescription;
import org.streampipes.rest.shared.annotation.JsonLdSerialized;
import org.streampipes.rest.shared.util.SpMediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/{username}/worker")
public class WorkerResource extends AbstractContainerResource {

    Logger logger = LoggerFactory.getLogger(WorkerResource.class);

    private IAdapterWorkerManagement adapterManagement;

    public WorkerResource() {
        adapterManagement = new AdapterWorkerManagement();
    }

    public WorkerResource(IAdapterWorkerManagement adapterManagement) {
        this.adapterManagement = adapterManagement;
    }

    @POST
    @JsonLdSerialized
    @Path("/stream/invoke")
    @Consumes(SpMediaType.JSONLD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response invokeStreamAdapter(AdapterStreamDescription adapterStreamDescription) {

        logger.info("yea");


        try {
            adapterManagement.invokeStreamAdapter(adapterStreamDescription);
        } catch (AdapterException e) {
            e.printStackTrace();
            return ok(Notifications.error(e.getMessage()));
        }

        return ok(Notifications.success("Stream adapter successfully started"));
    }

    @POST
    @JsonLdSerialized
    @Path("/stream/stop")
    @Consumes(SpMediaType.JSONLD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopStreamAdapter(AdapterStreamDescription adapterStreamDescription) {

        try {
            adapterManagement.stopStreamAdapter(adapterStreamDescription);
        } catch (AdapterException e) {
            e.printStackTrace();
            return ok(Notifications.error(e.getMessage()));
        }

        return ok(Notifications.success("Stream adapter successfully stopped"));
    }

    @POST
    @JsonLdSerialized
    @Path("/set/invoke")
    @Consumes(SpMediaType.JSONLD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response invokeSetAdapter(AdapterSetDescription adapterSetDescription) {

        try {
            adapterManagement.invokeSetAdapter(adapterSetDescription);
        } catch (AdapterException e) {
            e.printStackTrace();
            return ok(Notifications.error(e.getMessage()));
        }

        return ok(Notifications.success("Set adapter successfully started"));
    }

    @POST
    @JsonLdSerialized
    @Path("/set/stop")
    @Consumes(SpMediaType.JSONLD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopSetAdapter(AdapterSetDescription adapterSetDescription){
         try {
             adapterManagement.stopSetAdapter(adapterSetDescription);
        } catch (AdapterException e) {
            e.printStackTrace();
            return ok(Notifications.error(e.getMessage()));
        }

        return ok(Notifications.success("Set adapter successfully stopped"));
    }

    public void setAdapterManagement(IAdapterWorkerManagement adapterManagement) {
        this.adapterManagement = adapterManagement;
    }

}