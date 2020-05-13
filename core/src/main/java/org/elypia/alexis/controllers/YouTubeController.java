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

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.SearchResult;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.services.youtube.*;
import org.elypia.commandler.api.Controller;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class YouTubeController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeController.class);

    private YouTubeService youtube;
    private AlexisMessages messages;

    @Inject
    public YouTubeController(YouTubeService youtube, AlexisMessages messages) {
        this.youtube = youtube;
        this.messages = messages;
    }

    public Object search(String query) throws IOException {
        try {
            Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);
            return (searchResult.isPresent()) ? searchResult.get() : messages.noSearchResultsFound();
        } catch (GoogleJsonResponseException ex) {
            logger.error("The YouTube API hasn't been configured propertly: {}", ex.getDetails().getMessage());
            return messages.youtubeApiError();
        }
    }
}
