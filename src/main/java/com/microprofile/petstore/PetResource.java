package com.microprofile.petstore;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import netscape.javascript.JSException;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/pets")
@Produces("application/json")
public class PetResource {

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "All Pets", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))) })
	@GET
	public Response getPets() {

		return Response.ok(PetData.getInstance().getArrayList()).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the id.") })
	@GET
	@Path("{petId}")
	public Response getPet(@PathParam("petId") int petId) {
		if (petId < 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Pet pet = new Pet();
		pet.setPetId(petId);
		pet.setPetAge(3);
		pet.setPetName("Buula");
		pet.setPetType("Dog");

		return Response.ok(pet).build();
		
	}

	@Path("/add")
	@POST

	public Response addPet(String requestget) throws JSONException{
		JSONObject jsonObject = new JSONObject(requestget);

		Pet pet = new Pet();
		List<Pet> pets = new ArrayList<Pet>();
		List<Pet> temp = new ArrayList<Pet>();

		if(jsonObject.has("name") && jsonObject.has("age") && jsonObject.has("type")){

			String petName = jsonObject.getString("name");
			int petAge = Integer.parseInt(jsonObject.getString("age"));
			String petType = jsonObject.getString("type");

			pet.setPetId(PetData.getInstance().getArrayList().get(PetData.getInstance().getArrayList().size()-1).getPetId()+1);
			pet.setPetName(petName);
			pet.setPetAge(petAge);
			pet.setPetType(petType);
			pets.add(pet);
			temp.addAll(PetData.getInstance().getArrayList());
			temp.addAll(pets);
			PetData.getInstance().setArrayList(temp);
			//petData.addData(pet);
			//return Response.ok("New Pet Added. To view : 'http://localhost:8080/data/pets'").build();
			return Response.ok(pet).build();
		}

		else{

			return Response.ok("{\n" + "\"successful\":false\n" + "}").build();

		}



	}

	@PUT
	@Produces("application/json")
	@Path("/edit/{petId}")
	public Response editPet(@PathParam("petId") int petId,String petData) throws JSONException {
		JSONObject jsonObject = new JSONObject(petData);
		boolean isUpdated = false;
		int id = 0;
		if(jsonObject.has("name")){
			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petId == PetData.getInstance().getArrayList().get(i).getPetId()){
					PetData.getInstance().getArrayList().get(i).setPetName(jsonObject.getString("name"));
					isUpdated=true;
					id = i;
				}
			}
		}
		if(jsonObject.has("age")){
			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petId == PetData.getInstance().getArrayList().get(i).getPetId()){
					PetData.getInstance().getArrayList().get(i).setPetAge(Integer.parseInt(jsonObject.getString("age")));
					isUpdated=true;
					id = i;
				}
			}
		}
		if(jsonObject.has("type")){
			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petId == PetData.getInstance().getArrayList().get(i).getPetId()){
					PetData.getInstance().getArrayList().get(i).setPetType(jsonObject.getString("type"));
					isUpdated=true;
					id = i;
				}
			}
		}
		if(isUpdated){
			return Response.ok(PetData.getInstance().getArrayList().get(id)).build();
		}else{
			return Response.ok("{\n" + "\"success\":false\n" + "}").build();
		}

	}

	@DELETE
	@Produces("application/json")
	@Path("/delete/{petId}")
	public Response deletePet(@PathParam("petId") int petId){
		boolean isPetFound = false;
		for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
			if(petId == PetData.getInstance().getArrayList().get(i).getPetId()){
				PetData.getInstance().getArrayList().remove(i);
				isPetFound = true;
			}
		}
		if(isPetFound){
			return Response.ok("{\n" + "\"successful\":true\n" + "}").build();
		}else{
			return Response.ok("{\n" + "\"successful\":false\n" + "}").build();
		}

	}

	@Path("/search")
	@Produces("application/json")
	@GET
	public Response searchPet(@DefaultValue("-1") @QueryParam("id") int petId,
							  @DefaultValue("null") @QueryParam("name") String petName,
							  @DefaultValue("0") @QueryParam("age") int petAge){
		boolean isPetFound = false;
		int id = 0;
		if(petId != -1 && petName.equals("null") && petAge == 0){
			if (petId < 0) {
				return Response.status(Status.NOT_FOUND).build();
			}

			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petId == PetData.getInstance().getArrayList().get(i).getPetId()){
					isPetFound = true;
					id = i;
				}
			}
			if(isPetFound){
				return Response.ok(PetData.getInstance().getArrayList().get(id)).build();
			}else{
				return Response.ok("There is no pet with id = "+petId).build();
			}
		}else if(petId == -1 && !petName.equals("null") && petAge == 0){
			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petName.equals(PetData.getInstance().getArrayList().get(i).getPetName())){
					isPetFound = true;
					id = i;
				}
			}
			if(isPetFound){
				return Response.ok(PetData.getInstance().getArrayList().get(id)).build();
			}else{
				return Response.ok("There is no pet with name = "+petName).build();
			}
		}else if(petId == -1 && petName.equals("null") && petAge != 0){
			List<Pet> temp = new ArrayList<Pet>();
			for (int i=0;i<PetData.getInstance().getArrayList().size();i++){
				if(petAge == PetData.getInstance().getArrayList().get(i).getPetAge()){
					isPetFound = true;
					id = i;
					temp.add(PetData.getInstance().getArrayList().get(id));
				}
			}
			if(isPetFound){
				return Response.ok(temp).build();
			}else{
				return Response.ok("There is no pet with age = "+petAge).build();
			}
		}else{
			return Response.status(Status.NOT_FOUND).build();
		}


	}


}
