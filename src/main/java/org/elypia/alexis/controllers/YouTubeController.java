/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.controllers;

import com.google.api.services.youtube.model.SearchResult;
import org.elypia.alexis.services.youtube.*;
import org.elypia.commandler.api.Controller;

import javax.inject.*;
import java.io.IOException;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class YouTubeController implements Controller {

    private YouTubeService youtube;

    @Inject
    public YouTubeController(YouTubeService youtube) {
        this.youtube = youtube;
    }

    public Object search(String query) throws IOException {
        Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);
        return (searchResult.isPresent()) ? searchResult.get() : "There were no results for that.";
    }
}
