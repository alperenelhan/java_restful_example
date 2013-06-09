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
package org.elhan.utils;

import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.elhan.model.User;

/**
 * Holds the static required variables and methods
 *
 * @author Osman Alperen Elhan <alperen@elhan.org>
 */
public class Globals {
    
    // Constants
    /**
     * Name of the mongodb database.
     */
    public static final String DB_NAME = "usermanager";
    
    /**
     * Name of the mongodb database collection.
     */
    public static final String COLLECTION_NAME = "users";
    
    
    
    // HTTP Responses
    /**
     *  If application cannot connect to server, then throws this data in 
     *  exception.
     */
    public static final String COULD_NOT_CONNECT_TO_DATABASE = "{\"meta\":{\"code\":105},\"data\":{\"message\":\"Couldn't connect to database.\"}}";
    
    /**
     * If a user succesfully created as a result of the POST operation, then
     * application returns this data.
     */
    public static final String SUCCESFULLY_CREATED = "{\"meta\":{\"code\":200},\"data\":{\"message\":\"You successfully created a user.\"}}";
    
    /**
     * If a user succesfully updated as a result of the PUT operation, then
     * application returns this data.
     */
    public static final String SUCCESFULLY_UPDATED = "{\"meta\":{\"code\":200},\"data\":{\"message\":\"You successfully updated user.\"}}";
    
    /**
     * If a user succesfully deleted as a result of the DELETE operation, then
     * application returns this data.
     */
    public static final String SUCCESFULLY_DELETED = "{\"meta\":{\"code\":200},\"data\":{\"message\":\"You successfully deleted user.\"}}";
    
    /**
     * If database dropped after a DELETE operation, then application returns
     * this data.
     */
    public static final String SUCCESFULLY_DROPPED = "{\"meta\":{\"code\":200},\"data\":{\"message\":\"You successfully dropped database.\"}}";
    
    
    
    // Static methods
    /**
     * Converts instance of a user to an instance of mongo DBOject
     * 
     * @param user instance of a user
     * @return DBObject
     */
    public static DBObject user2dbobject(User user) {
        return (DBObject) JSON.parse(user.toString());
    }

    /**
     * Converts instance of a DBObject to an instance of user object
     *
     * @param dbobj instance of a DBObject
     * @return User
     */
    public static User dbobject2user(DBObject dbobj) {
        return new GsonBuilder().serializeNulls().disableHtmlEscaping().create()
                .fromJson(JSON.serialize(dbobj), User.class);
    }
    
}
