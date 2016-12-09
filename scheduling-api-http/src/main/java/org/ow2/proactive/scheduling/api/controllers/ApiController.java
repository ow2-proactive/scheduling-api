/*
 * ProActive Parallel Suite(TM):
 * The Java(TM) library for Parallel, Distributed,
 * Multi-Core Computing for Enterprise Grids & Clouds
 *
 * Copyright (c) 2016 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduling.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ApiController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "/home.html";
    }

    @RequestMapping(value = "/v1/rest", method = { RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD,
                                                   RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT,
                                                   RequestMethod.TRACE })
    @ResponseBody
    public String v1() {
        return "TODO: forward to /rest/";
    }

    @RequestMapping(value = "/v1/graphiql", method = RequestMethod.GET)
    public String v2() {
        return "/index.html";
    }

}
