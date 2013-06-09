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
package org.elhan.model;

import com.google.gson.GsonBuilder;

/**
 * Model for user resource
 *
 * @author Osman Alperen Elhan <alperen@elhan.org>
 */
public class User {

    /**
     * User id
     */
    protected String id;
    
    /**
     * User name (nickname)
     *      For Example: johndoe
     */
    protected String username;
    
    /**
     * Full name of the user
     *      For Example: John Doe
     */
    protected String fullname;
    
    /**
     * Gender of the user
     *      For Example: "M" or "F"
     */
    protected String gender;
    
    /**
     * BirthDate of the user
     *      For Example: 21.12.2012
     */
    protected String birthDate;
    
    /**
     * Using google-gson library this creates an instance of User object
     * from json dump of a User object
     * 
     * @param json json dump of a User instance
     */
    public User(String json) {
        User tmpUser = new GsonBuilder().serializeNulls()
                .create().fromJson(json, User.class);
        username = tmpUser.username ;
        fullname = tmpUser.fullname;
        gender = tmpUser.gender;
        birthDate = tmpUser.birthDate;
    }
    
    /**
     * Update current instance of User from another user instance. If values
     * are null or empty it won't update specified field.
     *
     * @param user Instance of User, holder of values that will be updated
     */
    public void updateUser (User user) {
        if (user.username != null && !user.username.isEmpty()) {
            username = user.username ;
        }
        if (user.fullname != null && !user.fullname.isEmpty()) {
            fullname = user.fullname;
        }
        if (user.gender != null && !user.gender.isEmpty()) {
            gender = user.gender;
        }
        if (user.birthDate != null && !user.birthDate.isEmpty()) {
            birthDate = user.birthDate;
        }
    }


    /**
     * Returns the json dump of User instance with serialized null values.
     *
     */
    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create()
                .toJson(this);
    }

    /**
     * Gets the username (nickname) of the user
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the full name of the user
     * 
     * @return fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Gets the gender of the user
     * 
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the birthdate of the user
     *
     * @return birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the id of the user
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}
