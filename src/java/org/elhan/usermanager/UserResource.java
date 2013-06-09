/**
 * *****************************************************************************
 * Copyright (c) 2013 Osman Alperen Elhan <alperen@elhan.org>. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this
 * distribution, and is available at http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: Osman Alperen Elhan <alperen@elhan.org> - initial API and
 * implementation
 * ****************************************************************************
 */
package org.elhan.usermanager;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elhan.exceptions.InternalServerErrorException;
import org.elhan.model.User;
import org.elhan.utils.Globals;

/**
 * REST Web Service
 *
 * @author Osman Alperen Elhan <alperen@elhan.org>
 */
@Path("/user")
public class UserResource {

    /**
     * Mongo Client
     */
    protected Mongo mongo;
    
    /**
     * Mongo Database
     */
    protected DB db;
    
    /**
     * Mongo Database Collection
     */
    protected DBCollection collection;

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
        try {
            mongo = new Mongo();
            db = mongo.getDB(Globals.DB_NAME);
            collection = db.getCollection(Globals.COLLECTION_NAME);
        } catch (Exception ex) {
            throw new InternalServerErrorException(Globals.COULD_NOT_CONNECT_TO_DATABASE);
        }
    }

    /**
     * GET method for retrieving list of User instances
     *
     * @return user list
     * @throws JSONException  
     */
    @GET
    @Produces("application/json; charset=UTF-8")
    public String getUsers() throws JSONException {
        DBCursor it = collection.find();
        JSONArray arr = new JSONArray();
        JSONObject root = new JSONObject();
        while (it.hasNext()) {
            DBObject curr = it.next();
            User user = Globals.dbobject2user(curr);
            JSONObject tmp = new JSONObject(user.toString());
            arr.put(tmp);
        }
        root.put("meta", new JSONObject().put("code", 200));
        if (collection.count() != 0) {
            root.put("data", arr);
        } else {
            root.put("data", JSONObject.NULL);
        }

        return root.toString();
    }

    /**
     * GET method for retrieving an instance of User
     *
     * @param userId id of a user
     * @return user with given id
     * @throws JSONException  
     */
    @GET
    @Produces("application/json; charset=UTF-8")
    @Path("/{userId}")
    public String getUser(@PathParam("userId") String userId) throws JSONException {
        DBObject item;
        DBObject found;
        try {
            item = new BasicDBObject("_id", new ObjectId(userId));
            found = collection.findOne(item);
            JSONObject root = new JSONObject();
            User user = Globals.dbobject2user(found);
            root.put("meta", new JSONObject().put("code", 200))
                    .put("data", new JSONObject(user.toString()));
            return root.toString();
        } catch (Exception e) {
            JSONObject jb = new JSONObject();
            jb.put("meta", new JSONObject().put("code", 102)
                    .put("type", "NotFoundError")
                    .put("errors", new JSONObject()
                    .put("fieldName", "id")
                    .put("rejectedValue", userId)));
            throw new InternalServerErrorException(jb.toString());
        }
    }

    /**
     * POST method for creating an instance of User
     *
     * @param content representation for the resource
     * @return an HTTP response with result of create operation.
     * @throws JSONException  
     */
    @POST
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public String postUser(String content) throws JSONException {
        JSONObject jb = new JSONObject(content).getJSONObject("user");
        String cont = jb.toString();
        User user = new User(cont);
        boolean anyError = false;
        jb = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject tmp;
        if (user.getUsername() == null) {
            arr.put(new JSONObject().put("fieldName", "username")
                    .put("rejectedValue", JSONObject.NULL));
            anyError = true;
        }
        if (user.getFullname() == null) {
            arr.put(new JSONObject().put("fieldName", "fullname")
                    .put("rejectedValue", JSONObject.NULL));
            anyError = true;
        }
        if (user.getGender() == null) {
            arr.put(new JSONObject().put("fieldName", "gender")
                    .put("rejectedValue", JSONObject.NULL));
            anyError = true;
        }
        if (user.getBirthDate() == null) {
            arr.put(new JSONObject().put("fieldName", "birthDate")
                    .put("rejectedValue", JSONObject.NULL));
            anyError = true;
        }
        if (anyError) {
            jb.put("meta", new JSONObject().put("code", 102)
                .put("type", "FieldError")
                .put("errors", arr));
            throw new InternalServerErrorException(jb.toString());
        }
        DBObject curr = Globals.user2dbobject(user);
        collection.insert(curr);
        ObjectId id = (ObjectId) curr.get("_id");
        user.setId(id.toString());
        collection.update(curr, Globals.user2dbobject(user));
        return Globals.SUCCESFULLY_CREATED;
    }

    /**
     * PUT method for updating an instance of User
     *
     * @param userId id of a user
     * @param content 
     * @return an HTTP response with result of update operation
     * @throws JSONException  
     */
    @PUT
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    @Path("/{userId}")
    public String putUser(@PathParam("userId") String userId, String content) throws JSONException {
        DBObject item;
        DBObject found;
        JSONObject jb = new JSONObject(content).getJSONObject("user");
        String cont = jb.toString();
        try {
            item = new BasicDBObject("_id", new ObjectId(userId));
            found = collection.findOne(item);
            User before = Globals.dbobject2user(found);
            before.updateUser(new User(cont));
            collection.update(found, Globals.user2dbobject(before));
            return Globals.SUCCESFULLY_UPDATED;
        } catch (Exception e) {
            jb = new JSONObject();            
            jb.put("meta", new JSONObject().put("code", 102)
                    .put("type", "NotFoundError")
                    .put("errors", new JSONObject()
                    .put("fieldName", "id")
                    .put("rejectedValue", userId)));
            throw new InternalServerErrorException(jb.toString());
        }
    }

    /**
     * DELETE method for deleting an instance of User
     *
     * @param userId id of a user
     * @return an HTTP response with result of delete operation
     * @throws JSONException  
     */
    @DELETE
    @Path("/{userId}")
    @Produces("application/json; charset=UTF-8")
    public String deleteUser(@PathParam("userId") String userId) throws JSONException {
        DBObject item;
        DBObject found;
        try {
            item = new BasicDBObject("_id", new ObjectId(userId));
            found = collection.findOne(item);
            collection.remove(found);
            return Globals.SUCCESFULLY_DELETED;
        } catch (Exception e) {
            JSONObject jb = new JSONObject();

            jb.put("meta", new JSONObject().put("code", 102)
                    .put("type", "NotFoundError")
                    .put("errors", new JSONObject()
                    .put("fieldName", "id")
                    .put("rejectedValue", userId)));
            throw new InternalServerErrorException(jb.toString());
        }
    }

    /**
     * DELETE method for deleting all users in mongodb
     * 
     * @return an HTTP response with result of drop operation
     */
    @DELETE
    @Produces("application/json; charset=UTF-8")
    public String deleteUser() {
        db.dropDatabase();
        return Globals.SUCCESFULLY_DROPPED;
    }
}
