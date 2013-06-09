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
package org.elhan.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles various exceptions
 * 
 * @author Osman Alperen Elhan <alperen@elhan.org>
 */
public class InternalServerErrorException extends WebApplicationException {

    /**
     * Creates an instance of InternalServerErrorException
     * 
     * @param message content of the exception
     */
    public InternalServerErrorException(String message) {
        
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message).type(MediaType.APPLICATION_JSON_TYPE).build());
    }    
}