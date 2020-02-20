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

package org.elypia.alexis.messengers;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.services.youtube.YouTubeService;
import org.elypia.alexis.utils.*;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.inject.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class YouTubeMessenger implements DiscordMessenger<SearchResult> {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeMessenger.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd MMM yyyy");

    private YouTubeService youtube;

    @Inject
    public YouTubeMessenger(YouTubeService youtube) {
        this.youtube = youtube;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, SearchResult output) {
        String videoId = output.getId().getVideoId();
        String url = YouTubeUtils.getVideoUrl(videoId);
        return new MessageBuilder(url).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, SearchResult output) {
        SearchResultSnippet snippet = output.getSnippet();
        String videoId = output.getId().getVideoId();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(snippet.getChannelTitle());
        builder.setTitle(snippet.getTitle(), YouTubeUtils.getVideoUrl(videoId));
        builder.setDescription(snippet.getDescription());
        builder.setImage(YouTubeUtils.getThumbnailUrl(videoId));

        DateTime datetime = snippet.getPublishedAt();
        long milli = datetime.getValue();
        Date date = new Date(milli);

        builder.setFooter("Published on " + FORMAT.format(date), null);

        try {
            builder.setThumbnail(youtube.getChannelThumbnail(snippet.getChannelId()));
        } catch (IOException e) {
            logger.error("Obtaining the thumbnail failed, and it isn't cached either.", e);
        }

        return new MessageBuilder(builder.build()).build();
    }
}