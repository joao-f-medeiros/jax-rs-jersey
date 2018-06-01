package com.javaee.jersey.controllers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaee.jersey.domain.Vehicle;
import com.javaee.jersey.services.VehicleService;
import com.javaee.jersey.services.VehicleServiceImpl;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleController {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	private VehicleService vehicleService;
	
	public VehicleController() {
		vehicleService = new VehicleServiceImpl();
	}
    
    @GET
    public List<Vehicle> getAll() {
        logger.info("getAllVehicles: {}");
        List<Vehicle> vehicles = vehicleService.getAll();
        return vehicles;
    }
    
    @GET
    @Path("{id : \\d+}")
    public Response getById(@PathParam("id") Integer id) {
        logger.info("getById : {}", id);
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle == null) {
        	return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(vehicle).build();
    }
    
    @POST
    public Response create(Vehicle vehicle, @Context UriInfo uriInfo) {
        logger.info("create: {}", vehicle);
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
        logger.debug("Vehicle created with id = ", savedVehicle.getId());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(savedVehicle.getId().toString());
        return Response.created(builder.build()).entity(savedVehicle).build();
    }
    
    @PUT
    @Path("/{id : \\d+}")
    public Response update(@PathParam("id") Integer id, Vehicle vehicle) {
        logger.info("Vehicle ID: {} ", id, vehicle);
        Vehicle vehicleSaved = vehicleService.findById(id);
        if (vehicleSaved == null) {
        	return Response.status(Status.NOT_FOUND).build();
        }
        vehicleSaved = vehicleService.saveVehicle(vehicle);
        return Response.ok().entity(vehicleSaved).build();
    }
    
    @DELETE
    @Path("/{id : \\d+}")
    public Response delete(@PathParam("id") Integer id) {
    	logger.info("Vehicle ID: {} ", id);
    	vehicleService.deleteById(id);
    	return Response.noContent().build();
    }
}
